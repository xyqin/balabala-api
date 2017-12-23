package com.balabala.web;

import com.balabala.auth.Authenticator;
import com.balabala.domain.*;
import com.balabala.netease.NeteaseClient;
import com.balabala.netease.request.ImUserCreateRequest;
import com.balabala.netease.response.ImUserCreateResponse;
import com.balabala.repository.*;
import com.balabala.repository.example.BalabalaMemberLessonExample;
import com.balabala.repository.example.BalabalaMemberPassportExample;
import com.balabala.web.exception.BadRequestException;
import com.balabala.web.exception.UnauthorizedException;
import com.balabala.web.request.SigninRequest;
import com.balabala.web.request.SignupRequest;
import com.balabala.web.request.UpdateMemberInfoRequest;
import com.balabala.web.response.CurrentLessonResponse;
import com.balabala.web.response.GetMemberResponse;
import com.balabala.web.response.LessonDto;
import com.balabala.web.response.SigninResponse;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
    private BalabalaClassLessonMapper lessonMapper;

    @Autowired
    private BalabalaTeacherMapper teacherMapper;

    @Autowired
    private NeteaseClient neteaseClient;

    /* 会员信息及账号相关接口 */

    @ApiOperation(value = "注册会员")
    @PostMapping(value = "/members/signup")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void signup(@Validated @RequestBody SignupRequest request) throws IOException {
        // TODO 检查手机号验证码

        BalabalaMemberPassportExample example = new BalabalaMemberPassportExample();
        example.createCriteria()
                .andProviderEqualTo(BalabalaMemberPassportProvider.PHONE.name())
                .andProviderIdEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaMemberPassport> passports = memberPassportMapper.selectByExample(example);

        if (CollectionUtils.isNotEmpty(passports)) {
            throw new BadRequestException("手机号已被注册");
        }

        // 注册网易云IM账号
        ImUserCreateRequest imUserCreateRequest = new ImUserCreateRequest();
        imUserCreateRequest.setAccid("member_" + request.getPhoneNumber());
        ImUserCreateResponse imUserCreateResponse = neteaseClient.execute(imUserCreateRequest);

        if (imUserCreateResponse.isSuccess()) {
            BalabalaMember member = new BalabalaMember();
            member.setCampusId(request.getCampusId());
            member.setAccid(imUserCreateResponse.getInfo().getAccid());
            member.setToken(imUserCreateResponse.getInfo().getToken());
            memberMapper.insertSelective(member);

            BalabalaMemberPassport passport = new BalabalaMemberPassport();
            passport.setMemberId(member.getId());
            passport.setProvider(BalabalaMemberPassportProvider.PHONE);
            passport.setProviderId(request.getPhoneNumber());
            passport.setPassword(DigestUtils.md5Hex(request.getPassword()));
            memberPassportMapper.insertSelective(passport);

            // 往session中设置会员ID
            authenticator.newSession(member.getId());
        }
    }

    @ApiOperation(value = "手机号登录")
    @PostMapping(value = "/members/signin")
    public SigninResponse signin(@Validated @RequestBody SigninRequest request) {
        BalabalaMemberPassportExample example = new BalabalaMemberPassportExample();
        example.createCriteria()
                .andProviderEqualTo(BalabalaMemberPassportProvider.PHONE.name())
                .andProviderIdEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaMemberPassport> passports = memberPassportMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(passports)) {
            throw new BadRequestException("手机号尚未注册");
        }

        BalabalaMember member = memberMapper.selectByPrimaryKey(passports.get(0).getMemberId());

        // 往session中设置会员ID
        authenticator.newSession(member.getId());

        SigninResponse response = new SigninResponse();
        response.setNickname(member.getNickname());
        return response;
    }

    @ApiOperation(value = "获取会员信息")
    @GetMapping(value = "/members")
    public GetMemberResponse getMember() {
        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long memberId = authenticator.getCurrentMemberId();


        GetMemberResponse response = new GetMemberResponse();


        return response;
    }

    @ApiOperation(value = "更新会员信息")
    @PostMapping(value = "/members/update")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateMemberInfo(@RequestBody UpdateMemberInfoRequest request) {
        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long memberId = authenticator.getCurrentMemberId();


        memberMapper.updateByPrimaryKeySelective(null);
    }

    @ApiOperation(value = "会员登出")
    @PostMapping(value = "/members/signout")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void signout() {
        authenticator.invalidateSession();
    }

    @ApiOperation(value = "进入直播课堂")
    @GetMapping(value = "/members/lessons/current")
    public CurrentLessonResponse currentLesson() {
        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long memberId = authenticator.getCurrentMemberId();

        Date now = new Date();
        BalabalaMemberLessonExample lessonExample = new BalabalaMemberLessonExample();
        lessonExample.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andStartAtLessThan(now)
                .andEndAtGreaterThan(now)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaMemberLesson> lessons = memberLessonMapper.selectByExample(lessonExample);

        if (CollectionUtils.isEmpty(lessons)) {
            throw new BadRequestException("当前没有正在进行的课程");
        }

        BalabalaMember member = memberMapper.selectByPrimaryKey(memberId);
        BalabalaMemberLesson currentLesson = lessons.get(0);
        BalabalaClassLesson lessonInfo = lessonMapper.selectByPrimaryKey(currentLesson.getLessonId());
        BalabalaTeacher teacher = teacherMapper.selectByPrimaryKey(lessonInfo.getTeacherId());

        CurrentLessonResponse response = new CurrentLessonResponse();
        response.setId(lessonInfo.getId());
        response.setName(lessonInfo.getLessonName());
        response.setStartAt(lessonInfo.getStartAt());
        response.setEndAt(lessonInfo.getEndAt());
        response.setRoom(lessonInfo.getRoom());
        response.setTeacherName(teacher.getFullName());
        response.setAccid(member.getAccid());
        response.setToken(member.getToken());
        return response;
    }

    @ApiOperation(value = "获取课程回顾")
    @GetMapping(value = "/members/lessons/history")
    public List<LessonDto> getLessonHistory(@RequestParam int page, @RequestParam int size) {
        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long memberId = authenticator.getCurrentMemberId();

        Date now = new Date();
        BalabalaMemberLessonExample example = new BalabalaMemberLessonExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andEndAtLessThan(now)
                .andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("end_at DESC");
        List<BalabalaMemberLesson> memberLessons = memberLessonMapper.selectByExample(example);
        List<LessonDto> response = Lists.newArrayList();

        for (BalabalaMemberLesson memberLesson : memberLessons) {
            BalabalaClassLesson lesson = lessonMapper.selectByPrimaryKey(memberLesson.getLessonId());
            LessonDto dto = new LessonDto();
            dto.setId(lesson.getId());
            dto.setName(lesson.getLessonName());
            dto.setDuration(0);
            dto.setThumbnail(lesson.getThumbnail());
            response.add(dto);
        }

        return response;
    }

}
