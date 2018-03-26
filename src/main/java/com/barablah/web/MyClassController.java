package com.barablah.web;

import com.barablah.domain.*;
import com.barablah.repository.example.BarablahClassExample;
import com.barablah.repository.example.BarablahClassMemberExample;
import com.barablah.repository.example.BarablahMemberCommentExample;
import com.barablah.repository.example.BarablahMemberPassportExample;
import com.barablah.web.enums.BarablahClassMemberStatusEnum;
import com.barablah.web.enums.BarablahClassStatusEnum;
import com.barablah.web.request.ApplyClassRequest;
import com.barablah.web.response.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by ling on 2018/3/26.
 */
@Slf4j
@Api(tags = "班级相关API", description = "班级相关API接口")
@RestController
public class MyClassController extends BaseController {

    @ApiOperation(value = "获取班级列表")
    @GetMapping(value = "/teachers/classes")
    public ApiEntity<List<ClassDto>> getClasses(
            @ApiParam(value = "班级状态（in_review审核中，ongoing线下，finished已结束）") @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();

        BarablahClassExample example = new BarablahClassExample();
        if (StringUtils.isBlank(status)) {
            List list = new ArrayList();
            list.add(BarablahClassStatusEnum.已开课.getValue());
            list.add(BarablahClassStatusEnum.已结束.getValue());
            list.add(BarablahClassStatusEnum.待开课.getValue());

            example.createCriteria().
                    andStatusIn(list).
                    andTeacherIdEqualTo(teacherId).
                    andDeletedEqualTo(Boolean.FALSE);
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
            if (aClass.getStatus().equals(BarablahClassStatusEnum.已开课.getValue())) {
                dto.setName(aClass.getClassName()+"(已开课)");

            } else if (aClass.getStatus().equals(BarablahClassStatusEnum.待开课.getValue())) {
                dto.setName(aClass.getClassName()+"(待开课)");
            } else {
                dto.setName(aClass.getClassName()+"(已结束)");
            }

            BarablahClassMemberExample memberExample = new BarablahClassMemberExample();

            memberExample.createCriteria()
                    .andClassIdEqualTo(aClass.getId())
                    .andStatusEqualTo(BarablahClassMemberStatusEnum.进行中.getValue())
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
        if (!authenticator.isTeacherAuthenticated()) {
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
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();
        BarablahTeacher teacher = teacherMapper.selectByPrimaryKey(teacherId);

        BarablahCourse c = courseMapper.selectByPrimaryKey(request.getCourseId());

        BarablahClass aClass = new BarablahClass();
        aClass.setCourseId(request.getCourseId());
        aClass.setClassName(request.getClassName());
        aClass.setCourseCatId(c.getCategoryId());
        aClass.setTeacherId(teacherId);
        aClass.setCampusId(teacher.getCampusId());
        aClass.setStatus(ClassStatus.IN_REVIEW.name());
        classMapper.insertSelective(aClass);

        if (CollectionUtils.isNotEmpty(request.getMemberIds())) {
            for (Long memberId : request.getMemberIds()) {
                BarablahClassMember classMember = new BarablahClassMember();
                classMember.setClassId(aClass.getId());
                classMember.setMemberId(memberId);
                classMember.setStatus(ClassStatus.IN_REVIEW.name());
                classMemberMapper.insertSelective(classMember);
            }
        }

        return new ApiEntity();
    }


    @ApiOperation(value = "获取我的班级信息")
    @GetMapping(value = "/members/classes")
    public ApiEntity<GetMemberClassResponse> getMemberClass(@RequestParam(required = true) String classid) {
        if (!authenticator.isAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();

        BarablahClassMemberExample example = new BarablahClassMemberExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andClassIdEqualTo(Long.valueOf(classid))
                .andDeletedEqualTo(Boolean.FALSE);
        List<BarablahClassMember> classes = classMemberMapper.selectByExample(example);

        if (classes==null || classes.size()==0) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "没有找到你所在的班级,或者你还不是该班的学员。");
        }

        GetMemberClassResponse response = null;
        if (CollectionUtils.isNotEmpty(classes)) {
            BarablahClass aClass = classMapper.selectByPrimaryKey(Long.valueOf(classid));
            if (aClass.getDeleted()) {
                return new ApiEntity(ApiStatus.STATUS_400.getCode(), "班级不存在或者已被取消,请重新进入。");
            }
            BarablahTeacher teacher = teacherMapper.selectByPrimaryKey(aClass.getTeacherId());
            BarablahTeacher englishTeacher = teacherMapper.selectByPrimaryKey(aClass.getEnglishTeacherId());
            response = new GetMemberClassResponse();
            response.setId(aClass.getId());
            response.setClassName(aClass.getClassName());
            response.setMonitor(aClass.getMonitor());
            response.setMonitorPhoneNumber(aClass.getMonitorPhoneNumber());
            response.setTeacher(teacher.getFullName());

            if (Objects.nonNull(englishTeacher)) {
                response.setEnglishTeacher(englishTeacher.getFullName());
            }

            BarablahClassMemberExample classMemberExample = new BarablahClassMemberExample();
            classMemberExample.createCriteria()
                    .andClassIdEqualTo(aClass.getId())
                    .andDeletedEqualTo(Boolean.FALSE);
            List<BarablahClassMember> classMembers = classMemberMapper.selectByExample(classMemberExample);

            for (BarablahClassMember classMember : classMembers) {
                BarablahMember member = memberMapper.selectByPrimaryKey(classMember.getMemberId());
                BarablahMemberPassportExample passportExample = new BarablahMemberPassportExample();
                passportExample.createCriteria()
                        .andMemberIdEqualTo(classMember.getMemberId())
                        .andProviderEqualTo(MemberPassportProvider.PHONE.name())
                        .andDeletedEqualTo(Boolean.FALSE);
                List<BarablahMemberPassport> passports = memberPassportMapper.selectByExample(passportExample);

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


    @ApiOperation(value = "获取我的班级信息")
    @GetMapping(value = "/members/classeslist")
    public ApiEntity<List<LessonDto>> getClasslist(
            @ApiParam(value = "课时类型（online线上，offline线下）") @RequestParam(defaultValue = "online") String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.isAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();

        List<ExtBarablahClass> list = classMapper.selectMyclasses(memberId);
        Date curDate = new Date();
        List<LessonDto> results = new ArrayList<>();
        Map<Long,List<ExtBarablahClass>> resultMap = new HashMap<>();

        for (ExtBarablahClass b:list) {
            if (resultMap.containsKey(b.getClassId())) {
                resultMap.get(b.getClassId()).add(b);
            } else {
                resultMap.put(b.getClassId(),new ArrayList<ExtBarablahClass>());
                resultMap.get(b.getClassId()).add(b);
            }
        }

        Collection<List<ExtBarablahClass>> c = resultMap.values();
        for(List<ExtBarablahClass> l:c) {
            if (l!=null && l.size()>0) {
                LessonDto dto = new LessonDto();
                Date startTime = l.get(0).getStartAt();
                dto.setId(l.get(0).getClassId());
                String status = l.get(0).getStatus();
                if (status.equals(BarablahClassStatusEnum.待开课.getValue())) {
                    dto.setStatus("a1");
                } else if (status.equals(BarablahClassStatusEnum.已开课.getValue())) {
                    dto.setStatus("a2");
                } else  {
                    dto.setStatus("a3");
                }
                dto.setName(l.get(0).getClassName());
                dto.setEndAt(l.get(0).getEndAt());
                dto.setStartAt(startTime);
                results.add(dto);
            }
        }
        //

        return new ApiEntity<>(results);
    }
}
