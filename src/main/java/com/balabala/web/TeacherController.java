package com.balabala.web;

import com.balabala.auth.Authenticator;
import com.balabala.domain.*;
import com.balabala.netease.NeteaseClient;
import com.balabala.netease.request.ImUserCreateRequest;
import com.balabala.netease.response.ImUserCreateResponse;
import com.balabala.repository.*;
import com.balabala.repository.example.*;
import com.balabala.web.request.*;
import com.balabala.web.response.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    private BalabalaTeacherHomeworkMapper teacherHomeworkMapper;

    @Autowired
    private BalabalaTeacherHomeworkItemMapper teacherHomeworkItemMapper;

    @Autowired
    private BalabalaMemberMapper memberMapper;

    @Autowired
    private BalabalaMemberPassportMapper passportMapper;

    @Autowired
    private BalabalaMemberLessonMapper memberLessonMapper;

    @Autowired
    private BalabalaClassMapper classMapper;

    @Autowired
    private BalabalaClassMemberMapper classMemberMapper;

    @Autowired
    private BalabalaClassLessonMapper lessonMapper;

    @Autowired
    private BalabalaMemberCommentMapper commentMapper;

    @Autowired
    private BalabalaMemberHomeworkMapper memberHomeworkMapper;

    @Autowired
    private BalabalaMemberHomeworkItemMapper memberHomeworkItemMapper;

    @Autowired
    private BalabalaCourseMapper courseMapper;

    @Autowired
    private BalabalaTextbookMapper textbookMapper;

    @Autowired
    private NeteaseClient neteaseClient;

    @ApiOperation(value = "教师申请")
    @PostMapping(value = "/teachers/signup")
    public ApiEntity signup(@Validated @RequestBody SignupTeacherRequest request) throws IOException {
        BalabalaTeacherExample example = new BalabalaTeacherExample();
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

        return new ApiEntity();
    }

    @ApiOperation(value = "手机号登录")
    @PostMapping(value = "/teachers/signin")
    public ApiEntity<SigninTeacherResponse> signin(@Validated @RequestBody SigninTeacherRequest request) {
        BalabalaTeacherExample example = new BalabalaTeacherExample();
        example.or().andPhoneNumberEqualTo(request.getLoginName()).andDeletedEqualTo(Boolean.FALSE);
        example.or().andUsernameEqualTo(request.getLoginName()).andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaTeacher> teachers = teacherMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(teachers)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "手机号或账号不存在");
        }

        BalabalaTeacher teacher = teachers.get(0);
        if (!Objects.equals(teacher.getStatus(), BalabalaTeacherStatus.ENABLED)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "账号状态异常, status=" + teacher.getStatus().name());
        }

        if (!Objects.equals(DigestUtils.md5Hex(request.getPassword()), teacher.getPassword())) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "密码错误");
        }

        authenticator.newSessionForTeacher(teacher.getId());
        BalabalaCampus campus = campusMapper.selectByPrimaryKey(teacher.getCampusId());

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

        BalabalaTeacher teacher = teacherMapper.selectByPrimaryKey(teacherId);
        BalabalaCampus campus = campusMapper.selectByPrimaryKey(teacher.getCampusId());

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

        BalabalaTeacher teacherToBeUpdated = new BalabalaTeacher();
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

        BalabalaClassExample example = new BalabalaClassExample();
        example.createCriteria()
                .andTeacherIdEqualTo(teacherId)
                .andStatusEqualTo(BalabalaClassStatus.ONGOING.name())
                .andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("created_at DESC");
        List<BalabalaClass> classes = classMapper.selectByExample(example);
        List<ClassDto> response = Lists.newArrayList();

        for (BalabalaClass aClass : classes) {
            BalabalaCourse course = courseMapper.selectByPrimaryKey(aClass.getCourseId());

            ClassDto dto = new ClassDto();
            dto.setId(aClass.getId());
            dto.setName(aClass.getClassName());
            dto.setCourseName(course.getCourseName());

            BalabalaClassLessonExample lessonExample = new BalabalaClassLessonExample();
            lessonExample.createCriteria()
                    .andClassIdEqualTo(teacherId)
                    .andDeletedEqualTo(Boolean.FALSE);
            lessonExample.setOrderByClause("start_at");
            List<BalabalaClassLesson> lessons = lessonMapper.selectByExample(lessonExample);

            for (BalabalaClassLesson lesson : lessons) {
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
    public ApiEntity<GetTextbooksResponse> getLessonTextbooks(@RequestParam Long id) {
        Long teacherId = authenticator.getCurrentTeacherId();
        BalabalaClassLessonExample example = new BalabalaClassLessonExample();
        example.createCriteria()
                .andIdEqualTo(id)
                .andTeacherIdEqualTo(teacherId)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaClassLesson> lessons = lessonMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(lessons)) {
            return new ApiEntity(ApiStatus.STATUS_401.getCode(), "找不到课程或您无权访问");
        }

        BalabalaClassLesson lesson = lessons.get(0);
        BalabalaTextbookExample textbookExample = new BalabalaTextbookExample();
        textbookExample.createCriteria()
                .andCategoryIdEqualTo(lesson.getCategoryId())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaTextbook> textbooks = textbookMapper.selectByExample(textbookExample);
        GetTextbooksResponse response = new GetTextbooksResponse();

        for (BalabalaTextbook textbook : textbooks) {
            TextbookDto dto = new TextbookDto();
            dto.setId(textbook.getId());
            dto.setName(textbook.getTextbookName());
            dto.setQuestion(textbook.getQuestion());
            dto.setCorrect(textbook.getCorrect());
            dto.setImage(textbook.getImage());

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
        BalabalaClassLessonExample example = new BalabalaClassLessonExample();
        example.createCriteria()
                .andIdEqualTo(id)
                .andTeacherIdEqualTo(teacherId)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaClassLesson> lessons = lessonMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(lessons)) {
            return new ApiEntity(ApiStatus.STATUS_401.getCode(), "找不到课程或您无权访问");
        }

        BalabalaClassLesson lessonToBeUpdated = new BalabalaClassLesson();
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
        BalabalaClassLessonExample lessonExample = new BalabalaClassLessonExample();
        lessonExample.createCriteria()
                .andTeacherIdEqualTo(teacherId)
                .andStartAtLessThan(DateUtils.addMinutes(now, 5))
                .andEndAtGreaterThan(now)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaClassLesson> lessons = lessonMapper.selectByExample(lessonExample);

        if (CollectionUtils.isEmpty(lessons)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "当前没有正在进行的课程");
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
            dto.setAccid(member.getAccid());
            response.getMembers().add(dto);
        }

        // 处理第一次开课的情况，生成网易云房间，创建学员课时记录
        if (StringUtils.isBlank(currentLesson.getRoom())) {
            String room = currentLesson.getLessonName() + "_" + teacher.getFullName() + "_"
                    + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
            currentLesson.setRoom(room);

            BalabalaClassLesson lessonToBeUpdated = new BalabalaClassLesson();
            lessonToBeUpdated.setId(currentLesson.getId());
            lessonToBeUpdated.setRoom(room);
            lessonMapper.updateByPrimaryKeySelective(lessonToBeUpdated);

            for (BalabalaClassMember classMember : members) {
                BalabalaMemberLesson memberLesson = new BalabalaMemberLesson();
                memberLesson.setMemberId(classMember.getMemberId());
                memberLesson.setClassId(currentLesson.getClassId());
                memberLesson.setLessonId(currentLesson.getId());
                memberLesson.setStartAt(currentLesson.getStartAt());
                memberLesson.setEndAt(currentLesson.getEndAt());
                memberLessonMapper.insertSelective(memberLesson);
            }
        }

        response.setRoom(currentLesson.getRoom());
        return new ApiEntity(response);
    }

    @ApiOperation(value = "获取授课历史")
    @GetMapping(value = "/teachers/lessons/history")
    public ApiEntity<List<LessonDto>> getLessonHistory(@RequestParam int page, @RequestParam int size) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        Date now = new Date();
        BalabalaClassLessonExample example = new BalabalaClassLessonExample();
        example.or().andTeacherIdEqualTo(teacherId).andEndAtLessThan(now).andDeletedEqualTo(Boolean.FALSE);
        example.or().andTeacherIdEqualTo(teacherId).andStartAtLessThan(now).andEndAtGreaterThan(now).andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("end_at DESC");
        List<BalabalaClassLesson> lessons = lessonMapper.selectByExample(example);
        List<LessonDto> response = Lists.newArrayList();

        for (BalabalaClassLesson lesson : lessons) {
            LessonDto dto = new LessonDto();
            dto.setId(lesson.getId());
            dto.setName(lesson.getLessonName());
            response.add(dto);
        }

        return new ApiEntity(response);
    }

    @ApiOperation(value = "获取班级列表")
    @GetMapping(value = "/teachers/classes")
    public ApiEntity<List<ClassDto>> getClasses() {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        BalabalaClassExample example = new BalabalaClassExample();
        example.createCriteria()
                .andTeacherIdEqualTo(teacherId)
                .andStatusEqualTo(BalabalaClassStatus.ONGOING.name())
                .andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("created_at DESC");
        List<BalabalaClass> classes = classMapper.selectByExample(example);
        List<ClassDto> response = Lists.newArrayList();

        for (BalabalaClass aClass : classes) {
            ClassDto dto = new ClassDto();
            dto.setId(aClass.getId());
            dto.setName(aClass.getClassName());

            BalabalaClassMemberExample memberExample = new BalabalaClassMemberExample();
            memberExample.createCriteria()
                    .andClassIdEqualTo(aClass.getId())
                    .andDeletedEqualTo(Boolean.FALSE);
            List<BalabalaClassMember> classMembers = classMemberMapper.selectByExample(memberExample);

            for (BalabalaClassMember classMember : classMembers) {
                BalabalaMember member = memberMapper.selectByPrimaryKey(classMember.getMemberId());

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
            @RequestParam Long classId,
            @RequestParam Long memberId) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        BalabalaClassMemberExample example = new BalabalaClassMemberExample();
        example.createCriteria()
                .andClassIdEqualTo(classId)
                .andMemberIdEqualTo(memberId)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaClassMember> members = classMemberMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(members)) {
            return new ApiEntity<>(ApiStatus.STATUS_400.getCode(), "找不到班级成员, class=" + classId + ", member=" + memberId);
        }

        BalabalaMember member = memberMapper.selectByPrimaryKey(memberId);
        BalabalaMemberCommentExample commentExample = new BalabalaMemberCommentExample();
        commentExample.createCriteria()
                .andTeacherIdEqualTo(teacherId)
                .andMemberIdEqualTo(memberId)
                .andDeletedEqualTo(Boolean.FALSE);
        commentExample.setOrderByClause("created_at DESC");
        List<BalabalaMemberComment> comments = commentMapper.selectByExample(commentExample);

        GetClassMemberResponse response = new GetClassMemberResponse();
        response.setId(member.getId());
        response.setNickname(member.getNickname());
        response.setAvatar(member.getAvatar());
        response.setPoints(member.getPoints());

        for (BalabalaMemberComment comment : comments) {
            CommentDto dto = new CommentDto();
            dto.setId(comment.getId());
            dto.setContent(comment.getContent());
            dto.setCreatedAt(comment.getCreatedAt());
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

        BalabalaClass aClass = new BalabalaClass();
        aClass.setCourseId(request.getCourseId());
        aClass.setClassName(request.getClassName());
        aClass.setTeacherId(teacherId);
        aClass.setStatus(BalabalaClassStatus.IN_REVIEW);
        classMapper.insertSelective(aClass);

        if (CollectionUtils.isNotEmpty(request.getMemberIds())) {
            for (Long memberId : request.getMemberIds()) {
                BalabalaClassMember classMember = new BalabalaClassMember();
                classMember.setClassId(aClass.getId());
                classMember.setMemberId(memberId);
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
        BalabalaTeacher teacher = teacherMapper.selectByPrimaryKey(teacherId);
        List<SearchMemberDto> response = Lists.newArrayList();

        if (StringUtils.isNotBlank(number)) {
            BalabalaMemberPassportExample passportExample = new BalabalaMemberPassportExample();
            passportExample.createCriteria()
                    .andProviderEqualTo(BalabalaMemberPassportProvider.PHONE.name())
                    .andProviderIdEqualTo(number)
                    .andDeletedEqualTo(Boolean.FALSE);
            List<BalabalaMemberPassport> passports = passportMapper.selectByExample(passportExample);

            if (CollectionUtils.isNotEmpty(passports)) {
                BalabalaMember member = memberMapper.selectByPrimaryKey(passports.get(0).getMemberId());
                SearchMemberDto dto = new SearchMemberDto();
                dto.setId(member.getId());
                dto.setNickname(member.getNickname());
                dto.setAvatar(member.getAvatar());
                dto.setPhoneNumber(passports.get(0).getProviderId());
                response.add(dto);
            }
        } else if (StringUtils.isNotBlank(nickname)) {
            BalabalaMemberExample memberExample = new BalabalaMemberExample();
            memberExample.createCriteria()
                    .andCampusIdEqualTo(teacher.getCampusId())
                    .andNicknameLike(nickname)
                    .andDeletedEqualTo(Boolean.FALSE);
            List<BalabalaMember> members = memberMapper.selectByExample(memberExample);

            for (BalabalaMember member : members) {
                SearchMemberDto dto = new SearchMemberDto();
                dto.setId(member.getId());
                dto.setNickname(member.getNickname());
                dto.setAvatar(member.getAvatar());

                BalabalaMemberPassportExample passportExample = new BalabalaMemberPassportExample();
                passportExample.createCriteria()
                        .andMemberIdEqualTo(member.getId())
                        .andProviderEqualTo(BalabalaMemberPassportProvider.PHONE.name())
                        .andDeletedEqualTo(Boolean.FALSE);
                List<BalabalaMemberPassport> passports = passportMapper.selectByExample(passportExample);

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

        BalabalaMemberComment commentToBeCreated = new BalabalaMemberComment();
        commentToBeCreated.setMemberId(request.getMemberId());
        commentToBeCreated.setTeacherId(teacherId);
        commentToBeCreated.setContent(request.getContent());
        commentMapper.insertSelective(commentToBeCreated);
        return new ApiEntity();
    }

    @ApiOperation(value = "发放积分")
    @PostMapping(value = "/members/{id}/points")
    public ApiEntity givePoints(
            @PathVariable Long id,
            @Validated @RequestBody GivePointsRequest request) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();
        BalabalaMember member = memberMapper.selectByPrimaryKey(id);

        if (Objects.isNull(member)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "找不到会员");
        }

        BalabalaMember memberToBeUpdated = new BalabalaMember();
        memberToBeUpdated.setId(member.getId());
        memberToBeUpdated.setPoints(member.getPoints() + request.getPoints());
        memberMapper.updateByPrimaryKeySelective(memberToBeUpdated);
        return new ApiEntity();
    }

    @ApiOperation(value = "邀请试听会员")
    @PostMapping(value = "/members/{id}/probation")
    public ApiEntity inviteMember(
            @PathVariable Long id,
            @RequestBody InviteMemberRequest request) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        BalabalaClassLesson lesson = lessonMapper.selectByPrimaryKey(request.getLessonId());

        if (Objects.isNull(lesson)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "找不到课时");
        }

        BalabalaMemberLesson memberLesson = new BalabalaMemberLesson();
        memberLesson.setMemberId(id);
        memberLesson.setClassId(lesson.getClassId());
        memberLesson.setLessonId(lesson.getId());
        memberLesson.setStartAt(lesson.getStartAt());
        memberLesson.setEndAt(lesson.getEndAt());
        memberLessonMapper.insertSelective(memberLesson);
        return new ApiEntity();
    }

    @ApiOperation(value = "发布作业")
    @PostMapping(value = "/teachers/homeworks")
    public ApiEntity setHomeworks(@RequestBody SetHomeworksRequest request) {
        if (!authenticator.authenticateForTeacher()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        // 创建教师作业数据
        BalabalaTeacherHomework teacherHomework = new BalabalaTeacherHomework();
        teacherHomework.setTeacherId(teacherId);
        teacherHomework.setHomeworkName(request.getName());
        teacherHomework.setClosingAt(request.getClosingAt());
        teacherHomeworkMapper.insertSelective(teacherHomework);

        if (CollectionUtils.isNotEmpty(request.getTextbookIds())) {
            for (Long textbookId : request.getTextbookIds()) {
                BalabalaTeacherHomeworkItem teacherHomeworkItem = new BalabalaTeacherHomeworkItem();
                teacherHomeworkItem.setHomeworkId(teacherHomework.getId());
                teacherHomeworkItem.setTeacherId(teacherId);
                teacherHomeworkItem.setTextbookId(textbookId);
                teacherHomeworkItemMapper.insertSelective(teacherHomeworkItem);
            }
        }

        // 创建会员作业数据
        BalabalaClassMemberExample classMemberExample = new BalabalaClassMemberExample();
        classMemberExample.createCriteria()
                .andClassIdEqualTo(request.getClassId())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BalabalaClassMember> classMembers = classMemberMapper.selectByExample(classMemberExample);

        for (BalabalaClassMember cMember : classMembers) {
            BalabalaMemberHomework homework = new BalabalaMemberHomework();
            homework.setMemberId(cMember.getMemberId());
            homework.setTeacherId(teacherId);
            homework.setHomeworkName(request.getName());
            homework.setClosingAt(request.getClosingAt());
            memberHomeworkMapper.insertSelective(homework);
        }

        return new ApiEntity();
    }

}
