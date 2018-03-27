package com.barablah.web;

import com.barablah.auth.Authenticator;
import com.barablah.domain.*;
import com.barablah.netease.NeteaseClient;
import com.barablah.netease.request.ImUserCreateRequest;
import com.barablah.netease.request.ImUserUpdateRequest;
import com.barablah.netease.response.ImUserCreateResponse;
import com.barablah.repository.*;
import com.barablah.repository.example.*;
import com.barablah.web.enums.BarablahClassLessonStatusEnum;
import com.barablah.web.enums.PointLogObjectTypeEnum;
import com.barablah.web.request.*;
import com.barablah.web.response.*;
import com.barablah.wechat.mp.WxMpClient;
import com.barablah.wechat.mp.request.SnsTokenRequest;
import com.barablah.wechat.mp.request.SnsUserInfoRequest;
import com.barablah.wechat.mp.response.SnsTokenResponse;
import com.barablah.wechat.mp.response.SnsUserInfoResponse;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by xyqin on 2017/4/1.
 */
@Slf4j
@Api(tags = "会员", description = "会员账号相关接口")
@RestController
public class MemberController {

    @Autowired
    private Authenticator authenticator;

    @Autowired
    private BarablahMemberMapper memberMapper;

    @Autowired
    private BarablahMemberLessonMapper memberLessonMapper;

    @Autowired
    private BarablahMemberPassportMapper memberPassportMapper;

    @Autowired
    private BarablahMemberHomeworkMapper memberHomeworkMapper;

    @Autowired
    private BarablahMemberHomeworkItemMapper memberHomeworkItemMapper;

    @Autowired
    private BarablahMemberCommentMapper memberCommentMapper;

    @Autowired
    private BarablahMemberPointLogMapper memberPointLogMapper;

    @Autowired
    private BarablahClassLessonMapper lessonMapper;

    @Autowired
    private BarablahTeacherMapper teacherMapper;

    @Autowired
    private BarablahClassMapper classMapper;

    @Autowired
    private BarablahClassMemberMapper classMemberMapper;

    @Autowired
    private BarablahCampusMapper campusMapper;

    @Autowired
    private BarablahTextbookMapper textbookMapper;

    @Autowired
    private BarablahPositionContentMapper positionContentMapper;

    @Autowired
    private NeteaseClient neteaseClient;

    @Autowired
    @Qualifier("wechatAppClient")
    private WxMpClient wxMpClient;

    @Autowired
    private StringRedisTemplate redisTemplate;


    /* 会员信息及账号相关接口 */

    @ApiOperation(value = "注册会员")
    @PostMapping(value = "/members/signup")
    public ApiEntity signup(@Valid @RequestBody SignupRequest request) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String number = ops.get("verifications:code:" + request.getCode());

