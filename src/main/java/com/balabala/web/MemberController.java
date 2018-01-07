package com.balabala.web;

import com.balabala.auth.Authenticator;
import com.balabala.domain.*;
import com.balabala.netease.NeteaseClient;
import com.balabala.netease.request.ImUserCreateRequest;
import com.balabala.netease.response.ImUserCreateResponse;
import com.balabala.repository.*;
import com.balabala.repository.example.BalabalaClassMemberExample;
import com.balabala.repository.example.BalabalaMemberLessonExample;
import com.balabala.repository.example.BalabalaMemberPassportExample;
import com.balabala.web.request.*;
import com.balabala.web.response.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    private BalabalaClassLessonMapper lessonMapper;

    @Autowired
    private BalabalaTeacherMapper teacherMapper;

    @Autowired
    private BalabalaClassMemberMapper classMemberMapper;

    @Autowired
    private BalabalaCampusMapper campusMapper;

    @Autowired
    private NeteaseClient neteaseClient;

    /* 会员信息及账号相关接口 */

    @ApiOperation(value = "注册会员")
    @PostMapping(value = "/members/signup")
    public ApiEntity signup(@Validated @RequestBody SignupRequest request) throws IOException {
        // TODO 检查手机号验证码

        BalabalaMemberPassportExample example = new BalabalaMemberPassportExample();
        example.createCriteria()
                .andProviderEqualTo(BalabalaMemberPassportProvider.PHONE.name())
                .andProviderIdEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaMemberPassport> passports = memberPassportMapper.selectByExample(example);

        if (CollectionUtils.isNotEmpty(passports)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "手机号已被注册");
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

        return new ApiEntity();
    }

    @ApiOperation(value = "手机号登录")
    @PostMapping(value = "/members/signin")
    public ApiEntity<SigninResponse> signin(@Validated @RequestBody SigninRequest request) {
        BalabalaMemberPassportExample example = new BalabalaMemberPassportExample();
        example.createCriteria()
                .andProviderEqualTo(BalabalaMemberPassportProvider.PHONE.name())
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

    @ApiOperation(value = "忘记密码")
    @PostMapping(value = "/members/password/reset")
    public ApiEntity resetPassword(@RequestBody ResetPasswordRequest request) {
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
                .andProviderEqualTo(BalabalaMemberPassportProvider.WECHAT.name())
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
        memberToBeUpdated.setGender(BalabalaMemberGender.valueOf(request.getGender()));

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

    @ApiOperation(value = "获取课程回顾")
    @GetMapping(value = "/members/lessons/history")
    public ApiEntity<List<LessonDto>> getLessonHistory(
            @RequestParam int page,
            @RequestParam int size) {
        if (!authenticator.authenticate()) {
            return new ApiEntity(ApiStatus.STATUS_401);
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

        return new ApiEntity(response);
    }

    @ApiOperation(value = "获取我的作业列表")
    @GetMapping(value = "/members/homeworks")
    public ApiEntity<List<HomeworkDto>> getHomeworks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ApiEntity<>();
    }

    @ApiOperation(value = "获取我的作业题目列表")
    @GetMapping(value = "/members/homeworks/{id}/items")
    public ApiEntity<List<HomeworkItemDto>> getHomeworkItems() {
        return new ApiEntity<>();
    }

    @ApiOperation(value = "提交作业")
    @PostMapping(value = "/members/homeworks/{id}/items")
    public ApiEntity submitHomework(@RequestBody SubmitHomeworkRequest request) {
        return new ApiEntity();
    }

    @ApiOperation(value = "获取我的班级信息")
    @GetMapping(value = "/members/classes")
    public ApiEntity<GetMemberClassResponse> getMemberClass() {
        return new ApiEntity<>();
    }

    @ApiOperation(value = "获取我的授课列表")
    @GetMapping(value = "/members/lessons")
    public ApiEntity<List<LessonDto>> getLessons(
            @ApiParam(value = "课时类型（online线上，offline线下）") @RequestParam(defaultValue = "online") String type) {
        if ("offline".equals(type)) {

        } else {

        }

        return new ApiEntity<>();
    }

    @ApiOperation(value = "获取我的评语列表")
    @GetMapping(value = "/members/lessons")
    public ApiEntity<List<CommentDto>> getComments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ApiEntity<>();
    }

    @ApiOperation(value = "获取我的积分日志列表")
    @GetMapping(value = "/members/points/logs")
    public ApiEntity<List<PointLogDto>> getPointLogs(
            @RequestParam int page,
            @RequestParam int size) {
        return new ApiEntity<>();
    }

}
