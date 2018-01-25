package com.barablah.web;

import com.barablah.auth.Authenticator;
import com.barablah.domain.*;
import com.barablah.netease.NeteaseClient;
import com.barablah.netease.request.ImUserCreateRequest;
import com.barablah.netease.response.ImUserCreateResponse;
import com.barablah.repository.*;
import com.barablah.repository.example.*;
import com.barablah.web.request.*;
import com.barablah.web.response.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Api(tags = "教师", description = "教师账号相关接口")
@RestController
public class TeacherController {

    @Autowired
    private Authenticator authenticator;

    @Autowired
    private BarablahCampusMapper campusMapper;

    @Autowired
    private BarablahTeacherMapper teacherMapper;

    @Autowired
    private BarablahTeacherHomeworkMapper teacherHomeworkMapper;

    @Autowired
    private BarablahTeacherHomeworkItemMapper teacherHomeworkItemMapper;

    @Autowired
    private BarablahMemberMapper memberMapper;

    @Autowired
    private BarablahMemberPassportMapper passportMapper;

    @Autowired
    private BarablahMemberLessonMapper memberLessonMapper;

    @Autowired
    private BarablahClassMapper classMapper;

    @Autowired
    private BarablahClassMemberMapper classMemberMapper;

    @Autowired
    private BarablahClassLessonMapper lessonMapper;

    @Autowired
    private BarablahMemberCommentMapper commentMapper;

    @Autowired
    private BarablahMemberHomeworkMapper memberHomeworkMapper;

    @Autowired
    private BarablahMemberHomeworkItemMapper memberHomeworkItemMapper;

    @Autowired
    private BarablahCourseMapper courseMapper;

    @Autowired
    private BarablahTextbookMapper textbookMapper;

    @Autowired
    private BarablahMemberPointLogMapper pointLogMapper;

    @Autowired
    private NeteaseClient neteaseClient;