        if (!Objects.equals(request.getPhoneNumber(), number)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "短信验证码错误");
        } else {
            redisTemplate.delete("verifications:code:" + request.getCode());
        }

        BarablahMemberPassportExample example = new BarablahMemberPassportExample();
        example.createCriteria()
                .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                .andProviderIdEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahMemberPassport> passports = memberPassportMapper.selectByExample(example);

        if (CollectionUtils.isNotEmpty(passports)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "手机号已被注册");
        }

        // 注册网易云IM账号
        ImUserCreateRequest imUserCreateRequest = new ImUserCreateRequest();
        imUserCreateRequest.setAccid("member_" + request.getPhoneNumber());
        ImUserCreateResponse imUserCreateResponse = null;

        try {
            imUserCreateResponse = neteaseClient.execute(imUserCreateRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!imUserCreateResponse.isSuccess()) {
            log.error("controller:members:signup:调用网易云注册IM账号失败, code=" + imUserCreateResponse.getCode());
            ImUserUpdateRequest updateRequest = new ImUserUpdateRequest();
            updateRequest.setAccid("member_" + request.getPhoneNumber());
            try {
                //return new ApiEntity(ApiStatus.STATUS_400.getCode(), "注册网易云账号失败,手机号已注册过");
                imUserCreateResponse = neteaseClient.execute(updateRequest);
            } catch (IOException e) {
                return new ApiEntity(ApiStatus.STATUS_500.getCode(),"手机号已注册过");
            }
        }

        BarablahMember member = new BarablahMember();
        member.setCampusId(request.getCampusId());
        member.setAccid(imUserCreateResponse.getInfo().getAccid());
        member.setToken(imUserCreateResponse.getInfo().getToken());
        memberMapper.insertSelective(member);

        BarablahMemberPassport passport = new BarablahMemberPassport();
        passport.setMemberId(member.getId());
        passport.setProvider(MemberPassportProvider.PHONE);
        passport.setProviderId(request.getPhoneNumber());
        passport.setPassword(DigestUtils.md5Hex(request.getPassword()));
        memberPassportMapper.insertSelective(passport);

        // 往session中设置会员ID
        authenticator.newSession(member.getId());
        return new ApiEntity();
    }

    @ApiOperation(value = "手机号登录")
    @PostMapping(value = "/members/signin")
    public ApiEntity<SigninResponse> signin(@Valid @RequestBody SigninRequest request) {
        BarablahMemberPassportExample example = new BarablahMemberPassportExample();
        example.createCriteria()
                .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                .andProviderIdEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahMemberPassport> passports = memberPassportMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(passports)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "手机号尚未注册");
        }

        BarablahMemberPassport passport = passports.get(0);

        if (!Objects.equals(DigestUtils.md5Hex(request.getPassword()), passport.getPassword())) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "密码错误");
        }

        BarablahMember member = memberMapper.selectByPrimaryKey(passports.get(0).getMemberId());

        if (MemberStatus.DISABLED.equals(member.getStatus())) {
            return new ApiEntity<>(ApiStatus.STATUS_400.getCode(), "账号已禁用");
        }

        // 往session中设置会员ID
        authenticator.newSession(member.getId());

        SigninResponse response = new SigninResponse();
        response.setNickname(member.getNickname());
        response.setAvatar(member.getAvatar());
        return new ApiEntity(response);
    }

    @ApiOperation(value = "微信授权登录")
    @PostMapping(value = "/members/signin/wechat")
    public ApiEntity<SigninResponse> signinByWechat(@Valid @RequestBody SigninWechatRequest body) throws Exception {
        log.info("controller:members:signin:wechat:调用微信公众号获取sns token, oauth_code={}", body.getCode());
        SnsTokenRequest tokenRequest = new SnsTokenRequest();
        tokenRequest.setCode(body.getCode());
        SnsTokenResponse tokenResponse = wxMpClient.execute(tokenRequest);

        if (tokenResponse.getErrcode() == 40029) {
            return new ApiEntity<>(ApiStatus.STATUS_400.getCode(), "不合法的 oauth_code");
        } else if (!tokenResponse.isSuccess()) {
            log.error("controller:members:signin:wechat:调用微信公众号获取sns token失败, code={}, msg={}",
                    tokenResponse.getErrcode(), tokenResponse.getErrmsg());
            return new ApiEntity<>(ApiStatus.STATUS_500);
        }

        SnsUserInfoRequest userInfoRequest = new SnsUserInfoRequest();
        userInfoRequest.setAccessToken(tokenResponse.getAccessToken());
        userInfoRequest.setOpenid(tokenResponse.getOpenid());
        SnsUserInfoResponse userInfoResponse = wxMpClient.execute(userInfoRequest);

        if (!userInfoResponse.isSuccess()) {
            log.error("controller:members:signin:wechat:调用微信公众号获取sns用户信息异常, code={}, msg={}",
                    userInfoResponse.getErrcode(), userInfoResponse.getErrmsg());
            return new ApiEntity<>(ApiStatus.STATUS_500);
        }

        BarablahMember memberToBeSave = new BarablahMember();
        memberToBeSave.setNickname(userInfoResponse.getNickname());
        memberToBeSave.setAvatar(userInfoResponse.getHeadimgurl());

        if (userInfoResponse.getSex() == 1) {
            memberToBeSave.setGender(MemberGender.MALE);
        } else if (userInfoResponse.getSex() == 2) {
            memberToBeSave.setGender(MemberGender.FEMALE);
        } else {
            memberToBeSave.setGender(MemberGender.MALE);
        }

        // 查找微信账号
        BarablahMemberPassportExample example = new BarablahMemberPassportExample();
        example.createCriteria()
                .andProviderEqualTo(MemberPassportProvider.WECHAT.name())
                .andProviderIdEqualTo(userInfoResponse.getOpenid())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahMemberPassport> passports = memberPassportMapper.selectByExample(example);
        boolean phoneBound = false;

        if (CollectionUtils.isEmpty(passports)) { // 注册新会员
            log.info("controller:members:signin:wechat:微信会员尚未注册, openid={}", userInfoResponse.getOpenid());
            memberMapper.insertSelective(memberToBeSave);

            BarablahMemberPassport passport = new BarablahMemberPassport();
            passport.setMemberId(memberToBeSave.getId());
            passport.setProvider(MemberPassportProvider.WECHAT);
            passport.setProviderId(userInfoResponse.getOpenid());
            memberPassportMapper.insertSelective(passport);
        } else { // 更新会员信息
            BarablahMemberPassport passport = passports.get(0);
            log.info("controller:members:signin:wechat:更新微信会员信息, openid={}, memberId={}",
                    userInfoResponse.getOpenid(), passport.getMemberId());
            BarablahMember member = memberMapper.selectByPrimaryKey(passport.getMemberId());

            if (MemberStatus.DISABLED.equals(member.getStatus())) {
                return new ApiEntity<>(ApiStatus.STATUS_400.getCode(), "账号已禁用");
            }

            memberToBeSave.setId(member.getId());
            memberMapper.updateByPrimaryKeySelective(memberToBeSave);

            // 查找是否已绑定手机号
            example.clear();
            example.createCriteria()
                    .andMemberIdEqualTo(member.getId())
                    .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                    .andDeletedEqualTo(Boolean.FALSE);
            List<BarablahMemberPassport> phonePassports = memberPassportMapper.selectByExample(example);

            if (CollectionUtils.isNotEmpty(phonePassports)) {
                phoneBound = true;
            }
        }

        // 往session中设置会员ID
        authenticator.newSession(memberToBeSave.getId());

        SigninResponse response = new SigninResponse();
        response.setNickname(memberToBeSave.getNickname());
        response.setAvatar(memberToBeSave.getAvatar());
        response.setPhoneBound(phoneBound);
        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "绑定手机号预检查")
    @PostMapping(value = "/members/phone/bind/precheck")
    public ApiEntity checkBindPhone(@Valid @RequestBody PrecheckBindPhoneRequest request) {
        if (!authenticator.isAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String number = ops.get("verifications:code:" + request.getCode());

        if (!Objects.equals(request.getPhoneNumber(), number)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "短信验证码错误");
        } else {
            redisTemplate.delete("verifications:code:" + request.getCode());
        }

        Long memberId = authenticator.getCurrentMemberId();
        BarablahMemberPassportExample example = new BarablahMemberPassportExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahMemberPassport> passports = memberPassportMapper.selectByExample(example);

        if (CollectionUtils.isNotEmpty(passports)) {
            log.info("controller:members:phone:bind:check:当前登录用户已绑定过手机号, memberId={}", memberId);
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "您已经绑定过手机号");
        }

        example.clear();
        example.createCriteria()
                .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                .andProviderIdEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahMemberPassport> phonePassports = memberPassportMapper.selectByExample(example);

        if (CollectionUtils.isNotEmpty(phonePassports)) {
            BarablahMemberPassport phonePassport = phonePassports.get(0);
            example.clear();
            example.createCriteria()
                    .andProviderEqualTo(MemberPassportProvider.WECHAT.name())
                    .andMemberIdEqualTo(phonePassport.getMemberId())
                    .andDeletedEqualTo(Boolean.FALSE);
            List<BarablahMemberPassport> wechatPassports = memberPassportMapper.selectByExample(example);

            if (CollectionUtils.isNotEmpty(wechatPassports)) {
                log.info("controller:members:phone:bind:check:手机号已注册且已绑定微信, memberId={}, phone={}, openid={}",
                        wechatPassports.get(0).getMemberId(), phonePassport.getProviderId(), wechatPassports.get(0).getProviderId());
                return new ApiEntity(ApiStatus.STATUS_400.getCode(), "您的手机号已经绑定了其它微信");
            }

            return new ApiEntity(ApiStatus.STATUS_300);
        }

        return new ApiEntity();
    }

    @ApiOperation(value = "绑定手机号")
    @PostMapping(value = "/members/phone/bind")
    public ApiEntity bindPhone(@Valid @RequestBody BindPhoneRequest request) {
        if (!authenticator.isAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();

        BarablahMemberPassportExample example = new BarablahMemberPassportExample();
        example.createCriteria()
                .andProviderEqualTo(MemberPassportProvider.WECHAT.name())
                .andMemberIdEqualTo(memberId)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahMemberPassport> wechatPassports = memberPassportMapper.selectByExample(example);
        BarablahMemberPassport wechatPassport = wechatPassports.get(0);

        example.clear();
        example.createCriteria()
                .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                .andProviderIdEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahMemberPassport> phonePassports = memberPassportMapper.selectByExample(example);

        BarablahMember memberToBeUpdated = new BarablahMember();
        memberToBeUpdated.setId(memberId);
        memberToBeUpdated.setCampusId(request.getCampusId());

        if (CollectionUtils.isEmpty(phonePassports)) { // 手机号尚未注册
            log.info("controller:members:phone:bind:手机号尚未注册创建新的手机账号, phone={}", request.getPhoneNumber());
            // 注册网易云IM账号
            ImUserCreateRequest imUserCreateRequest = new ImUserCreateRequest();
            imUserCreateRequest.setAccid("member_" + request.getPhoneNumber());
            ImUserCreateResponse imUserCreateResponse = null;

            try {
                imUserCreateResponse = neteaseClient.execute(imUserCreateRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            if (!imUserCreateResponse.isSuccess()) {
//                log.error("controller:members:phone:bind:调用网易云注册IM账号失败, code=" + imUserCreateResponse.getCode());
//                return new ApiEntity(ApiStatus.STATUS_500);
//            }

            if (!imUserCreateResponse.isSuccess()) {
                log.error("controller:members:phone:bind:调用网易云注册IM账号失败, code=" + imUserCreateResponse.getCode());
                ImUserUpdateRequest updateRequest = new ImUserUpdateRequest();
                updateRequest.setAccid("member_" + request.getPhoneNumber());
                try {
                    //return new ApiEntity(ApiStatus.STATUS_400.getCode(), "注册网易云账号失败,手机号已注册过");
                    imUserCreateResponse = neteaseClient.execute(updateRequest);
                } catch (IOException e) {
                    return new ApiEntity(ApiStatus.STATUS_500.getCode(),"手机号已注册过");
                }
            }

            memberToBeUpdated.setAccid(imUserCreateResponse.getInfo().getAccid());
            memberToBeUpdated.setToken(imUserCreateResponse.getInfo().getToken());

            // 手机号未绑定直接绑定
            BarablahMemberPassport passportToBeCreated = new BarablahMemberPassport();
            passportToBeCreated.setMemberId(memberId);
            passportToBeCreated.setProvider(MemberPassportProvider.PHONE);
            passportToBeCreated.setProviderId(request.getPhoneNumber());
            passportToBeCreated.setPassword(DigestUtils.md5Hex(request.getPassword()));
            memberPassportMapper.insertSelective(passportToBeCreated);
        } else { // 手机号已注册
            BarablahMember wechatMember = memberMapper.selectByPrimaryKey(memberId);
            BarablahMemberPassport phonePassport = phonePassports.get(0);
            log.info("controller:members:phone:bind:手机号已注册尚未绑定微信, memberId={}, phone={}",
                    phonePassport.getMemberId(), phonePassport.getProviderId());

            // 使用老的手机账号并使用用微信用户信息
            BarablahMember phoneMember = memberMapper.selectByPrimaryKey(phonePassport.getMemberId());
            memberToBeUpdated.setId(phoneMember.getId());
            memberToBeUpdated.setCampusId(phoneMember.getCampusId());
            memberToBeUpdated.setNickname(wechatMember.getNickname());
            memberToBeUpdated.setAvatar(wechatMember.getAvatar());
            memberToBeUpdated.setGender(wechatMember.getGender());

            // 更新手机账号的密码
            BarablahMemberPassport passportToBeUpdated = new BarablahMemberPassport();
            passportToBeUpdated.setId(phonePassport.getId());
            passportToBeUpdated.setPassword(DigestUtils.md5Hex(request.getPassword()));
            memberPassportMapper.updateByPrimaryKeySelective(passportToBeUpdated);

            // 更新微信账号的memberId
            passportToBeUpdated = new BarablahMemberPassport();
            passportToBeUpdated.setId(wechatPassport.getId());
            passportToBeUpdated.setMemberId(phonePassport.getMemberId());
            memberPassportMapper.updateByPrimaryKeySelective(passportToBeUpdated);

            // 注销当前微信账号session
            authenticator.invalidateSession();
            authenticator.newSession(phoneMember.getId());
        }

        // 会员信息
        memberMapper.updateByPrimaryKeySelective(memberToBeUpdated);
        log.info("controller:members:phone:bind:绑定手机成功, memberId={}, phone={}",
                memberId, request.getPhoneNumber());
        return new ApiEntity();
    }

    @ApiOperation(value = "忘记密码")
    @PostMapping(value = "/members/password/reset")
    public ApiEntity resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String number = ops.get("verifications:code:" + request.getCode());

        if (!Objects.equals(request.getPhoneNumber(), number)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "短信验证码错误");
        }

        BarablahMemberPassportExample example = new BarablahMemberPassportExample();
        example.createCriteria()
                .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                .andProviderIdEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahMemberPassport> passports = memberPassportMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(passports)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "手机号尚未注册, number=" + request.getPhoneNumber());
        } else {
            redisTemplate.delete("verifications:code:" + request.getCode());
        }

        BarablahMemberPassport passportToBeUpdated = new BarablahMemberPassport();
        passportToBeUpdated.setId(passports.get(0).getId());
        passportToBeUpdated.setPassword(DigestUtils.md5Hex(request.getPassword()));
        memberPassportMapper.updateByPrimaryKeySelective(passportToBeUpdated);
        return new ApiEntity();
    }

    @ApiOperation(value = "获取会员信息")
    @GetMapping(value = "/members")
    public ApiEntity<GetMemberResponse> getMember() {
        if (!authenticator.isAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        BarablahMember member = memberMapper.selectByPrimaryKey(memberId);
        BarablahCampus campus = campusMapper.selectByPrimaryKey(member.getCampusId());
        BarablahMemberPassportExample example = new BarablahMemberPassportExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andProviderEqualTo(MemberPassportProvider.WECHAT.name())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahMemberPassport> passports = memberPassportMapper.selectByExample(example);

        // 获取会员信息
        GetMemberResponse response = new GetMemberResponse();
        response.setId(member.getId());
        response.setNickname(member.getNickname());
        response.setAvatar(member.getAvatar());
        response.setBirthday(member.getBirthday());
        response.setEnglishName(member.getEnglishName());
        response.setGender(member.getGender().name());
        response.setPoints(member.getPoints());
        response.setWechatBound(CollectionUtils.isNotEmpty(passports));
        BarablahMemberPointLogExample bp = new BarablahMemberPointLogExample();
        bp.createCriteria().andMemberIdEqualTo(memberId);

        List<BarablahMemberPointLog> bps = memberPointLogMapper.selectByExample(bp);
        int count = 0;
        for(BarablahMemberPointLog log:bps) {
            count = count + log.getPoints();
        }
        response.setPoints(count);

        if (Objects.nonNull(campus)) {
            response.setCampus(campus.getCampusName());
        }

        return new ApiEntity(response);
    }

    @ApiOperation(value = "更新会员信息")
    @PostMapping(value = "/members/update")
    public ApiEntity updateMemberInfo(@RequestBody UpdateMemberInfoRequest request) {
        if (!authenticator.isAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        MemberGender gender = null;

        try {
            gender = MemberGender.valueOf(request.getGender());
        } catch (IllegalArgumentException e) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "性别值不合法，必须为：MALE或FEMALE");
        }

        Long memberId = authenticator.getCurrentMemberId();
        BarablahMember memberToBeUpdated = new BarablahMember();
        memberToBeUpdated.setId(memberId);
        memberToBeUpdated.setCampusId(request.getCampusId());
        memberToBeUpdated.setNickname(request.getNickname());
        memberToBeUpdated.setAvatar(request.getAvatar());
        memberToBeUpdated.setBirthday(request.getBirthday());
        memberToBeUpdated.setEnglishName(request.getEnglishName());
        memberToBeUpdated.setGender(gender);

        // 更新会员信息
        memberMapper.updateByPrimaryKeySelective(memberToBeUpdated);
        return new ApiEntity();
    }

    @ApiOperation(value = "会员登出")
    @PostMapping(value = "/members/signout")
    public ApiEntity signout() {
        authenticator.invalidateSession();
        return new ApiEntity();
    }

    @ApiOperation(value = "获取会员首页信息")
    @GetMapping(value = "/members/home")
    public ApiEntity<GetMemberHomeResponse> getMemberHome() {
        log.info("controller:members:home:获取会员首页信息请求, path={}", "/members/home");
        GetMemberHomeResponse response = new GetMemberHomeResponse();

        Date now = new Date();
        BarablahPositionContentExample example = new BarablahPositionContentExample();
        example.createCriteria()
                .andPositionIdEqualTo(1L)
//                .andStartAtLessThan(now)
//                .andEndAtGreaterThan(now)
                .andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("position DESC");
        List<BarablahPositionContent> contents = positionContentMapper.selectByExample(example);

        for (BarablahPositionContent content : contents) {
            PositionContentDto dto = new PositionContentDto();
            dto.setId(content.getId());
            dto.setName(content.getContentName());
            dto.setImage(content.getImage());
            dto.setLink(content.getLink());
            response.getContents().add(dto);
        }

        Long memberId = authenticator.getCurrentMemberId();

        if (Objects.nonNull(memberId)) {
            log.info("controller:members:home:获取会员课程回顾, memberId={}", memberId);
            BarablahMemberLessonExample memberLessonExample = new BarablahMemberLessonExample();
            memberLessonExample.createCriteria()
                    .andMemberIdEqualTo(memberId)
                    .andTypeEqualTo(LessonType.ONLINE.name())
                    .andDeletedEqualTo(Boolean.FALSE)
                    .andEndAtLessThan(new Date())
                    .andDeletedEqualTo(Boolean.FALSE);
            memberLessonExample.setStartRow(0);
            memberLessonExample.setPageSize(2);
            memberLessonExample.setOrderByClause("end_at DESC");
            List<BarablahMemberLesson> lessons = memberLessonMapper.selectByExample(memberLessonExample);

            for (BarablahMemberLesson memberLesson : lessons) {
                BarablahClassLesson lesson = lessonMapper.selectByPrimaryKey(memberLesson.getLessonId());
                LessonDto dto = new LessonDto();
                dto.setId(lesson.getId());
                dto.setName(lesson.getLessonName());
                dto.setThumbnail(lesson.getThumbnail());
                dto.setDuration((int) ((lesson.getEndAt().getTime() - lesson.getStartAt().getTime()) / 1000 / 60));
                response.getLessons().add(dto);
            }
        }

        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "进入直播课堂")
    @GetMapping(value = "/members/lessons/current")
    public ApiEntity<CurrentLessonResponse> currentLesson() {
        if (!authenticator.isAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        Date now = new Date();
        BarablahMemberLessonExample lessonExample = new BarablahMemberLessonExample();
        lessonExample.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andStartAtLessThan(DateUtils.addMinutes(now, 5))
                .andEndAtGreaterThan(now)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahMemberLesson> lessons = memberLessonMapper.selectByExample(lessonExample);

        if (CollectionUtils.isEmpty(lessons)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "当前没有正在进行的课程");
        }

        BarablahMember member = memberMapper.selectByPrimaryKey(memberId);
        if (member.getDeleted()) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "你的账号已经被冻结,请联系管理员!");
        }
        BarablahMemberLesson currentLesson = lessons.get(0);
        BarablahClassLesson lessonInfo = lessonMapper.selectByPrimaryKey(currentLesson.getLessonId());

        BarablahClass cla = classMapper.selectByPrimaryKey(lessonInfo.getClassId());
        BarablahTeacher teacher = teacherMapper.selectByPrimaryKey(cla.getTeacherId());

        BarablahClassMemberExample memberExample = new BarablahClassMemberExample();
        memberExample.createCriteria()
                .andClassIdEqualTo(currentLesson.getClassId())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahClassMember> members = classMemberMapper.selectByExample(memberExample);

        CurrentLessonResponse response = new CurrentLessonResponse();
        response.setId(lessonInfo.getId());
        response.setName(lessonInfo.getLessonName());
        response.setStartAt(lessonInfo.getStartAt());
        response.setEndAt(lessonInfo.getEndAt());
        response.setRoom(lessonInfo.getRoom());
        response.setTeacherName(teacher.getFullName());
        response.setTeacherAccid(teacher.getAccid());
        response.setAccid(member.getAccid());
        response.setToken(member.getToken());

        for (BarablahClassMember classMember : members) {
            BarablahMember cMember = memberMapper.selectByPrimaryKey(classMember.getMemberId());
            ClassMemberDto dto = new ClassMemberDto();
            dto.setId(cMember.getId());
            dto.setNickname(cMember.getNickname());
            dto.setAvatar(cMember.getAvatar());
            dto.setAccid(cMember.getAccid());
            response.getMembers().add(dto);
        }

        return new ApiEntity(response);
    }

    @ApiOperation(value = "获取课程回顾详情")
    @GetMapping(value = "/members/lessons/{id}")
    public ApiEntity<GetLessonResponse> getLesson(@RequestParam Long id) {
        if (!authenticator.isAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        BarablahMemberLessonExample example = new BarablahMemberLessonExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andLessonIdEqualTo(id)
                .andDeletedEqualTo(Boolean.FALSE);
        BarablahClassLesson lesson = lessonMapper.selectByPrimaryKey(id);
        BarablahClass aClass = classMapper.selectByPrimaryKey(lesson.getClassId());
        BarablahTeacher teacher = teacherMapper.selectByPrimaryKey(aClass.getTeacherId());

        GetLessonResponse response = new GetLessonResponse();
        response.setId(lesson.getId());
        response.setName(lesson.getLessonName());
        response.setClassName(aClass.getClassName());
        response.setTeacherName(teacher.getFullName());
        response.setDuration((int) ((lesson.getEndAt().getTime() - lesson.getStartAt().getTime()) / 1000 / 60));
        response.setVideo(lesson.getVideo());
        return new ApiEntity<>(response);
    }



    @ApiOperation(value = "获取我的授课列表")
    @GetMapping(value = "/members/lessons")
    public ApiEntity<List<LessonDto>> getLessons(
            @ApiParam(value = "课时类型（online线上，offline线下）") @RequestParam(defaultValue = "online") String type,
            @RequestParam(required = true) Long classid,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.isAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        LessonType typeEnum = null;
        try {
            typeEnum = LessonType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return new ApiEntity<>(ApiStatus.STATUS_400.getCode(), "授课类型值不合法, type=" + type);
        }

        List<String> status = new ArrayList<>();
        status.add(BarablahClassLessonStatusEnum.待开课.getValue());
        status.add(BarablahClassLessonStatusEnum.开课中.getValue());


        BarablahClassLessonExample example = new BarablahClassLessonExample();
        example.createCriteria().
                andClassIdEqualTo(classid).andDeletedEqualTo(false)
                .andStatusIn(status).
                andTypeEqualTo(typeEnum.name());
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("start_at ASC");
        List<BarablahClassLesson> lessons = lessonMapper.selectByExample(example);

        List<LessonDto> response = Lists.newArrayList();
        Date now = new Date();
        for (BarablahClassLesson lesson : lessons) {
            LessonDto dto = new LessonDto();
            dto.setId(lesson.getId());
            dto.setName(lesson.getLessonName());
            dto.setThumbnail(lesson.getThumbnail());
            dto.setStartAt(lesson.getStartAt());
            dto.setDuration((int) ((lesson.getEndAt().getTime() - lesson.getStartAt().getTime()) / 1000 / 60));
            dto.setStatus(lesson.getStatus());
            response.add(dto);
        }
        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "获取我的授课历史列表")
    @GetMapping(value = "/members/lessons/history")
    public ApiEntity<List<LessonDto>> getLessonsHistory(
            @ApiParam(value = "课时类型（online线上，offline线下）", required = true) @RequestParam(defaultValue = "online") String type,
            @RequestParam(required = true) Long classid,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("controller:members:lessons:history:获取我的授课历史列表请求, path={}, type={}, page={}, size={}",
                "/members/lessons/history", type, page, size);
        Long memberId = authenticator.getCurrentMemberId();
        LessonType typeEnum = null;
        try {
            typeEnum = LessonType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return new ApiEntity<>(ApiStatus.STATUS_400.getCode(), "授课类型值不合法, type=" + type);
        }
        List<String> status = new ArrayList<>();
        status.add(BarablahClassLessonStatusEnum.已结束.getValue());
        status.add(BarablahClassLessonStatusEnum.已过期.getValue());

        Date now = new Date();
        BarablahClassLessonExample example = new BarablahClassLessonExample();
        example.createCriteria().
                andClassIdEqualTo(classid).
                andDeletedEqualTo(false).
                andStatusIn(status).
                andTypeEqualTo(typeEnum.name());
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("end_at DESC");
        List<BarablahClassLesson> lessons = lessonMapper.selectByExample(example);

        List<LessonDto> response = Lists.newArrayList();

        for (BarablahClassLesson lesson : lessons) {
            LessonDto dto = new LessonDto();
            dto.setId(lesson.getId());
            dto.setName(lesson.getLessonName());
            dto.setThumbnail(lesson.getThumbnail());
            dto.setStartAt(lesson.getStartAt());
            dto.setStatus(lesson.getStatus());
            dto.setDuration((int) ((lesson.getEndAt().getTime() - lesson.getStartAt().getTime()) / 1000 / 60));

            response.add(dto);
        }
        return new ApiEntity<>(response);

    }

    @ApiOperation(value = "获取我的评语列表")
    @GetMapping(value = "/members/comments")
    public ApiEntity<List<CommentDto>> getComments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.isAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        BarablahMemberCommentExample example = new BarablahMemberCommentExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("created_at DESC");
        List<BarablahMemberComment> comments = memberCommentMapper.selectByExample(example);
        List<CommentDto> response = Lists.newArrayList();

        for (BarablahMemberComment comment : comments) {
            CommentDto dto = new CommentDto();
            dto.setId(comment.getId());
            dto.setContent(comment.getContent());
            dto.setCreatedAt(comment.getCreatedAt());

            BarablahTeacher teacher = teacherMapper.selectByPrimaryKey(comment.getTeacherId());
            dto.setTeacher(teacher.getFullName());
            dto.setTeacherAvatar(teacher.getAvatar());
            response.add(dto);
        }

        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "获取我的积分日志列表")
    @GetMapping(value = "/members/points/logs")
    public ApiEntity<List<PointLogDto>> getPointLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.isAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        BarablahMemberPointLogExample example = new BarablahMemberPointLogExample();
        example.createCriteria().andMemberIdEqualTo(memberId).andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("created_at DESC");
        List<BarablahMemberPointLog> logs = memberPointLogMapper.selectByExample(example);
        List<PointLogDto> response = Lists.newArrayList();

        for (BarablahMemberPointLog pointLog : logs) {
            PointLogDto dto = new PointLogDto();
            dto.setId(pointLog.getId());
            dto.setPoints(pointLog.getPoints());
            dto.setType(pointLog.getType());
            dto.setCreatedAt(pointLog.getCreatedAt());
            dto.setObjectType(pointLog.getObjectType());


            if (pointLog.getObjectType().equals(PointLogObjectTypeEnum.班级表现.getValue())) {
                BarablahClass c = classMapper.selectByPrimaryKey(pointLog.getObjectId());
                dto.setMessage(c.getClassName()+"-综合表现积分");
            } else if (pointLog.getObjectType().equals(PointLogObjectTypeEnum.作业.getValue())) {
                BarablahMemberHomework h = memberHomeworkMapper.selectByPrimaryKey(pointLog.getObjectId());
                dto.setMessage(h.getHomeworkName()+"-作业奖励积分");
            } else {
                BarablahClassLesson l = lessonMapper.selectByPrimaryKey(pointLog.getObjectId());
                dto.setMessage(l.getLessonName()+"-");
            }
            response.add(dto);
        }

        return new ApiEntity<>(response);
    }




}
