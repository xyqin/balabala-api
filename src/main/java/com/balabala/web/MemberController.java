package com.balabala.web;

import com.balabala.auth.Authenticator;
import com.balabala.domain.*;
import com.balabala.netease.NeteaseClient;
import com.balabala.netease.request.ImUserCreateRequest;
import com.balabala.netease.request.SmsVerifyCodeRequest;
import com.balabala.netease.response.ImUserCreateResponse;
import com.balabala.netease.response.SmsVerifyCodeResponse;
import com.balabala.repository.*;
import com.balabala.repository.example.*;
import com.balabala.web.request.*;
import com.balabala.web.response.*;
import com.balabala.wechat.mp.WxMpClient;
import com.balabala.wechat.mp.request.SnsTokenRequest;
import com.balabala.wechat.mp.request.SnsUserInfoRequest;
import com.balabala.wechat.mp.response.SnsTokenResponse;
import com.balabala.wechat.mp.response.SnsUserInfoResponse;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    private BalabalaMemberMapper memberMapper;

    @Autowired
    private BalabalaMemberLessonMapper memberLessonMapper;

    @Autowired
    private BalabalaMemberPassportMapper memberPassportMapper;

    @Autowired
    private BalabalaMemberHomeworkMapper memberHomeworkMapper;

    @Autowired
    private BalabalaMemberHomeworkItemMapper memberHomeworkItemMapper;

    @Autowired
    private BalabalaMemberCommentMapper memberCommentMapper;

    @Autowired
    private BalabalaMemberPointLogMapper memberPointLogMapper;

    @Autowired
    private BalabalaClassLessonMapper lessonMapper;

    @Autowired
    private BalabalaTeacherMapper teacherMapper;

    @Autowired
    private BalabalaClassMapper classMapper;

    @Autowired
    private BalabalaClassMemberMapper classMemberMapper;

    @Autowired
    private BalabalaCampusMapper campusMapper;

    @Autowired
    private BalabalaTextbookMapper textbookMapper;

    @Autowired
    private BalabalaPositionContentMapper positionContentMapper;

    @Autowired
    private NeteaseClient neteaseClient;

    @Autowired
    @Qualifier("wechatAppClient")
    private WxMpClient wxMpClient;

    /* 会员信息及账号相关接口 */

    @ApiOperation(value = "注册会员")
    @PostMapping(value = "/members/signup")
    public ApiEntity signup(@Validated @RequestBody SignupRequest request) {
        // 检查短信验证码
        SmsVerifyCodeRequest verifyCodeRequest = new SmsVerifyCodeRequest();
        verifyCodeRequest.setMobile(request.getPhoneNumber());
        verifyCodeRequest.setCode(request.getCode());
        SmsVerifyCodeResponse verifyCodeResponse = null;

        try {
            verifyCodeResponse = neteaseClient.execute(verifyCodeRequest);
        } catch (IOException e) {
            log.error("controller:members:signup:调用网易云检查短信验证码失败", e);
            return new ApiEntity(ApiStatus.STATUS_500);
        }

        if (verifyCodeResponse.getCode() == 413) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "验证失败(短信服务)");
        } else if (!verifyCodeResponse.isSuccess()) {
            log.error("controller:members:signup:调用网易云检查短信验证码失败, code=" + verifyCodeResponse.getCode());
            return new ApiEntity(ApiStatus.STATUS_500);
        }

        BalabalaMemberPassportExample example = new BalabalaMemberPassportExample();
        example.createCriteria()
                .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                .andProviderIdEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaMemberPassport> passports = memberPassportMapper.selectByExample(example);

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
            return new ApiEntity(ApiStatus.STATUS_500);
        }

        BalabalaMember member = new BalabalaMember();
        member.setCampusId(request.getCampusId());
        member.setAccid(imUserCreateResponse.getInfo().getAccid());
        member.setToken(imUserCreateResponse.getInfo().getToken());
        memberMapper.insertSelective(member);

        BalabalaMemberPassport passport = new BalabalaMemberPassport();
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
    public ApiEntity<SigninResponse> signin(@Validated @RequestBody SigninRequest request) {
        BalabalaMemberPassportExample example = new BalabalaMemberPassportExample();
        example.createCriteria()
                .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                .andProviderIdEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaMemberPassport> passports = memberPassportMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(passports)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "手机号尚未注册");
        }

        BalabalaMemberPassport passport = passports.get(0);

        if (!Objects.equals(DigestUtils.md5Hex(request.getPassword()), passport.getPassword())) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "密码错误");
        }

        BalabalaMember member = memberMapper.selectByPrimaryKey(passports.get(0).getMemberId());

        // 往session中设置会员ID
        authenticator.newSession(member.getId());

        SigninResponse response = new SigninResponse();
        response.setNickname(member.getNickname());
        return new ApiEntity(response);
    }

    @ApiOperation(value = "微信授权登录")
    @PostMapping(value = "/members/signin/wechat")
    public ApiEntity<SigninResponse> signinByWechat(@Validated @RequestBody SigninWechatRequest body) throws Exception {
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

        BalabalaMember memberToBeSave = new BalabalaMember();
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
        BalabalaMemberPassportExample example = new BalabalaMemberPassportExample();
        example.createCriteria()
                .andProviderEqualTo(MemberPassportProvider.WECHAT.name())
                .andProviderIdEqualTo(userInfoResponse.getOpenid())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaMemberPassport> passports = memberPassportMapper.selectByExample(example);
        boolean phoneBound = false;

        if (CollectionUtils.isEmpty(passports)) { // 注册新会员
            log.info("controller:members:signin:wechat:微信会员尚未注册, openid={}", userInfoResponse.getOpenid());
            memberMapper.insertSelective(memberToBeSave);

            BalabalaMemberPassport passport = new BalabalaMemberPassport();
            passport.setMemberId(memberToBeSave.getId());
            passport.setProvider(MemberPassportProvider.WECHAT);
            passport.setProviderId(userInfoResponse.getOpenid());
            memberPassportMapper.insertSelective(passport);
        } else { // 更新会员信息
            BalabalaMemberPassport passport = passports.get(0);
            log.info("controller:members:signin:wechat:更新微信会员信息, openid={}, memberId={}",
                    userInfoResponse.getOpenid(), passport.getMemberId());
            BalabalaMember member = memberMapper.selectByPrimaryKey(passport.getMemberId());
            memberToBeSave.setId(member.getId());
            memberMapper.updateByPrimaryKeySelective(memberToBeSave);

            // 查找是否已绑定手机号
            example.clear();
            example.createCriteria()
                    .andMemberIdEqualTo(member.getId())
                    .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                    .andDeletedEqualTo(Boolean.FALSE);
            List<BalabalaMemberPassport> phonePassports = memberPassportMapper.selectByExample(example);

            if (CollectionUtils.isNotEmpty(phonePassports)) {
                phoneBound = true;
            }
        }

        // 往session中设置会员ID
        authenticator.newSession(memberToBeSave.getId());

        SigninResponse response = new SigninResponse();
        response.setNickname(memberToBeSave.getNickname());
        response.setPhoneBound(phoneBound);
        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "绑定手机号")
    @PostMapping(value = "/members/phone/bind")
    public ApiEntity bindPhone(@Validated @RequestBody BindPhoneRequest request) {
        if (!authenticator.authenticate()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();

        // 检查短信验证码
        SmsVerifyCodeRequest verifyCodeRequest = new SmsVerifyCodeRequest();
        verifyCodeRequest.setMobile(request.getPhoneNumber());
        verifyCodeRequest.setCode(request.getCode());
        SmsVerifyCodeResponse verifyCodeResponse = null;

        try {
            verifyCodeResponse = neteaseClient.execute(verifyCodeRequest);
        } catch (IOException e) {
            log.error("controller:members:phone:bind:调用网易云检查短信验证码失败", e);
            return new ApiEntity(ApiStatus.STATUS_500);
        }

        if (verifyCodeResponse.getCode() == 413) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "验证失败(短信服务)");
        } else if (!verifyCodeResponse.isSuccess()) {
            log.error("controller:members:phone:bind:调用网易云检查短信验证码失败, code=" + verifyCodeResponse.getCode());
            return new ApiEntity(ApiStatus.STATUS_500);
        }

        BalabalaMemberPassportExample example = new BalabalaMemberPassportExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaMemberPassport> passports = memberPassportMapper.selectByExample(example);

        if (CollectionUtils.isNotEmpty(passports)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "您已绑定过手机号");
        }

        example.clear();
        example.createCriteria()
                .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                .andProviderIdEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaMemberPassport> phonePassports = memberPassportMapper.selectByExample(example);

        BalabalaMember memberToBeUpdated = new BalabalaMember();
        memberToBeUpdated.setId(memberId);
        memberToBeUpdated.setCampusId(request.getCampusId());

        if (CollectionUtils.isEmpty(phonePassports)) {
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

            if (!imUserCreateResponse.isSuccess()) {
                log.error("controller:members:phone:bind:调用网易云注册IM账号失败, code=" + imUserCreateResponse.getCode());
                return new ApiEntity(ApiStatus.STATUS_500);
            }

            memberToBeUpdated.setAccid(imUserCreateResponse.getInfo().getAccid());
            memberToBeUpdated.setToken(imUserCreateResponse.getInfo().getToken());

            // 手机号未绑定直接绑定
            BalabalaMemberPassport passportToBeCreated = new BalabalaMemberPassport();
            passportToBeCreated.setMemberId(memberId);
            passportToBeCreated.setProvider(MemberPassportProvider.PHONE);
            passportToBeCreated.setProviderId(request.getPhoneNumber());
            passportToBeCreated.setPassword(DigestUtils.md5Hex(request.getPassword()));
            memberPassportMapper.insertSelective(passportToBeCreated);
        } else {
            BalabalaMemberPassport phonePassport = phonePassports.get(0);
            example.clear();
            example.createCriteria()
                    .andProviderEqualTo(MemberPassportProvider.WECHAT.name())
                    .andMemberIdEqualTo(phonePassport.getMemberId())
                    .andDeletedEqualTo(Boolean.FALSE);
            List<BalabalaMemberPassport> wechatPassports = memberPassportMapper.selectByExample(example);

            if (CollectionUtils.isNotEmpty(wechatPassports)) {
                log.info("controller:members:phone:bind:手机号已注册且已绑定微信, memberId={}, phone={}, openid={}",
                        wechatPassports.get(0).getMemberId(), phonePassport.getProviderId(), wechatPassports.get(0).getProviderId());
                return new ApiEntity(ApiStatus.STATUS_400.getCode(), "您的手机号已绑定了其它微信");
            }

            log.info("controller:members:phone:bind:手机号已注册尚未绑定微信, memberId={}, phone={}",
                    phonePassport.getMemberId(), phonePassport.getProviderId());
            BalabalaMemberPassport passportToBeUpdated = new BalabalaMemberPassport();
            passportToBeUpdated.setId(phonePassport.getId());
            passportToBeUpdated.setMemberId(memberId);
            passportToBeUpdated.setPassword(DigestUtils.md5Hex(request.getPassword()));
            memberPassportMapper.updateByPrimaryKeySelective(passportToBeUpdated);

            BalabalaMember phoneMember = memberMapper.selectByPrimaryKey(phonePassport.getMemberId());
            memberToBeUpdated.setAccid(phoneMember.getAccid());
            memberToBeUpdated.setToken(phoneMember.getToken());
            memberToBeUpdated.setPoints(phoneMember.getPoints());
        }

        // 会员信息
        memberMapper.updateByPrimaryKeySelective(memberToBeUpdated);
        log.info("controller:members:phone:bind:绑定手机成功, memberId={}, phone={}",
                memberId, request.getPhoneNumber());
        return new ApiEntity();
    }

    @ApiOperation(value = "忘记密码")
    @PostMapping(value = "/members/password/reset")
    public ApiEntity resetPassword(@Validated @RequestBody ResetPasswordRequest request) {
        // 检查短信验证码
        SmsVerifyCodeRequest verifyCodeRequest = new SmsVerifyCodeRequest();
        verifyCodeRequest.setMobile(request.getPhoneNumber());
        verifyCodeRequest.setCode(request.getCode());
        SmsVerifyCodeResponse verifyCodeResponse = null;

        try {
            verifyCodeResponse = neteaseClient.execute(verifyCodeRequest);
        } catch (IOException e) {
            log.error("controller:members:phone:bind:调用网易云检查短信验证码失败", e);
            return new ApiEntity(ApiStatus.STATUS_500);
        }

        if (verifyCodeResponse.getCode() == 413) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "验证失败(短信服务)");
        } else if (!verifyCodeResponse.isSuccess()) {
            log.error("controller:members:phone:bind:调用网易云检查短信验证码失败, code=" + verifyCodeResponse.getCode());
            return new ApiEntity(ApiStatus.STATUS_500);
        }

        return new ApiEntity();
    }

    @ApiOperation(value = "获取会员信息")
    @GetMapping(value = "/members")
    public ApiEntity<GetMemberResponse> getMember() {
        if (!authenticator.authenticate()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        BalabalaMember member = memberMapper.selectByPrimaryKey(memberId);
        BalabalaCampus campus = campusMapper.selectByPrimaryKey(member.getCampusId());
        BalabalaMemberPassportExample example = new BalabalaMemberPassportExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andProviderEqualTo(MemberPassportProvider.WECHAT.name())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaMemberPassport> passports = memberPassportMapper.selectByExample(example);

        // 获取会员信息
        GetMemberResponse response = new GetMemberResponse();
        response.setId(member.getId());
        response.setNickname(member.getNickname());
        response.setAvatar(member.getAvatar());
        response.setEnglishName(member.getEnglishName());
        response.setGender(member.getGender().name());
        response.setPoints(member.getPoints());
        response.setCampus(campus.getCampusName());
        response.setWechatBound(CollectionUtils.isNotEmpty(passports));
        return new ApiEntity(response);
    }

    @ApiOperation(value = "更新会员信息")
    @PostMapping(value = "/members/update")
    public ApiEntity updateMemberInfo(@RequestBody UpdateMemberInfoRequest request) {
        if (!authenticator.authenticate()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        BalabalaMember memberToBeUpdated = new BalabalaMember();
        memberToBeUpdated.setId(memberId);
        memberToBeUpdated.setCampusId(request.getCampusId());
        memberToBeUpdated.setNickname(request.getNickname());
        memberToBeUpdated.setAvatar(request.getAvatar());
        memberToBeUpdated.setEnglishName(request.getEnglishName());
        memberToBeUpdated.setGender(MemberGender.valueOf(request.getGender()));

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
        BalabalaPositionContentExample example = new BalabalaPositionContentExample();
        example.createCriteria()
                .andPositionIdEqualTo(1L)
                .andStartAtLessThan(now)
                .andEndAtGreaterThan(now)
                .andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("position DESC");
        List<BalabalaPositionContent> contents = positionContentMapper.selectByExample(example);

        for (BalabalaPositionContent content : contents) {
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
            BalabalaMemberLessonExample memberLessonExample = new BalabalaMemberLessonExample();
            memberLessonExample.createCriteria()
                    .andMemberIdEqualTo(memberId)
                    .andTypeEqualTo(LessonType.OFFLINE.name()).andDeletedEqualTo(Boolean.FALSE)
                    .andEndAtLessThan(new Date())
                    .andDeletedEqualTo(Boolean.FALSE);
            memberLessonExample.setStartRow(0);
            memberLessonExample.setPageSize(2);
            memberLessonExample.setOrderByClause("end_at DESC");
            List<BalabalaMemberLesson> lessons = memberLessonMapper.selectByExample(memberLessonExample);

            for (BalabalaMemberLesson memberLesson : lessons) {
                BalabalaClassLesson lesson = lessonMapper.selectByPrimaryKey(memberLesson.getLessonId());
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
        if (!authenticator.authenticate()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        Date now = new Date();
        BalabalaMemberLessonExample lessonExample = new BalabalaMemberLessonExample();
        lessonExample.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andStartAtLessThan(DateUtils.addMinutes(now, 5))
                .andEndAtGreaterThan(now)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaMemberLesson> lessons = memberLessonMapper.selectByExample(lessonExample);

        if (CollectionUtils.isEmpty(lessons)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "当前没有正在进行的课程");
        }

        BalabalaMember member = memberMapper.selectByPrimaryKey(memberId);
        BalabalaMemberLesson currentLesson = lessons.get(0);
        BalabalaClassLesson lessonInfo = lessonMapper.selectByPrimaryKey(currentLesson.getLessonId());
        BalabalaTeacher teacher = teacherMapper.selectByPrimaryKey(lessonInfo.getTeacherId());

        BalabalaClassMemberExample memberExample = new BalabalaClassMemberExample();
        memberExample.createCriteria()
                .andClassIdEqualTo(currentLesson.getClassId())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaClassMember> members = classMemberMapper.selectByExample(memberExample);

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

        for (BalabalaClassMember classMember : members) {
            BalabalaMember cMember = memberMapper.selectByPrimaryKey(classMember.getMemberId());
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
    public ApiEntity getLesson(@RequestParam Long id) {
        if (!authenticator.authenticate()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        BalabalaMemberLessonExample example = new BalabalaMemberLessonExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andLessonIdEqualTo(id)
                .andDeletedEqualTo(Boolean.FALSE);
        BalabalaClassLesson lesson = lessonMapper.selectByPrimaryKey(id);
        BalabalaClass aClass = classMapper.selectByPrimaryKey(lesson.getClassId());
        BalabalaTeacher teacher = teacherMapper.selectByPrimaryKey(lesson.getTeacherId());

        GetLessonResponse response = new GetLessonResponse();
        response.setId(lesson.getId());
        response.setName(lesson.getLessonName());
        response.setClassName(aClass.getClassName());
        response.setTeacherName(teacher.getFullName());
        response.setDuration((int) ((lesson.getEndAt().getTime() - lesson.getStartAt().getTime()) / 1000 / 60));
        response.setVideo("http://www.baidu.com");
        return new ApiEntity(response);
    }

    @ApiOperation(value = "获取我的作业列表")
    @GetMapping(value = "/members/homeworks")
    public ApiEntity<List<HomeworkDto>> getHomeworks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.authenticate()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        BalabalaMemberHomeworkExample example = new BalabalaMemberHomeworkExample();
        example.createCriteria().andMemberIdEqualTo(memberId).andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("created_at DESC");
        List<BalabalaMemberHomework> homeworks = memberHomeworkMapper.selectByExample(example);
        List<HomeworkDto> response = Lists.newArrayList();

        for (BalabalaMemberHomework homework : homeworks) {
            HomeworkDto dto = new HomeworkDto();
            dto.setId(homework.getId());
            dto.setName(homework.getHomeworkName());
            dto.setStatus(homework.getStatus().name());
            dto.setClosingAt(homework.getClosingAt());

            BalabalaTeacher teacher = teacherMapper.selectByPrimaryKey(homework.getTeacherId());
            dto.setTeacher(teacher.getFullName());
            response.add(dto);
        }

        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "获取我的作业题目列表")
    @GetMapping(value = "/members/homeworks/{id}/items")
    public ApiEntity<List<HomeworkItemDto>> getHomeworkItems(@PathVariable Long id) {
        if (!authenticator.authenticate()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        BalabalaMemberHomeworkItemExample example = new BalabalaMemberHomeworkItemExample();
        example.createCriteria().
                andMemberIdEqualTo(memberId)
                .andHomeworkIdEqualTo(id)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaMemberHomeworkItem> items = memberHomeworkItemMapper.selectByExample(example);
        List<HomeworkItemDto> response = Lists.newArrayList();

        for (BalabalaMemberHomeworkItem item : items) {
            BalabalaTextbook textbook = textbookMapper.selectByPrimaryKey(item.getTextbookId());

            HomeworkItemDto dto = new HomeworkItemDto();
            dto.setId(item.getId());
            dto.setTextbookId(textbook.getId());
            dto.setName(textbook.getTextbookName());
            dto.setType(textbook.getType().name());
            dto.setQuestion(textbook.getQuestion());
            dto.setCorrect(textbook.getCorrect());
            dto.setImage(textbook.getImage());
            response.add(dto);
        }

        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "提交作业")
    @PostMapping(value = "/members/homeworks/{id}/items")
    public ApiEntity submitHomework(
            @PathVariable Long id,
            @Validated @RequestBody SubmitHomeworkRequest request) {
        if (!authenticator.authenticate()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        for (HomeworkItemDto item : request.getItems()) {
            BalabalaMemberHomeworkItem itemToBeUpdated = new BalabalaMemberHomeworkItem();
            itemToBeUpdated.setId(item.getId());
            itemToBeUpdated.setAnswer(item.getAnswer());
            memberHomeworkItemMapper.updateByPrimaryKeySelective(itemToBeUpdated);
        }

        BalabalaMemberHomework homeworkToBeUpdated = new BalabalaMemberHomework();
        homeworkToBeUpdated.setId(id);
        homeworkToBeUpdated.setStatus(HomeworkStatus.FINISHED);
        memberHomeworkMapper.updateByPrimaryKeySelective(homeworkToBeUpdated);
        return new ApiEntity();
    }

    @ApiOperation(value = "获取我的班级信息")
    @GetMapping(value = "/members/classes")
    public ApiEntity<GetMemberClassResponse> getMemberClass() {
        if (!authenticator.authenticate()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        BalabalaClassMemberExample example = new BalabalaClassMemberExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andStatusEqualTo(ClassStatus.ONGOING.name())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaClassMember> classes = classMemberMapper.selectByExample(example);

        GetMemberClassResponse response = null;

        if (CollectionUtils.isNotEmpty(classes)) {
            BalabalaClass aClass = classMapper.selectByPrimaryKey(classes.get(0).getClassId());
            BalabalaTeacher teacher = teacherMapper.selectByPrimaryKey(aClass.getTeacherId());
            BalabalaTeacher englishTeacher = teacherMapper.selectByPrimaryKey(aClass.getEnglishTeacherId());
            response = new GetMemberClassResponse();
            response.setId(aClass.getId());
            response.setClassName(aClass.getClassName());
            response.setMonitor(aClass.getMonitor());
            response.setMonitorPhoneNumber(aClass.getMonitorPhoneNumber());
            response.setTeacher(teacher.getFullName());
            response.setEnglishTeacher(englishTeacher.getFullName());

            BalabalaClassMemberExample classMemberExample = new BalabalaClassMemberExample();
            classMemberExample.createCriteria()
                    .andClassIdEqualTo(aClass.getId())
                    .andDeletedEqualTo(Boolean.FALSE);
            List<BalabalaClassMember> classMembers = classMemberMapper.selectByExample(classMemberExample);

            for (BalabalaClassMember classMember : classMembers) {
                BalabalaMember member = memberMapper.selectByPrimaryKey(classMember.getMemberId());
                BalabalaMemberPassportExample passportExample = new BalabalaMemberPassportExample();
                passportExample.createCriteria()
                        .andMemberIdEqualTo(classMember.getMemberId())
                        .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                        .andDeletedEqualTo(Boolean.FALSE);
                List<BalabalaMemberPassport> passports = memberPassportMapper.selectByExample(passportExample);

                ClassMemberDto dto = new ClassMemberDto();
                dto.setId(member.getId());
                dto.setNickname(member.getNickname());
                dto.setAvatar(member.getAvatar());

                if (CollectionUtils.isNotEmpty(passports)) {
                    dto.setPhoneNumber(passports.get(0).getProviderId());
                }
                response.getMembers().add(dto);
            }
        }

        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "获取我的授课列表")
    @GetMapping(value = "/members/lessons")
    public ApiEntity<List<LessonDto>> getLessons(
            @ApiParam(value = "课时类型（online线上，offline线下）") @RequestParam(defaultValue = "online") String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.authenticate()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        LessonType typeEnum = null;
        try {
            typeEnum = LessonType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return new ApiEntity<>(ApiStatus.STATUS_400.getCode(), "授课类型值不合法, type=" + type);
        }

        BalabalaMemberLessonExample example = new BalabalaMemberLessonExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andTypeEqualTo(typeEnum.name()).andDeletedEqualTo(Boolean.FALSE)
                .andStartAtGreaterThan(new Date())
                .andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("start_at ASC");
        List<BalabalaMemberLesson> lessons = memberLessonMapper.selectByExample(example);
        return new ApiEntity<>(toLessonDtoList(lessons));
    }

    @ApiOperation(value = "获取我的授课历史列表")
    @GetMapping(value = "/members/lessons/history")
    public ApiEntity<List<LessonDto>> getLessonsHistory(
            @ApiParam(value = "课时类型（online线上，offline线下）", required = true) @RequestParam(defaultValue = "online") String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("controller:members:lessons:history:获取我的授课历史列表请求, path={}, type={}, page={}, size={}",
                "/members/lessons/history", type, page, size);

        if (!authenticator.authenticate()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        LessonType typeEnum = null;
        try {
            typeEnum = LessonType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return new ApiEntity<>(ApiStatus.STATUS_400.getCode(), "授课类型值不合法, type=" + type);
        }

        BalabalaMemberLessonExample example = new BalabalaMemberLessonExample();
        example.setOrderByClause("end_at DESC");
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andTypeEqualTo(typeEnum.name()).andDeletedEqualTo(Boolean.FALSE)
                .andEndAtLessThan(new Date())
                .andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        List<BalabalaMemberLesson> lessons = memberLessonMapper.selectByExample(example);
        return new ApiEntity<>(toLessonDtoList(lessons));
    }

    @ApiOperation(value = "获取我的评语列表")
    @GetMapping(value = "/members/comments")
    public ApiEntity<List<CommentDto>> getComments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.authenticate()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        BalabalaMemberCommentExample example = new BalabalaMemberCommentExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("created_at DESC");
        List<BalabalaMemberComment> comments = memberCommentMapper.selectByExample(example);
        List<CommentDto> response = Lists.newArrayList();

        for (BalabalaMemberComment comment : comments) {
            CommentDto dto = new CommentDto();
            dto.setId(comment.getId());
            dto.setContent(comment.getContent());
            dto.setCreatedAt(comment.getCreatedAt());

            BalabalaTeacher teacher = teacherMapper.selectByPrimaryKey(comment.getTeacherId());
            dto.setTeacher(teacher.getFullName());
            response.add(dto);
        }

        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "获取我的积分日志列表")
    @GetMapping(value = "/members/points/logs")
    public ApiEntity<List<PointLogDto>> getPointLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.authenticate()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        BalabalaMemberPointLogExample example = new BalabalaMemberPointLogExample();
        example.createCriteria().andMemberIdEqualTo(memberId).andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("created_at DESC");
        List<BalabalaMemberPointLog> logs = memberPointLogMapper.selectByExample(example);
        List<PointLogDto> response = Lists.newArrayList();

        for (BalabalaMemberPointLog pointLog : logs) {
            PointLogDto dto = new PointLogDto();
            dto.setId(pointLog.getId());
            dto.setPoints(pointLog.getPoints());
            dto.setType(pointLog.getType().name());
            dto.setCreatedAt(pointLog.getCreatedAt());
            response.add(dto);
        }

        return new ApiEntity<>(response);
    }

    private List<LessonDto> toLessonDtoList(List<BalabalaMemberLesson> memberLessons) {
        List<LessonDto> response = Lists.newArrayList();
        Date now = new Date();

        for (BalabalaMemberLesson memberLesson : memberLessons) {
            BalabalaClassLesson lesson = lessonMapper.selectByPrimaryKey(memberLesson.getLessonId());
            LessonDto dto = new LessonDto();
            dto.setId(lesson.getId());
            dto.setName(lesson.getLessonName());
            dto.setThumbnail(lesson.getThumbnail());
            dto.setStartAt(lesson.getStartAt());
            dto.setDuration((int) ((lesson.getEndAt().getTime() - lesson.getStartAt().getTime()) / 1000 / 60));

            if (DateUtils.isSameDay(new Date(), lesson.getStartAt())) {
                dto.setStatus("SOON");
            } else if (now.after(lesson.getEndAt())) {
                dto.setStatus("FINISHED");
            } else {
                dto.setStatus("PENDING");
            }

            response.add(dto);
        }

        return response;
    }

}
