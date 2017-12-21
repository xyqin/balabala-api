package com.balabala.web;

import com.balabala.auth.Authenticator;
import com.balabala.domain.*;
import com.balabala.netease.NeteaseClient;
import com.balabala.netease.request.ImUserCreateRequest;
import com.balabala.netease.response.ImUserCreateResponse;
import com.balabala.repository.*;
import com.balabala.repository.example.BalabalaClassExample;
import com.balabala.repository.example.BalabalaClassLessonExample;
import com.balabala.repository.example.BalabalaClassMemberExample;
import com.balabala.repository.example.BalabalaTeacherExample;
import com.balabala.web.exception.BadRequestException;
import com.balabala.web.exception.UnauthorizedException;
import com.balabala.web.request.ApplyClassRequest;
import com.balabala.web.request.SigninTeacherRequest;
import com.balabala.web.request.SignupTeacherRequest;
import com.balabala.web.response.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Api(tags = "教师", description = "教师账号相关接口")
@RestController
public class TeacherController {

    @Autowired
    private Authenticator authenticator;

    @Autowired
    private BalabalaCampusMapper campusMapper;

    @Autowired
    private BalabalaTeacherMapper teacherMapper;

    @Autowired
    private BalabalaMemberMapper memberMapper;

    @Autowired
    private BalabalaClassMapper classMapper;

    @Autowired
    private BalabalaClassMemberMapper classMemberMapper;

    @Autowired
    private BalabalaClassLessonMapper lessonMapper;

    @Autowired
    private NeteaseClient neteaseClient;

    @ApiOperation(value = "教师申请")
    @PostMapping(value = "/teachers/signup")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void signup(@Validated @RequestBody SignupTeacherRequest request) throws IOException {
        BalabalaTeacherExample example = new BalabalaTeacherExample();
        example.createCriteria()
                .andPhoneNumberEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        long count = teacherMapper.countByExample(example);

        if (count > 0) {
            throw new BadRequestException("手机号已被注册");
        }

        // 注册网易云IM账号
        ImUserCreateRequest imUserCreateRequest = new ImUserCreateRequest();
        imUserCreateRequest.setAccid("teacher_" + request.getPhoneNumber());
        ImUserCreateResponse imUserCreateResponse = neteaseClient.execute(imUserCreateRequest);

        if (imUserCreateResponse.isSuccess()) {
            BalabalaTeacher teacher = new BalabalaTeacher();
            teacher.setCampusId(request.getCampusId());
            teacher.setFullName(request.getFullName());
            teacher.setPhoneNumber(request.getPhoneNumber());
            teacher.setMajor(request.getMajor());
            teacher.setComeFrom(request.getFrom());
            teacher.setStatus(BalabalaTeacherStatus.IN_REVIEW);
            teacher.setAccid(imUserCreateResponse.getInfo().getAccid());
            teacher.setToken(imUserCreateResponse.getInfo().getToken());
            teacherMapper.insertSelective(teacher);
        }
    }

    @ApiOperation(value = "手机号登录")
    @PostMapping(value = "/teachers/signin")
    public SigninTeacherResponse signin(@Validated @RequestBody SigninTeacherRequest request) {
        BalabalaTeacherExample example = new BalabalaTeacherExample();
        example.or().andPhoneNumberEqualTo(request.getLoginName()).andDeletedEqualTo(Boolean.FALSE);
        example.or().andUsernameEqualTo(request.getLoginName()).andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaTeacher> teachers = teacherMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(teachers)) {
            throw new BadRequestException("手机号或账号不存在");
        }

        BalabalaTeacher teacher = teachers.get(0);
        if (!Objects.equals(teacher.getStatus(), BalabalaTeacherStatus.ENABLED)) {
            throw new BadRequestException("账号状态异常, status=" + teacher.getStatus().name());
        }

        if (!Objects.equals(DigestUtils.md5Hex(request.getPassword()), teacher.getPassword())) {
            throw new BadRequestException("密码错误");
        }

        authenticator.newSessionForTeacher(teacher.getId());
        BalabalaCampus campus = campusMapper.selectByPrimaryKey(teacher.getCampusId());