    @ApiOperation(value = "教师申请")
    @PostMapping(value = "/teachers/signup")
    public ApiEntity signup(@Validated @RequestBody SignupTeacherRequest request) {
        BarablahTeacherExample example = new BarablahTeacherExample();
        example.createCriteria()
                .andPhoneNumberEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        long count = teacherMapper.countByExample(example);

        if (count > 0) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "手机号已被注册");
        }

        // 注册网易云IM账号
        ImUserCreateRequest imUserCreateRequest = new ImUserCreateRequest();
        imUserCreateRequest.setAccid("teacher_" + request.getPhoneNumber());
        ImUserCreateResponse imUserCreateResponse = null;
        try {
            imUserCreateResponse = neteaseClient.execute(imUserCreateRequest);
        } catch (IOException e) {
            log.error("controller:teachers:signup:调用网易云注册IM账号失败", e);
            return new ApiEntity(ApiStatus.STATUS_500);
        }

        if (!imUserCreateResponse.isSuccess()) {
            log.error("controller:teachers:signup:调用网易云注册IM账号失败, code=" + imUserCreateResponse.getCode());
            return new ApiEntity(ApiStatus.STATUS_500);
        }

        BarablahTeacher teacher = new BarablahTeacher();
        teacher.setCampusId(request.getCampusId());
        teacher.setFullName(request.getFullName());
        teacher.setPhoneNumber(request.getPhoneNumber());
        teacher.setMajor(request.getMajor());
        teacher.setComeFrom(request.getFrom());
        teacher.setStatus(TeacherStatus.IN_REVIEW);
        teacher.setAccid(imUserCreateResponse.getInfo().getAccid());
        teacher.setToken(imUserCreateResponse.getInfo().getToken());
        teacherMapper.insertSelective(teacher);
        return new ApiEntity();
    }

    @ApiOperation(value = "手机号登录")
    @PostMapping(value = "/teachers/signin")
    public ApiEntity<SigninTeacherResponse> signin(@Validated @RequestBody SigninTeacherRequest request) {
        BarablahTeacherExample example = new BarablahTeacherExample();
        example.or().andPhoneNumberEqualTo(request.getLoginName()).andDeletedEqualTo(Boolean.FALSE);
        example.or().andUsernameEqualTo(request.getLoginName()).andDeletedEqualTo(Boolean.FALSE);
        List<BarablahTeacher> teachers = teacherMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(teachers)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "手机号或账号不存在");
        }

        BarablahTeacher teacher = teachers.get(0);
        if (!Objects.equals(teacher.getStatus(), TeacherStatus.ENABLED)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "账号状态异常, status=" + teacher.getStatus().name());
        }

        if (!Objects.equals(DigestUtils.md5Hex(request.getPassword()), teacher.getPassword())) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "密码错误");
        }

        authenticator.newSessionForTeacher(teacher.getId());
        BarablahCampus campus = campusMapper.selectByPrimaryKey(teacher.getCampusId());

        SigninTeacherResponse response = new SigninTeacherResponse();
        response.setUsername("12345678");
        response.setFullName(teacher.getFullName());
        response.setAvatar(teacher.getAvatar());
        response.setCampusName(campus.getCampusName());
        return new ApiEntity(response);
    }

    @ApiOperation(value = "获取教师信息")
    @GetMapping(value = "/teachers")
    public ApiEntity<GetTeacherResponse> getTeacherInfo() {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        BarablahTeacher teacher = teacherMapper.selectByPrimaryKey(teacherId);
        BarablahCampus campus = campusMapper.selectByPrimaryKey(teacher.getCampusId());

        GetTeacherResponse response = new GetTeacherResponse();
        response.setCampus(campus.getCampusName());
        response.setId(teacher.getId());
        response.setUsername(teacher.getUsername());
        response.setFullName(teacher.getFullName());
        response.setPhoneNumber(teacher.getPhoneNumber());
        response.setAvatar(teacher.getAvatar());
        response.setMajor(teacher.getMajor());
        response.setComeFrom(teacher.getComeFrom());
        return new ApiEntity(response);
    }

    @ApiOperation(value = "更新教师信息")
    @PostMapping(value = "/teachers/update")
    public ApiEntity updateTeacherInfo(@RequestBody UpdateTeacherInfoRequest request) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        BarablahTeacher teacherToBeUpdated = new BarablahTeacher();
        teacherToBeUpdated.setId(teacherId);
        teacherToBeUpdated.setAvatar(request.getAvatar());
        teacherToBeUpdated.setFullName(request.getFullName());
        teacherToBeUpdated.setMajor(request.getMajor());
        teacherToBeUpdated.setComeFrom(request.getComeFrom());
        teacherMapper.updateByPrimaryKeySelective(teacherToBeUpdated);

        return new ApiEntity();
    }

    @ApiOperation(value = "获取备课列表")
    @GetMapping(value = "/teachers/lessons")
    public ApiEntity<List<ClassDto>> getLessons() {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        BarablahClassExample example = new BarablahClassExample();
        example.createCriteria()
                .andTeacherIdEqualTo(teacherId)
                .andStatusEqualTo(ClassStatus.ONGOING.name())
                .andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("created_at DESC");
        List<BarablahClass> classes = classMapper.selectByExample(example);
        List<ClassDto> response = Lists.newArrayList();

        for (BarablahClass aClass : classes) {
            BarablahCourse course = courseMapper.selectByPrimaryKey(aClass.getCourseId());

            ClassDto dto = new ClassDto();
            dto.setId(aClass.getId());
            dto.setName(aClass.getClassName());
            dto.setCourseName(course.getCourseName());

            BarablahClassLessonExample lessonExample = new BarablahClassLessonExample();
            lessonExample.createCriteria()
                    .andClassIdEqualTo(aClass.getId())
                    .andTypeEqualTo(LessonType.ONLINE.name())
                    .andDeletedEqualTo(Boolean.FALSE);
            lessonExample.setOrderByClause("start_at");
            List<BarablahClassLesson> lessons = lessonMapper.selectByExample(lessonExample);

            for (BarablahClassLesson lesson : lessons) {
                LessonDto lDto = new LessonDto();
                lDto.setId(lesson.getId());
                lDto.setName(lesson.getLessonName());
                lDto.setPrepared(lesson.getPrepared());
                dto.getLessons().add(lDto);
            }

            response.add(dto);
        }

        return new ApiEntity(response);
    }

    @ApiOperation(value = "获取课时的题目列表")
    @GetMapping(value = "/teachers/lessons/{id}/textbooks")
    public ApiEntity<GetTextbooksResponse> getLessonTextbooks(@PathVariable Long id) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();
        BarablahClassLessonExample example = new BarablahClassLessonExample();
        example.createCriteria()
                .andIdEqualTo(id)
                .andTeacherIdEqualTo(teacherId)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahClassLesson> lessons = lessonMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(lessons)) {
            return new ApiEntity(ApiStatus.STATUS_401.getCode(), "找不到课程或您无权访问");
        }

        BarablahClassLesson lesson = lessons.get(0);
        BarablahTextbookExample textbookExample = new BarablahTextbookExample();
        textbookExample.createCriteria()
                .andCategoryIdEqualTo(lesson.getCategoryId())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahTextbook> textbooks = textbookMapper.selectByExample(textbookExample);
        GetTextbooksResponse response = new GetTextbooksResponse();

        for (BarablahTextbook textbook : textbooks) {
            TextbookDto dto = new TextbookDto();
            dto.setId(textbook.getId());
            dto.setName(textbook.getTextbookName());
            dto.setQuestion(textbook.getQuestion());
            dto.setOption(textbook.getOption());
            dto.setCorrect(textbook.getCorrect());
            dto.setImage(textbook.getImage());
            dto.setVideo(textbook.getVideo());

            if (TextbookType.CHOICE.equals(textbook.getType())) {
                response.getChoices().add(dto);
            } else if (TextbookType.FILLIN.equals(textbook.getType())) {
                response.getFillins().add(dto);
            } else if (TextbookType.LISTEN.equals(textbook.getType())) {
                response.getListens().add(dto);
            } else if (TextbookType.SENTENCE.equals(textbook.getType())) {
                response.getSentences().add(dto);
            } else if (TextbookType.CONNECT.equals(textbook.getType())) {
                response.getConnects().add(dto);
            } else if (TextbookType.WORD.equals(textbook.getType())) {
                response.getWords().add(dto);
            } else if (TextbookType.PICTURE.equals(textbook.getType())) {
                response.getPictures().add(dto);
            } else if (TextbookType.ARTICLE.equals(textbook.getType())) {
                response.getArticle().add(dto);
            }
        }

        return new ApiEntity(response);
    }

    @ApiOperation(value = "标记已备课")
    @GetMapping(value = "/teachers/lessons/{id}/prepared")
    public ApiEntity prepare(@PathVariable Long id) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();
        BarablahClassLessonExample example = new BarablahClassLessonExample();
        example.createCriteria()
                .andIdEqualTo(id)
                .andTeacherIdEqualTo(teacherId)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahClassLesson> lessons = lessonMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(lessons)) {
            return new ApiEntity(ApiStatus.STATUS_401.getCode(), "找不到课程或您无权访问");
        }

        BarablahClassLesson lessonToBeUpdated = new BarablahClassLesson();
        lessonToBeUpdated.setId(lessons.get(0).getId());
        lessonToBeUpdated.setPrepared(Boolean.TRUE);
        lessonMapper.updateByPrimaryKeySelective(lessonToBeUpdated);
        return new ApiEntity();
    }

    @ApiOperation(value = "开始授课")
    @GetMapping(value = "/teachers/lessons/current")
    public ApiEntity<NewLessonResponse> newLesson() {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        Date now = new Date();
        BarablahClassLessonExample lessonExample = new BarablahClassLessonExample();
        lessonExample.createCriteria()
                .andTeacherIdEqualTo(teacherId)
                .andStartAtLessThan(DateUtils.addMinutes(now, 5))
                .andEndAtGreaterThan(now)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahClassLesson> lessons = lessonMapper.selectByExample(lessonExample);

        if (CollectionUtils.isEmpty(lessons)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "当前没有正在进行的课程");
        }

        BarablahClassLesson currentLesson = lessons.get(0);
        BarablahTeacher teacher = teacherMapper.selectByPrimaryKey(teacherId);

        BarablahMemberLessonExample memberLessonExample = new BarablahMemberLessonExample();
        memberLessonExample.createCriteria()
                .andLessonIdEqualTo(currentLesson.getId())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahMemberLesson> classMembers = memberLessonMapper.selectByExample(memberLessonExample);

        NewLessonResponse response = new NewLessonResponse();
        response.setId(currentLesson.getId());
        response.setName(currentLesson.getLessonName());
        response.setStartAt(currentLesson.getStartAt());
        response.setEndAt(currentLesson.getEndAt());
        response.setTeacherName(teacher.getFullName());
        response.setTeacherUsername(teacher.getUsername());
        response.setAccid(teacher.getAccid());
        response.setToken(teacher.getToken());

        for (BarablahMemberLesson classMember : classMembers) {
            BarablahMember member = memberMapper.selectByPrimaryKey(classMember.getMemberId());
            ClassMemberDto dto = new ClassMemberDto();
            dto.setId(member.getId());
            dto.setNickname(member.getNickname());
            dto.setAvatar(member.getAvatar());
            dto.setAccid(member.getAccid());

            if (classMember.getProbational()) {
                response.getProbationalMembers().add(dto);
            } else {
                response.getMembers().add(dto);
            }
        }

        // 处理第一次开课的情况，生成网易云房间
        if (StringUtils.isBlank(currentLesson.getRoom())) {
            String room = currentLesson.getLessonName() + "_" + teacher.getFullName() + "_"
                    + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
            currentLesson.setRoom(room);

            BarablahClassLesson lessonToBeUpdated = new BarablahClassLesson();
            lessonToBeUpdated.setId(currentLesson.getId());
            lessonToBeUpdated.setRoom(room);
            lessonMapper.updateByPrimaryKeySelective(lessonToBeUpdated);
        }

        response.setRoom(currentLesson.getRoom());
        return new ApiEntity(response);
    }

    @ApiOperation(value = "获取授课历史")
    @GetMapping(value = "/teachers/lessons/history")
    public ApiEntity<List<LessonDto>> getLessonHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        Date now = new Date();
        BarablahClassLessonExample example = new BarablahClassLessonExample();
        example.or().andTeacherIdEqualTo(teacherId).andEndAtLessThan(now).andDeletedEqualTo(Boolean.FALSE);
        example.or().andTeacherIdEqualTo(teacherId).andStartAtLessThan(now).andEndAtGreaterThan(now).andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("end_at DESC");
        List<BarablahClassLesson> lessons = lessonMapper.selectByExample(example);
        List<LessonDto> response = Lists.newArrayList();

        for (BarablahClassLesson lesson : lessons) {
            LessonDto dto = new LessonDto();
            dto.setId(lesson.getId());
            dto.setName(lesson.getLessonName());
            response.add(dto);
        }

        return new ApiEntity(response);
    }

    @ApiOperation(value = "获取班级列表")
    @GetMapping(value = "/teachers/classes")
    public ApiEntity<List<ClassDto>> getClasses(
            @ApiParam(value = "班级状态（in_review审核中，ongoing线下，finished已结束）") @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        BarablahClassExample example = new BarablahClassExample();
        if (StringUtils.isBlank(status)) {
            example.createCriteria()
                    .andTeacherIdEqualTo(teacherId)
                    .andDeletedEqualTo(Boolean.FALSE);
        } else {
            example.createCriteria()
                    .andTeacherIdEqualTo(teacherId)
                    .andStatusEqualTo(status.toUpperCase())
                    .andDeletedEqualTo(Boolean.FALSE);
        }

        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("created_at DESC");
        List<BarablahClass> classes = classMapper.selectByExample(example);
        List<ClassDto> response = Lists.newArrayList();

        for (BarablahClass aClass : classes) {
            ClassDto dto = new ClassDto();
            dto.setId(aClass.getId());
            dto.setName(aClass.getClassName());

            BarablahClassMemberExample memberExample = new BarablahClassMemberExample();
            memberExample.createCriteria()
                    .andClassIdEqualTo(aClass.getId())
                    .andDeletedEqualTo(Boolean.FALSE);
            List<BarablahClassMember> classMembers = classMemberMapper.selectByExample(memberExample);

            for (BarablahClassMember classMember : classMembers) {
                BarablahMember member = memberMapper.selectByPrimaryKey(classMember.getMemberId());

                if (Objects.nonNull(member)) {
                    ClassMemberDto mDto = new ClassMemberDto();
                    mDto.setId(member.getId());
                    mDto.setNickname(member.getNickname());
                    mDto.setAvatar(member.getAvatar());
                    mDto.setAccid(member.getAccid());
                    dto.getMembers().add(mDto);
                }
            }

            response.add(dto);
        }

        return new ApiEntity(response);
    }

    @ApiOperation(value = "获取班级成员详情")
    @GetMapping(value = "/teachers/classes/{classId}/members/{memberId}")
    public ApiEntity<GetClassMemberResponse> getClassMember(
            @PathVariable Long classId,
            @PathVariable Long memberId) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();
        BarablahTeacher teacher = teacherMapper.selectByPrimaryKey(teacherId);

        // 获取学员信息
        BarablahClassMemberExample example = new BarablahClassMemberExample();
        example.createCriteria()
                .andClassIdEqualTo(classId)
                .andMemberIdEqualTo(memberId)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahClassMember> members = classMemberMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(members)) {
            return new ApiEntity<>(ApiStatus.STATUS_400.getCode(), "找不到班级成员, class=" + classId + ", member=" + memberId);
        }

        BarablahMember member = memberMapper.selectByPrimaryKey(memberId);
        BarablahMemberPassportExample passportExample = new BarablahMemberPassportExample();
        passportExample.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahMemberPassport> passports = passportMapper.selectByExample(passportExample);
        BarablahMemberCommentExample commentExample = new BarablahMemberCommentExample();
        commentExample.createCriteria()
                .andTeacherIdEqualTo(teacherId)
                .andMemberIdEqualTo(memberId)
                .andDeletedEqualTo(Boolean.FALSE);
        commentExample.setOrderByClause("created_at DESC");
        List<BarablahMemberComment> comments = commentMapper.selectByExample(commentExample);

        GetClassMemberResponse response = new GetClassMemberResponse();
        response.setId(member.getId());
        response.setNickname(member.getNickname());
        response.setAvatar(member.getAvatar());
        response.setPoints(member.getPoints());

        if (CollectionUtils.isNotEmpty(passports)) {
            response.setPhoneNumber(passports.get(0).getProviderId());
        }

        for (BarablahMemberComment comment : comments) {
            CommentDto dto = new CommentDto();
            dto.setId(comment.getId());
            dto.setContent(comment.getContent());
            dto.setCreatedAt(comment.getCreatedAt());
            dto.setTeacher(teacher.getFullName());
            dto.setTeacherAvatar(teacher.getAvatar());
            response.getComments().add(dto);
        }

        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "申请开班")
    @PostMapping(value = "/teachers/classes")
    public ApiEntity newClass(@RequestBody ApplyClassRequest request) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();
        BarablahTeacher teacher = teacherMapper.selectByPrimaryKey(teacherId);
        BarablahClass aClass = new BarablahClass();
        aClass.setCourseId(request.getCourseId());
        aClass.setClassName(request.getClassName());
        aClass.setTeacherId(teacherId);
        aClass.setCampusId(teacher.getCampusId());
        aClass.setStatus(ClassStatus.IN_REVIEW);
        classMapper.insertSelective(aClass);

        if (CollectionUtils.isNotEmpty(request.getMemberIds())) {
            for (Long memberId : request.getMemberIds()) {
                BarablahClassMember classMember = new BarablahClassMember();
                classMember.setClassId(aClass.getId());
                classMember.setMemberId(memberId);
                classMember.setStatus(ClassStatus.IN_REVIEW);
                classMemberMapper.insertSelective(classMember);
            }
        }

        return new ApiEntity();
    }

    @ApiOperation(value = "搜索校区学员")
    @GetMapping(value = "/members/search")
    public ApiEntity<List<SearchMemberDto>> searchMembers(
            @RequestParam String number,
            @RequestParam String nickname) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();
        BarablahTeacher teacher = teacherMapper.selectByPrimaryKey(teacherId);
        List<SearchMemberDto> response = Lists.newArrayList();

        if (StringUtils.isNotBlank(number)) {
            BarablahMemberPassportExample passportExample = new BarablahMemberPassportExample();
            passportExample.createCriteria()
                    .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                    .andProviderIdEqualTo(number)
                    .andDeletedEqualTo(Boolean.FALSE);
            List<BarablahMemberPassport> passports = passportMapper.selectByExample(passportExample);

            if (CollectionUtils.isNotEmpty(passports)) {
                BarablahMember member = memberMapper.selectByPrimaryKey(passports.get(0).getMemberId());
                SearchMemberDto dto = new SearchMemberDto();
                dto.setId(member.getId());
                dto.setNickname(member.getNickname());
                dto.setAvatar(member.getAvatar());
                dto.setPhoneNumber(passports.get(0).getProviderId());
                response.add(dto);
            }
        } else if (StringUtils.isNotBlank(nickname)) {
            BarablahMemberExample memberExample = new BarablahMemberExample();
            memberExample.createCriteria()
                    .andCampusIdEqualTo(teacher.getCampusId())
                    .andNicknameLike(nickname)
                    .andDeletedEqualTo(Boolean.FALSE);
            List<BarablahMember> members = memberMapper.selectByExample(memberExample);

            for (BarablahMember member : members) {
                SearchMemberDto dto = new SearchMemberDto();
                dto.setId(member.getId());
                dto.setNickname(member.getNickname());
                dto.setAvatar(member.getAvatar());

                BarablahMemberPassportExample passportExample = new BarablahMemberPassportExample();
                passportExample.createCriteria()
                        .andMemberIdEqualTo(member.getId())
                        .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                        .andDeletedEqualTo(Boolean.FALSE);
                List<BarablahMemberPassport> passports = passportMapper.selectByExample(passportExample);

                if (CollectionUtils.isNotEmpty(passports)) {
                    dto.setPhoneNumber(passports.get(0).getProviderId());
                }

                response.add(dto);
            }
        }

        return new ApiEntity(response);
    }

    @ApiOperation(value = "发表评语")
    @PostMapping(value = "/members/{id}/comments")
    public ApiEntity makeComment(@RequestBody MakeCommentRequest request) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        BarablahMemberComment commentToBeCreated = new BarablahMemberComment();
        commentToBeCreated.setMemberId(request.getMemberId());
        commentToBeCreated.setTeacherId(teacherId);
        commentToBeCreated.setContent(request.getContent());
        commentMapper.insertSelective(commentToBeCreated);
        return new ApiEntity();
    }

    @ApiOperation(value = "发放表情增加积分")
    @PostMapping(value = "/members/{id}/points")
    public ApiEntity givePoints(
            @PathVariable Long id,
            @Validated @RequestBody GivePointsRequest request) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();
        BarablahMember member = memberMapper.selectByPrimaryKey(id);

        if (Objects.isNull(member)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "找不到会员");
        }

        int points = 0;

        if (PointType.TROPHY.name().equals(request.getExpression().toUpperCase())) {
            points = 5;
        } else if (PointType.CLAPPING.name().equals(request.getExpression().toUpperCase())) {
            points = 2;
        } else if (PointType.SMILING.name().equals(request.getExpression().toUpperCase())) {
            points = 1;
        }

        if (points > 0) {
            BarablahMember memberToBeUpdated = new BarablahMember();
            memberToBeUpdated.setId(member.getId());
            memberToBeUpdated.setPoints(member.getPoints() + points);
            memberMapper.updateByPrimaryKeySelective(memberToBeUpdated);

            BarablahMemberPointLog pointLog = new BarablahMemberPointLog();
            pointLog.setMemberId(member.getId());
            pointLog.setPoints(points);
            pointLog.setType(PointType.valueOf(request.getExpression().toUpperCase()));
            pointLogMapper.insertSelective(pointLog);
        }

        return new ApiEntity();
    }

    @ApiOperation(value = "邀请试听会员")
    @PostMapping(value = "/members/{id}/probation")
    public ApiEntity<List<ClassMemberDto>> inviteMember(
            @PathVariable Long id,
            @RequestBody InviteMemberRequest request) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        BarablahClassLesson lesson = lessonMapper.selectByPrimaryKey(request.getLessonId());

        if (Objects.isNull(lesson)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "找不到课时");
        }

        BarablahMemberLessonExample example = new BarablahMemberLessonExample();
        example.createCriteria()
                .andClassIdEqualTo(lesson.getClassId())
                .andLessonIdEqualTo(lesson.getId())
                .andMemberIdEqualTo(id)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahMemberLesson> memberLessons = memberLessonMapper.selectByExample(example);

        if (CollectionUtils.isNotEmpty(memberLessons)) {
            return new ApiEntity<>(ApiStatus.STATUS_400.getCode(), "学员已参加该课程，请勿重复邀请");
        }

        BarablahMemberLesson memberLesson = new BarablahMemberLesson();
        memberLesson.setMemberId(id);
        memberLesson.setClassId(lesson.getClassId());
        memberLesson.setLessonId(lesson.getId());
        memberLesson.setStartAt(lesson.getStartAt());
        memberLesson.setEndAt(lesson.getEndAt());
        memberLesson.setProbational(Boolean.TRUE);
        memberLessonMapper.insertSelective(memberLesson);

        example.clear();
        example.createCriteria()
                .andLessonIdEqualTo(lesson.getId())
                .andProbationalEqualTo(Boolean.TRUE)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahMemberLesson> classMembers = memberLessonMapper.selectByExample(example);
        List<ClassMemberDto> response = Lists.newArrayList();

        for (BarablahMemberLesson classMember : classMembers) {
            BarablahMember member = memberMapper.selectByPrimaryKey(classMember.getMemberId());
            ClassMemberDto dto = new ClassMemberDto();
            dto.setId(member.getId());
            dto.setNickname(member.getNickname());
            dto.setAvatar(member.getAvatar());
            dto.setAccid(member.getAccid());
            response.add(dto);
        }

        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "发布作业")
    @PostMapping(value = "/teachers/homeworks")
    public ApiEntity setHomeworks(@RequestBody SetHomeworksRequest request) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        // 创建教师作业数据
        BarablahTeacherHomework teacherHomework = new BarablahTeacherHomework();
        teacherHomework.setTeacherId(teacherId);
        teacherHomework.setHomeworkName(request.getName());
        teacherHomework.setClosingAt(request.getClosingAt());
        teacherHomeworkMapper.insertSelective(teacherHomework);

        if (CollectionUtils.isNotEmpty(request.getTextbookIds())) {
            for (Long textbookId : request.getTextbookIds()) {
                BarablahTeacherHomeworkItem teacherHomeworkItem = new BarablahTeacherHomeworkItem();
                teacherHomeworkItem.setHomeworkId(teacherHomework.getId());
                teacherHomeworkItem.setTeacherId(teacherId);
                teacherHomeworkItem.setTextbookId(textbookId);
                teacherHomeworkItemMapper.insertSelective(teacherHomeworkItem);
            }
        }

        // 创建会员作业数据
        BarablahClassMemberExample classMemberExample = new BarablahClassMemberExample();
        classMemberExample.createCriteria()
                .andClassIdEqualTo(request.getClassId())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahClassMember> classMembers = classMemberMapper.selectByExample(classMemberExample);

        for (BarablahClassMember cMember : classMembers) {
            BarablahMemberHomework homework = new BarablahMemberHomework();
            homework.setMemberId(cMember.getMemberId());
            homework.setTeacherId(teacherId);
            homework.setHomeworkName(request.getName());
            homework.setClosingAt(request.getClosingAt());
            memberHomeworkMapper.insertSelective(homework);
        }

        return new ApiEntity();
    }

    @ApiOperation(value = "获取学生作业列表")
    @GetMapping(value = "/members/{id}/homeworks")
    public ApiEntity<List<HomeworkDto>> getMemberHomeworks(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();
        BarablahMemberHomeworkExample example = new BarablahMemberHomeworkExample();
        example.createCriteria()
                .andMemberIdEqualTo(id)
                .andTeacherIdEqualTo(teacherId)
                .andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("created_at DESC");
        List<BarablahMemberHomework> homeworks = memberHomeworkMapper.selectByExample(example);
        List<HomeworkDto> response = Lists.newArrayList();

        for (BarablahMemberHomework homework : homeworks) {
            HomeworkDto dto = new HomeworkDto();
            dto.setId(homework.getId());
            dto.setName(homework.getHomeworkName());
            dto.setClosingAt(homework.getClosingAt());
            dto.setStatus(homework.getStatus().name());
            response.add(dto);
        }

        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "获取学生作业题目列表")
    @GetMapping(value = "/members/{memberId}/homeworks/{homeworkId}/items")
    public ApiEntity<List<HomeworkItemDto>> getMemberHomeworkItems(
            @PathVariable Long memberId,
            @PathVariable Long homeworkId) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        BarablahMemberHomeworkItemExample example = new BarablahMemberHomeworkItemExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andHomeworkIdEqualTo(homeworkId)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahMemberHomeworkItem> items = memberHomeworkItemMapper.selectByExample(example);
        List<HomeworkItemDto> response = Lists.newArrayList();

        for (BarablahMemberHomeworkItem item : items) {
            BarablahTextbook textbook = textbookMapper.selectByPrimaryKey(item.getTextbookId());
            HomeworkItemDto dto = new HomeworkItemDto();
            dto.setId(item.getId());
            dto.setAnswer(item.getAnswer());
            dto.setTextbookId(item.getTextbookId());
            dto.setName(textbook.getTextbookName());
            dto.setType(textbook.getType().name());
            dto.setQuestion(textbook.getQuestion());
            dto.setOption(textbook.getOption());
            dto.setCorrect(textbook.getCorrect());
            dto.setImage(textbook.getImage());
            dto.setVideo(textbook.getVideo());
            response.add(dto);
        }

        return new ApiEntity<>(response);
    }

}
