package com.barablah.web;

import com.barablah.domain.*;
import com.barablah.repository.example.BarablahClassMemberExample;
import com.barablah.repository.example.BarablahMemberHomeworkExample;
import com.barablah.repository.example.BarablahMemberHomeworkItemExample;
import com.barablah.repository.example.BarablahMemberPointLogExample;
import com.barablah.web.enums.BarablahTextbookTypeEnum;
import com.barablah.web.enums.HomeworkStatusEnum;
import com.barablah.web.enums.PointLogObjectTypeEnum;
import com.barablah.web.request.SetHomeworksRequest;
import com.barablah.web.request.SubmitHomeworkRequest;
import com.barablah.web.response.HomeworkDto;
import com.barablah.web.response.HomeworkItemDto;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ling on 2018/3/26.
 */
@Slf4j
@Api(tags = "作业相关API", description = "作业相关API接口")
@RestController
public class HomeworkController extends BaseController {

    @ApiOperation(value = "发布作业")
    @PostMapping(value = "/teachers/homeworks")
    @Transactional
    public ApiEntity setHomeworks(@RequestBody SetHomeworksRequest request) {
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        long rd =  Long.valueOf(sf.format(request.getClosingAt()));
        long cd = Long.valueOf(sf.format(new Date()));
        if (rd<cd) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(),"作业完成日期已经过期,请重新设置!");
        }
        Long teacherId = authenticator.getCurrentTeacherId();
        // 创建教师作业数据
        BarablahTeacherHomework teacherHomework = new BarablahTeacherHomework();
        teacherHomework.setTeacherId(teacherId);
        teacherHomework.setHomeworkName(request.getName());
        teacherHomework.setClosingAt(request.getClosingAt());
        teacherHomework.setClassId(request.getClassId());
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
            homework.setClassId(request.getClassId());
            homework.setClosingAt(request.getClosingAt());
            homework.setScoreIcon(0);
            homework.setFinishedNums(0);
            //初始化都是待完成
            homework.setStatus(HomeworkStatusEnum.待完成.getValue());
            homework.setFinishedNums(0);
            memberHomeworkMapper.insertSelective(homework);
            for (Long textbookId : request.getTextbookIds()) {
                BarablahMemberHomeworkItem memberHomeworkItem = new BarablahMemberHomeworkItem();
                memberHomeworkItem.setHomeworkId(homework.getId());
                memberHomeworkItem.setMemberId(cMember.getMemberId());
                memberHomeworkItem.setTextbookId(textbookId);
                memberHomeworkItemMapper.insertSelective(memberHomeworkItem);
            }
        }
        return new ApiEntity();
    }



    @ApiOperation(value = "获取学生作业列表")
    @GetMapping(value = "/members/{id}/homeworks")
    @Transactional
    public ApiEntity<List<HomeworkDto>> getMemberHomeworks(
            @PathVariable Long id,
            @RequestParam long classId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long teacherId = authenticator.getCurrentTeacherId();
        BarablahTeacher teacher = teacherMapper.selectByPrimaryKey(teacherId);

        BarablahMemberHomeworkExample example = new BarablahMemberHomeworkExample();
        example.createCriteria()
                .andMemberIdEqualTo(id)
                .andClassIdEqualTo(classId)
                .andTeacherIdEqualTo(teacherId)
                .andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("created_at DESC");
        List<BarablahMemberHomework> homeworks = memberHomeworkMapper.selectByExample(example);
        List<HomeworkDto> response = Lists.newArrayList();

        for (BarablahMemberHomework homework : homeworks) {
            HomeworkDto dto = getAndModifyHomeWorkState(homework);
            dto.setTeacher(teacher.getFullName());
            response.add(dto);
        }
        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "提交作业")
    @PostMapping(value = "/members/homeworks/{id}/items")
    public ApiEntity submitHomework(
            @PathVariable Long id,
            @Valid @RequestBody SubmitHomeworkRequest request) {
        if (!authenticator.isAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        //保存学员的作业记录。
        Map<Long,BarablahMemberHomeworkItem> homeworkMap  = new HashMap<>();
        for (HomeworkItemDto item : request.getItems()) {
            BarablahMemberHomeworkItem itemToBeUpdated = new BarablahMemberHomeworkItem();
            itemToBeUpdated.setId(item.getId());
            itemToBeUpdated.setAnswer(item.getAnswer());
            memberHomeworkItemMapper.updateByPrimaryKeySelective(itemToBeUpdated);
            homeworkMap.put(item.getId(),itemToBeUpdated);
        }

        String status = "";

        //判断作业的完成状态---检查是否过期
        boolean isext = false;
        BarablahMemberHomework homework = memberHomeworkMapper.selectByPrimaryKey(id);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        long rd =  Long.valueOf(sf.format(homework.getClosingAt()));
        long cd = Long.valueOf(sf.format(new Date()));
        if (rd<cd) {
            isext = true;
        }

        //判断作业的完成状态---检查是否完成
        BarablahMemberHomeworkItemExample homeitems = new BarablahMemberHomeworkItemExample();
        homeitems.createCriteria().andHomeworkIdEqualTo(id);
        List<BarablahMemberHomeworkItem> homeworkitems = memberHomeworkItemMapper.selectByExample(homeitems);
        int i = 0;
        for(BarablahMemberHomeworkItem item:homeworkitems) {
            BarablahMemberHomeworkItem homeItem = homeworkMap.get(item.getId());
            if (homeItem!=null) {
                if (!StringUtils.isEmpty(homeItem.getAnswer())) {
                    i++;
                } else {
                    BarablahTextbook book = textbookMapper.selectByPrimaryKey(item.getId());
                    boolean need = BarablahTextbookTypeEnum.isNeed(book.getType().name());
                    if (!need) {
                        i++;
                    }
                }
            }
        }

        //设置作业的完成状态
        if (i==homeworkitems.size()) {
            if (isext) {
                status = HomeworkStatusEnum.延时完成.getValue();
            } else {
                status = HomeworkStatusEnum.已完成.getValue();
            }
        } else if (i>=0) {
            if (isext) {
                status = HomeworkStatusEnum.已超时.getValue();
            } else {
                status = HomeworkStatusEnum.待完成.getValue();
            }
        }
        BarablahMemberHomework homeworkToBeUpdated = new BarablahMemberHomework();
        homeworkToBeUpdated.setId(id);
        homeworkToBeUpdated.setFinishedNums(i);
        homeworkToBeUpdated.setStatus(status);
        memberHomeworkMapper.updateByPrimaryKeySelective(homeworkToBeUpdated);
        return new ApiEntity();
    }

    @ApiOperation(value = "获取我的作业列表")
    @GetMapping(value = "/members/homeworks")
    public ApiEntity<List<HomeworkDto>> getHomeworks(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.isAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        Long memberId = authenticator.getCurrentMemberId();
        BarablahMemberHomeworkExample example = new BarablahMemberHomeworkExample();
        example.createCriteria().andMemberIdEqualTo(memberId).andDeletedEqualTo(Boolean.FALSE);

        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("created_at DESC");
        List<BarablahMemberHomework> homeworks = memberHomeworkMapper.selectByExample(example);
        List<HomeworkDto> response = Lists.newArrayList();

        for (BarablahMemberHomework homework : homeworks) {

            HomeworkDto dto = getAndModifyHomeWorkState(homework);

            BarablahTeacher teacher = teacherMapper.selectByPrimaryKey(homework.getTeacherId());

            dto.setTeacher(teacher.getFullName());


            response.add(dto);
        }

        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "获取学生作业题目列表")
    @GetMapping(value = "/members/{memberId}/homeworks/{homeworkId}/items")
    public ApiEntity<List<HomeworkItemDto>> getMemberHomeworkItems(
            @PathVariable Long memberId,
            @PathVariable Long homeworkId) {
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }

        return getHomeworkItems(memberId,homeworkId);
    }

    @ApiOperation(value = "获取我的作业题目列表")
    @GetMapping(value = "/members/homeworks/{id}/items")
    public ApiEntity<List<HomeworkItemDto>> getHomeworkItems(@PathVariable Long id) {
        if (!authenticator.isAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }
        Long memberId = authenticator.getCurrentMemberId();
        return getHomeworkItems(memberId,id);
    }

    /**
     * 获取学员的作业答题内容
     * @param memberId
     * @param homeworkid
     * @return
     */
    public ApiEntity<List<HomeworkItemDto>> getHomeworkItems(Long memberId,Long homeworkid) {
        BarablahMemberHomeworkItemExample example = new BarablahMemberHomeworkItemExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andHomeworkIdEqualTo(homeworkid)
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

    /**
     * 处理作业状态
     * @param homework
     * @return
     */
    private HomeworkDto getAndModifyHomeWorkState(BarablahMemberHomework homework) {
        HomeworkDto dto = new HomeworkDto();
        dto.setClassName(classMapper.selectByPrimaryKey(homework.getClassId()).getClassName());

        dto.setId(homework.getId());
        dto.setContent(homework.getContent());
        if (homework.getScoreIcon()!=null) {
            dto.setScore(homework.getScoreIcon());
        }
        if (homework.getScoreIcon()!=null && homework.getScoreIcon()>0) {
            //获取积分
            BarablahMemberPointLogExample bmp = new BarablahMemberPointLogExample();
            bmp.createCriteria().andObjectIdEqualTo(homework.getId());
            List<BarablahMemberPointLog> points = memberPointLogMapper.selectByExample(bmp);
            if (points!=null && points.size()>0) {
                dto.setPoints(points.get(0).getPoints());
            } else {
                dto.setPoints(0);
            }
        }

        dto.setName(homework.getHomeworkName());
        if (homework.getStatus().equals(HomeworkStatusEnum.待完成.getValue())) {
            dto.setStatus(HomeworkStatusEnum.待完成.name());
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            long rd =  Long.valueOf(sf.format(homework.getClosingAt()));
            long cd = Long.valueOf(sf.format(new Date()));
            if (rd<cd) {
                dto.setStatus(HomeworkStatusEnum.已超时.name());
                //检查作业时,如果已经超时需要,变更成已经超时的状态.
                homework.setStatus(HomeworkStatusEnum.已超时.getValue());
                memberHomeworkMapper.updateByPrimaryKey(homework);
            }
            //已经做了
            if (homework.getFinishedNums()>0) {
                dto.setFinishStatus(1);
            } else {
                //还没有做
                dto.setFinishStatus(0);
            }
        } else if (homework.getStatus().equals(HomeworkStatusEnum.已完成.getValue())) {
            dto.setStatus(HomeworkStatusEnum.已完成.name());
            dto.setFinishStatus(2);
        } else if (homework.getStatus().equals(HomeworkStatusEnum.已超时.getValue())) {
            dto.setStatus(HomeworkStatusEnum.已超时.name());
            //已经做了
            if (homework.getFinishedNums()>0) {
                dto.setFinishStatus(1);
            } else {
                //还没有做
                dto.setFinishStatus(0);
            }
        } else if (homework.getStatus().equals(HomeworkStatusEnum.延时完成.getValue())) {
            dto.setStatus(HomeworkStatusEnum.延时完成.name());
            dto.setFinishStatus(2);
        }
        dto.setClosingAt(homework.getClosingAt());

        return dto;
    }


    @Transactional
    @ApiOperation(value = "检查学生作业,并打分")
    @PostMapping(value = "/homework/check")
    public ApiEntity correctHomework(@RequestBody CorrectHomeworkRequest request) {
        if (!authenticator.isTeacherAuthenticated()) {
            return new ApiEntity(ApiStatus.STATUS_401);
        }
        BarablahMemberHomework homework = memberHomeworkMapper.selectByPrimaryKey(request.getHomeworkId());

        if (homework.getScoreIcon()!=null && homework.getScoreIcon()>0) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(),"作业已经批改过!");
        }
        if (homework.getStatus().equals(HomeworkStatusEnum.待完成.getValue()) || homework.getStatus().equals(HomeworkStatusEnum.已超时.getValue())) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(),"作业完成后才可以批改!");
        }

        homework.setContent(request.getContent());
        homework.setScoreIcon(request.getScoreLevel());

        memberHomeworkMapper.updateByPrimaryKey(homework);

        if (request.getPoint()>0) {
            BarablahMemberPointLog log = new BarablahMemberPointLog();
            log.setMemberId(homework.getMemberId());
            log.setObjectId(homework.getId());
            log.setObjectType(PointLogObjectTypeEnum.作业.getValue());
            log.setPoints(request.getPoint());
            log.setType("");

             pointLogMapper.insert(log);
        }

        return new ApiEntity();
    }
}