        SigninTeacherResponse response = new SigninTeacherResponse();
        response.setUsername("12345678");
        response.setFullName(teacher.getFullName());
        response.setAvatar(teacher.getAvatar());
        response.setCampusName(campus.getCampusName());
        return response;
    }

    @ApiOperation(value = "开始授课")
    @GetMapping(value = "/teachers/lessons/current")
    public NewLessonResponse newLesson() {
        if (!authenticator.authenticateForTeacher()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        Date now = new Date();
        BalabalaClassLessonExample lessonExample = new BalabalaClassLessonExample();
        lessonExample.createCriteria()
                .andTeacherIdEqualTo(teacherId)
                .andStartAtLessThan(now)
                .andEndAtGreaterThan(now)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaClassLesson> lessons = lessonMapper.selectByExample(lessonExample);

        if (CollectionUtils.isEmpty(lessons)) {
            throw new BadRequestException("当前没有正在进行的课程");
        }

        BalabalaClassLesson currentLesson = lessons.get(0);
        BalabalaTeacher teacher = teacherMapper.selectByPrimaryKey(teacherId);

        BalabalaClassMemberExample memberExample = new BalabalaClassMemberExample();
        memberExample.createCriteria()
                .andClassIdEqualTo(currentLesson.getClassId())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaClassMember> members = classMemberMapper.selectByExample(memberExample);

        NewLessonResponse response = new NewLessonResponse();
        response.setId(currentLesson.getId());
        response.setName(currentLesson.getLessonName());
        response.setStartAt(currentLesson.getStartAt());
        response.setEndAt(currentLesson.getEndAt());
        response.setTeacherName(teacher.getFullName());
        response.setTeacherUsername(teacher.getUsername());
        response.setAccid(teacher.getAccid());
        response.setToken(teacher.getToken());

        for (BalabalaClassMember classMember : members) {
            BalabalaMember member = memberMapper.selectByPrimaryKey(classMember.getMemberId());
            ClassMemberDto dto = new ClassMemberDto();
            dto.setId(member.getId());
            dto.setNickname(member.getNickname());
            dto.setAvatar(member.getAvatar());
            response.getMembers().add(dto);
        }

        if (StringUtils.isBlank(currentLesson.getRoom())) {
            String room = currentLesson.getLessonName() + "_" + teacher.getFullName() + "_"
                    + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
            currentLesson.setRoom(room);

            BalabalaClassLesson lessonToBeUpdated = new BalabalaClassLesson();
            lessonToBeUpdated.setId(currentLesson.getId());
            lessonToBeUpdated.setRoom(room);
            lessonMapper.updateByPrimaryKeySelective(lessonToBeUpdated);
        }

        response.setRoom(currentLesson.getRoom());
        return response;
    }

    @ApiOperation(value = "获取备课列表")
    @GetMapping(value = "/teachers/lessons")
    public List<LessonDto> getLessons() {
        if (!authenticator.authenticateForTeacher()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        BalabalaClassLessonExample example = new BalabalaClassLessonExample();
        example.createCriteria()
                .andTeacherIdEqualTo(teacherId)
                .andStartAtGreaterThan(new Date())
                .andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("start_at");
        List<BalabalaClassLesson> lessons = lessonMapper.selectByExample(example);
        List<LessonDto> response = Lists.newArrayList();

        for (BalabalaClassLesson lesson : lessons) {
            LessonDto dto = new LessonDto();
            dto.setId(lesson.getId());
            dto.setName(lesson.getLessonName());
            response.add(dto);
        }

        return response;
    }

    @ApiOperation(value = "获取授课历史")
    @GetMapping(value = "/teachers/lessons/history")
    public List<LessonDto> getLessonHistory() {
        if (!authenticator.authenticateForTeacher()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        Date now = new Date();
        BalabalaClassLessonExample example = new BalabalaClassLessonExample();
        example.or().andTeacherIdEqualTo(teacherId).andEndAtLessThan(now).andDeletedEqualTo(Boolean.FALSE);
        example.or().andTeacherIdEqualTo(teacherId).andStartAtLessThan(now).andEndAtGreaterThan(now).andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("end_at DESC");
        List<BalabalaClassLesson> lessons = lessonMapper.selectByExample(example);
        List<LessonDto> response = Lists.newArrayList();

        for (BalabalaClassLesson lesson : lessons) {
            LessonDto dto = new LessonDto();
            dto.setId(lesson.getId());
            dto.setName(lesson.getLessonName());
            response.add(dto);
        }

        return response;
    }

    @ApiOperation(value = "获取班级列表")
    @GetMapping(value = "/teachers/classes")
    public List<ClassDto> getClasses() {
        BalabalaClassExample example = new BalabalaClassExample();
        example.createCriteria()
                .andStatusEqualTo(BalabalaClassStatus.ONGOING.name())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaClass> classes = classMapper.selectByExample(example);
        List<ClassDto> response = Lists.newArrayList();

        for (BalabalaClass cls : classes) {
            ClassDto dto = new ClassDto();
            dto.setId(cls.getId());
            dto.setName(cls.getClassName());
            response.add(dto);
        }
        return response;
    }

    @ApiOperation(value = "申请开班")
    @PostMapping(value = "/teachers/classes")
    public void getClasses(@RequestBody ApplyClassRequest request) {
        if (!authenticator.authenticateForTeacher()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long teacherId = authenticator.getCurrentTeacherId();
        BalabalaClass aClass = new BalabalaClass();
        aClass.setCourseId(request.getCourseId());
        aClass.setClassName(request.getClassName());
        aClass.setTeacherId(teacherId);
        aClass.setStatus(BalabalaClassStatus.IN_REVIEW);
        classMapper.insertSelective(aClass);
    }

}
