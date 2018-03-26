package com.barablah.web;

import com.barablah.auth.Authenticator;
import com.barablah.domain.*;
import com.barablah.netease.NeteaseClient;
import com.barablah.netease.request.ImUserCreateRequest;
import com.barablah.netease.request.ImUserUpdateRequest;
import com.barablah.netease.response.ImUserCreateResponse;
import com.barablah.repository.*;
import com.barablah.repository.example.*;
import com.barablah.web.enums.BarablahClassStatusEnum;
import com.barablah.web.enums.BarablahMemberStatusEnum;
import com.barablah.web.request.*;
import com.barablah.web.response.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
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
    private BarablahTextbookMapper textbookMapper;

    @Autowired
    private BarablahMemberPointLogMapper pointLogMapper;


    @Autowired
    private BarablahTeacherMajorMapper majorMapper;

    @Autowired
    private BarablahCountryMapper countryMapper;

    @Autowired
    private BarablahCourseMapper courseMapper;

    @Autowired
    private NeteaseClient neteaseClient;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @ApiOperation(value = "获得教师信息")
    @GetMapping(value = "/teachers/getmajors")
    public ApiEntity<List<CampusDto>> getMajors() {
        BarablahTeacherMajorExample example = new BarablahTeacherMajorExample();
        example.createCriteria().andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("position asc,id desc");
        List<BarablahTeacherMajor> campuses = majorMapper.selectByExample(example);

        List<CampusDto> response = Lists.newArrayList();

        for (BarablahTeacherMajor campus : campuses) {
            CampusDto dto = new CampusDto();
            dto.setId(campus.getId());
            dto.setName(campus.getMajorName());
            response.add(dto);
        }
        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "获得国籍信息")
    @GetMapping(value = "/teachers/getcountrys")
    public ApiEntity<List<CampusDto>> getCountrys() {
        BarablahCountryExample example = new BarablahCountryExample();
        example.createCriteria().andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("id asc ");
        List<BarablahCountry> campuses = countryMapper.selectByExample(example);

        List<CampusDto> response = Lists.newArrayList();

        for (BarablahCountry campus : campuses) {
            CampusDto dto = new CampusDto();
            dto.setId(campus.getId());
            dto.setName(campus.getName());
            response.add(dto);
        }
        return new ApiEntity<>(response);    }




    @ApiOperation(value = "教师申请")
    @PostMapping(value = "/teachers/signup")
    public ApiEntity signup(@Valid @RequestBody SignupTeacherRequest request) {
        BarablahTeacherExample example = new BarablahTeacherExample();
        example.createCriteria()
                .andPhoneNumberEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        example.or().andDeletedEqualTo(false).andUsernameEqualTo(request.getPhoneNumber());
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
            log.error("controller:teachers:signup:注册IM账号失败", e);
            return new ApiEntity(ApiStatus.STATUS_500);
        }

        if (!imUserCreateResponse.isSuccess()) {
            log.error("controller:teachers:signup:调用网易云注册IM账号失败, code=" + imUserCreateResponse.getCode());

            ImUserUpdateRequest updateRequest = new ImUserUpdateRequest();
            updateRequest.setAccid("teacher_" + request.getPhoneNumber());
            try {
                //return new ApiEntity(ApiStatus.STATUS_400.getCode(), "注册网易云账号失败,手机号已注册过");
                imUserCreateResponse = neteaseClient.execute(updateRequest);
            } catch (IOException e) {
                return new ApiEntity(ApiStatus.STATUS_500.getCode(),"手机号已注册过");
            }
        }

        BarablahTeacher teacher = new BarablahTeacher();
        teacher.setCampusId(request.getCampusId());
        //判断账号是否已经存在

        teacher.setUsername(request.getPhoneNumber());


        teacher.setPassword(DigestUtils.md5Hex(request.getPassword()));
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
    public ApiEntity<SigninTeacherResponse> signin(@Valid @RequestBody SigninTeacherRequest request) {
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
        response.setUsername(teacher.getUsername());
        response.setFullName(teacher.getFullName());
        response.setAvatar(teacher.getAvatar());
        response.setCampusName(campus.getCampusName());
        return new ApiEntity(response);
    }

    @ApiOperation(value = "忘记密码")
    @PostMapping(value = "/teachers/password/reset")
    public ApiEntity resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String number = ops.get("verifications:code:" + request.getCode());

        if (!Objects.equals(request.getPhoneNumber(), number)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "短信验证码错误");
        } else {
            redisTemplate.delete("verifications:code:" + request.getCode());
        }

        BarablahTeacherExample example = new BarablahTeacherExample();
        example.createCriteria()
                .andPhoneNumberEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahTeacher> teachers = teacherMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(teachers)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "手机号尚未注册, number=" + request.getPhoneNumber());
        }

        BarablahTeacher teacherToBeUpdated = new BarablahTeacher();
        teacherToBeUpdated.setId(teachers.get(0).getId());
        teacherToBeUpdated.setPassword(DigestUtils.md5Hex(request.getPassword()));
        teacherMapper.updateByPrimaryKeySelective(teacherToBeUpdated);
        return new ApiEntity();
    }

    @ApiOperation(value = "获取教师信息")
    @GetMapping(value = "/teachers")
    public ApiEntity<GetTeacherResponse> getTeacherInfo() {
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        BarablahTeacher teacher = teacherMapper.selectByPrimaryKey(teacherId);

        BarablahTeacherMajor major = majorMapper.selectByPrimaryKey(teacher.getMajor());

        BarablahCountry country = countryMapper.selectByPrimaryKey(teacher.getComeFrom());
        BarablahCampus campus = campusMapper.selectByPrimaryKey(teacher.getCampusId());

        GetTeacherResponse response = new GetTeacherResponse();
        response.setCampus(campus.getCampusName());
        response.setId(teacher.getId());
        response.setUsername(teacher.getUsername());
        response.setFullName(teacher.getFullName());
        response.setPhoneNumber(teacher.getPhoneNumber());
        response.setAvatar(teacher.getAvatar());

        response.setMajor(major.getMajorName());
        response.setComeFrom(country.getName());

        response.setMajorId(major.getId());

        response.setComeFromId(teacher.getComeFrom());
        return new ApiEntity(response);
    }

    @ApiOperation(value = "更新教师信息")
    @PostMapping(value = "/teachers/update")
    public ApiEntity updateTeacherInfo(@RequestBody UpdateTeacherInfoRequest request) {
        if (!authenticator.isTeacherAuthenticated()) {
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
    //获取即将开班和正在进行班级列表,获得即将备课的最近2节课
    public ApiEntity<List<ClassDto>> getLessons() {
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();
        List<String>  status = new ArrayList<>();
        status.add(BarablahClassStatusEnum.已开课.getValue());
        status.add(BarablahClassStatusEnum.待开课.getValue());

        BarablahClassExample example = new BarablahClassExample();
        example.createCriteria()
                .andTeacherIdEqualTo(teacherId).
                    andStatusIn(status)
                .andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("created_at DESC");
        List<BarablahClass> classes = classMapper.selectByExample(example);
        List<ClassDto> response = Lists.newArrayList();


        for (BarablahClass aClass : classes) {
            BarablahCourse course = courseMapper.selectByPrimaryKey(aClass.getCourseId());
            ClassDto dto = new ClassDto();
            dto.setId(aClass.getId());
            dto.setName(aClass.getClassName());
            if (aClass.getStatus().equals(BarablahClassStatusEnum.已开课.getValue())) {
                dto.setCourseName(course.getCourseName()+"(开课中)");

            } else {
                dto.setCourseName(course.getCourseName()+"(即将开课)");
            }

            BarablahClassLessonExample lessonExample = new BarablahClassLessonExample();
            lessonExample.createCriteria()
                    .andClassIdEqualTo(aClass.getId())
                    .andTypeEqualTo(LessonType.ONLINE.name())
                    .andDeletedEqualTo(Boolean.FALSE);
            lessonExample.setOrderByClause("start_at");
            List<BarablahClassLesson> lessons = lessonMapper.selectByExample(lessonExample);

            int i = 0;
            Date curDate = new Date();
            for (BarablahClassLesson lesson : lessons) {

                if (lesson.getStartAt().after(curDate)) {
                    i++;
                }
                //最多只允许备两节,还没有开始的课
                if (i==3) {
                    break;
                }
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



    @ApiOperation(value = "标记已备课")
    @PostMapping(value = "/teachers/lessons/{id}/prepared")
    public ApiEntity prepare(@PathVariable Long id) {
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }


        //获取当前备课课程
        BarablahClassLesson lesson = lessonMapper.selectByPrimaryKey(id);
        if(lesson.getPrepared()) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "您已经背过本课");

        }
        if (lesson.getStartAt().before(new Date())) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "课时已经开始,请提前备课");

        }
        if (Objects.isNull(lesson)) {
            return new ApiEntity(ApiStatus.STATUS_404);
        }
        Date curDate = new Date();
        BarablahClassLessonExample firstExample = new BarablahClassLessonExample();
        firstExample.createCriteria()
                .andTypeEqualTo(LessonType.ONLINE.name())
                .andCategoryIdEqualTo(lesson.getCategoryId())
                .andClassIdEqualTo(lesson.getClassId())
                .andStartAtLessThanOrEqualTo(curDate)
                .andPreparedEqualTo(false)
                .andIdNotEqualTo(lesson.getId())
                .andDeletedEqualTo(Boolean.FALSE);

        if(lessonMapper.countByExample(firstExample)>0) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "请按照课时顺序背课");
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        //得到任课老师所有的班级列表,用来查询所有的同样教材的课程。
        List<String>  status = new ArrayList<>();
        status.add(BarablahClassStatusEnum.已开课.getValue());
        status.add(BarablahClassStatusEnum.待开课.getValue());

        BarablahClassExample example = new BarablahClassExample();
        example.createCriteria()
                .andTeacherIdEqualTo(teacherId).
                andStatusIn(status)
                .andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("created_at DESC");
        List<BarablahClass> classes = classMapper.selectByExample(example);

        List<Long> classids = new ArrayList<>();
        for(BarablahClass c:classes) {
            classids.add(c.getId());
        }

        //查找并更新最近三天内即将开始的所有同样教材的课程备课标志为已备课。
        BarablahClassLessonExample lessonExample = new BarablahClassLessonExample();
        lessonExample.createCriteria()
                .andTypeEqualTo(LessonType.ONLINE.name())
                .andCategoryIdEqualTo(lesson.getCategoryId())
                .andClassIdIn(classids)
                .andStartAtLessThanOrEqualTo(curDate)
                .andStartAtGreaterThanOrEqualTo(DateUtils.addDays(curDate,3))
                .andDeletedEqualTo(Boolean.FALSE);
        BarablahClassLesson ul = new BarablahClassLesson();
        ul.setPrepared(true);
        lessonMapper.updateByExample(ul,lessonExample);

        return new ApiEntity();
    }



    @ApiOperation(value = "获取课时的题目列表")
    @GetMapping(value = "/teachers/lessons/{id}/textbooks1")
    public ApiEntity<GetTextbooksResponse> getLessonTextbooks1(@PathVariable Long id) {
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        BarablahTextbookExample textbookExample = new BarablahTextbookExample();
        textbookExample.createCriteria()
                .andCategoryIdEqualTo(id)
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahTextbook> textbooks = textbookMapper.selectByExample(textbookExample);
        GetTextbooksResponse response = new GetTextbooksResponse();

        for (BarablahTextbook textbook : textbooks) {
            TextbookDto dto = new TextbookDto();
            dto.setId(textbook.getId());
            dto.setType(textbook.getType().name());
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


    @ApiOperation(value = "获取课时的题目列表")
    @GetMapping(value = "/teachers/lessons/{id}/textbooks")
    public ApiEntity<GetTextbooksResponse> getLessonTextbooks(@PathVariable Long id) {
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        BarablahClassLesson lesson  = lessonMapper.selectByPrimaryKey(id);
        if (Objects.isNull(lesson)) {
            return new ApiEntity(ApiStatus.STATUS_404);
        }

        BarablahTextbookExample textbookExample = new BarablahTextbookExample();
        textbookExample.createCriteria()
                .andCategoryIdEqualTo(lesson.getCategoryId())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahTextbook> textbooks = textbookMapper.selectByExample(textbookExample);
        GetTextbooksResponse response = new GetTextbooksResponse();

        for (BarablahTextbook textbook : textbooks) {
            TextbookDto dto = new TextbookDto();
            dto.setId(textbook.getId());
            dto.setType(textbook.getType().name());
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
        response.setClassid(lesson.getClassId());
        response.setCatid(lesson.getCategoryId());
        return new ApiEntity(response);
    }

    @ApiOperation(value = "更新课时信息")
    @PostMapping(value = "/teachers/lessons/{id}")
    public ApiEntity updateLesson(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLessonRequest request) {
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();
        BarablahClassLesson lesson = lessonMapper.selectByPrimaryKey(id);

        if (Objects.isNull(lesson)) {
            return new ApiEntity(ApiStatus.STATUS_404);
        }

        BarablahClassLesson lessonToBeUpdated = new BarablahClassLesson();
        lessonToBeUpdated.setId(id);
        lessonToBeUpdated.setVideo(request.getVideo());
        lessonMapper.updateByPrimaryKeySelective(lessonToBeUpdated);
        return new ApiEntity();
    }

    @ApiOperation(value = "开始授课")
    @GetMapping(value = "/teachers/lessons/current")
    public ApiEntity<NewLessonResponse> newLesson() {
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        //老师信息
        Long teacherId = authenticator.getCurrentTeacherId();
        BarablahTeacher teacher = teacherMapper.selectByPrimaryKey(teacherId);

        //查找正在直播的课时
        BarablahClassExample classExample = new BarablahClassExample();
        List status = new ArrayList();
        status.add(BarablahClassStatusEnum.待开课.getValue());
        status.add(BarablahClassStatusEnum.已开课.getValue());
        classExample.createCriteria().
                andTeacherIdEqualTo(teacherId).
                andDeletedEqualTo(false).
                andStatusIn(status);
        List<BarablahClass> cs = classMapper.selectByExample(classExample);
        if (cs==null || cs.size()==0) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "当前没有正在进行的课程");
        }
        //查找符合条件的课时
        List classids = new ArrayList();
        for(BarablahClass c:cs) {
            classids.add(c.getId());
        }
        Date now = new Date();
        BarablahClassLessonExample lessonExample = new BarablahClassLessonExample();
        lessonExample.createCriteria()
                .andClassIdIn(classids)
                .andStartAtLessThan(DateUtils.addMinutes(now, 5))
                .andEndAtGreaterThan(now)
                .andTypeEqualTo(LessonType.ONLINE.name())
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahClassLesson> lessons = lessonMapper.selectByExample(lessonExample);
        if (CollectionUtils.isEmpty(lessons)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "当前没有正在进行的课程");
        }
        if (lessons.size()!=1) {
        }
        //获得当前课程
        BarablahClassLesson currentLesson = lessons.get(0);
        NewLessonResponse response = new NewLessonResponse();
        response.setId(currentLesson.getId());
        response.setName(currentLesson.getLessonName());
        response.setStartAt(currentLesson.getStartAt());
        response.setEndAt(currentLesson.getEndAt());
        response.setTeacherName(teacher.getFullName());
        response.setTeacherUsername(teacher.getUsername());
        response.setAccid(teacher.getAccid());
        response.setToken(teacher.getToken());

        //获得班级的所有学生
        BarablahClassMemberExample memberExample = new BarablahClassMemberExample();
        memberExample.createCriteria().
                andClassIdEqualTo(currentLesson.getClassId()).
                andDeletedEqualTo(false);
        List<BarablahClassMember> classMembers = classMemberMapper.selectByExample(memberExample);
        for (BarablahClassMember classMember : classMembers) {
            BarablahMember member = memberMapper.selectByPrimaryKey(classMember.getMemberId());
            if (member.getDeleted()) {
                continue;
            }
            if (!member.getStatus().equals(BarablahMemberStatusEnum.启用.getValue())) {
                continue;
            }
            ClassMemberDto dto = new ClassMemberDto();
            dto.setId(member.getId());
            dto.setNickname(member.getNickname());
            dto.setAvatar(member.getAvatar());
            dto.setAccid(member.getAccid());
            response.getMembers().add(dto);
        }

        //获取试听学员信息
        BarablahMemberLessonExample memberLessonExample = new BarablahMemberLessonExample();
        memberLessonExample.createCriteria().
                andClassIdEqualTo(currentLesson.getClassId()).
                andDeletedEqualTo(false).
                andProbationalEqualTo(true).
                andLessonIdEqualTo(currentLesson.getId());
        List<BarablahMemberLesson> membetLessons = memberLessonMapper.selectByExample(memberLessonExample);
        for (BarablahMemberLesson memberLesson : membetLessons) {
            BarablahMember member = memberMapper.selectByPrimaryKey(memberLesson.getMemberId());
            if (member.getDeleted()) {
                continue;
            }
            if (!member.getStatus().equals("ENABLED")) {
                continue;
            }
            ClassMemberDto dto = new ClassMemberDto();
            dto.setId(member.getId());
            dto.setNickname(member.getNickname());
            dto.setAvatar(member.getAvatar());
            dto.setAccid(member.getAccid());
            response.getProbationalMembers().add(dto);
        }

        // 处理第一次开课的情况，生成网易云房间
        if (StringUtils.isBlank(currentLesson.getRoom())) {
            String room = currentLesson.getLessonName() + "_" + teacher.getFullName() + "_"
                    + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
            currentLesson.setRoom(room);

            BarablahClassLesson lessonToBeUpdated = new BarablahClassLesson();
            lessonToBeUpdated.setId(currentLesson.getId());
            lessonToBeUpdated.setSign(true);
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
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        Date now = new Date();
        BarablahClassLessonExample example = new BarablahClassLessonExample();
//        example.or().andTeacherIdEqualTo(teacherId).andEndAtLessThan(now).andDeletedEqualTo(Boolean.FALSE);
//        example.or().andTeacherIdEqualTo(teacherId).andStartAtLessThan(now).andEndAtGreaterThan(now).andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("end_at DESC");
        List<BarablahClassLesson> lessons = lessonMapper.selectByExample(example);
        List<LessonDto> response = Lists.newArrayList();

        for (BarablahClassLesson lesson : lessons) {
            LessonDto dto = new LessonDto();
            dto.setId(lesson.getId());
            dto.setName(lesson.getLessonName());
            dto.setStartAt(lesson.getStartAt());
            if (now.after(lesson.getStartAt()) && now.before(lesson.getEndAt())) {
                dto.setStatus("ONGOING");
            } else {
                dto.setStatus("FINISHED");
            }
            response.add(dto);
        }

        return new ApiEntity(response);
    }

    @ApiOperation(value = "获取班级数量")
    @GetMapping(value = "/teachers/getclassnums")
    public ApiEntity<Long> getClasseNums() {
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();
        BarablahClassExample example = new BarablahClassExample();
        List list = new ArrayList();
        list.add("WAITING");
        list.add("ONGOING");
        list.add("FINISHED");

        example.createCriteria().andStatusIn(list).andTeacherIdEqualTo(teacherId).andDeletedEqualTo(Boolean.FALSE);
        long num = classMapper.countByExample(example);
        return new ApiEntity<>(Long.valueOf(num));

    }


    @ApiOperation(value = "搜索校区学员")
    @GetMapping(value = "/members/search")
    public ApiEntity<List<SearchMemberDto>> searchMembers(
            @RequestParam String number,
            @RequestParam String nickname) {
        if (!authenticator.isTeacherAuthenticated()) {
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
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        BarablahMemberComment commentToBeCreated = new BarablahMemberComment();
        commentToBeCreated.setMemberId(request.getMemberId());
        commentToBeCreated.setTeacherId(teacherId);
        commentToBeCreated.setContent(request.getContent());
        //commentToBeCreated.set
        commentMapper.insertSelective(commentToBeCreated);

        if (request.getScore() > 0) {
            BarablahMember member = memberMapper.selectByPrimaryKey(request.getMemberId());

            BarablahMember memberToBeUpdated = new BarablahMember();
            memberToBeUpdated.setId(request.getMemberId());
            memberToBeUpdated.setPoints(member.getPoints() + request.getScore());
            memberMapper.updateByPrimaryKeySelective(memberToBeUpdated);

            BarablahMemberPointLog pointLog = new BarablahMemberPointLog();
            pointLog.setMemberId(request.getMemberId());
            pointLog.setPoints(request.getScore());
            pointLog.setType(PointType.班级表现.value());
            pointLogMapper.insertSelective(pointLog);
        }

        return new ApiEntity();
    }

    @ApiOperation(value = "发放表情增加积分")
    @PostMapping(value = "/members/{id}/points")
    public ApiEntity givePoints(
            @PathVariable Long id,
            @Valid @RequestBody GivePointsRequest request) {
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();
        BarablahMember member = memberMapper.selectByPrimaryKey(id);

        if (Objects.isNull(member)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "找不到会员");
        }

        int points = 0;

        if (PointType.奖杯.value().equals(request.getExpression().toUpperCase())) {
            points = 5;
        } else if (PointType.鼓掌.value().equals(request.getExpression().toUpperCase())) {
            points = 2;
        } else if (PointType.开心.value().equals(request.getExpression().toUpperCase())) {
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
            pointLog.setType(request.getExpression().toUpperCase());
            pointLogMapper.insertSelective(pointLog);
        }

        return new ApiEntity();
    }

    @ApiOperation(value = "邀请试听会员")
    @PostMapping(value = "/members/{id}/probation")
    public ApiEntity<List<ClassMemberDto>> inviteMember(
            @PathVariable Long id,
            @RequestBody InviteMemberRequest request) {
        if (!authenticator.isTeacherAuthenticated()) {
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




    public void classlog() {
    }

}
