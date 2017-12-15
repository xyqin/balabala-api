package com.finance4car.web;

import com.finance4car.Page;
import com.finance4car.auth.Authenticator;
import com.finance4car.domain.Finance4carPlan;
import com.finance4car.domain.Finance4carPlanAction;
import com.finance4car.domain.Finance4carPlanProcess;
import com.finance4car.domain.PlanDepartment;
import com.finance4car.repository.Finance4carPlanActionMapper;
import com.finance4car.repository.Finance4carPlanMapper;
import com.finance4car.repository.Finance4carPlanProcessMapper;
import com.finance4car.repository.example.Finance4carPlanActionExample;
import com.finance4car.repository.example.Finance4carPlanExample;
import com.finance4car.repository.example.Finance4carPlanProcessExample;
import com.finance4car.web.exception.ForbiddenException;
import com.finance4car.web.exception.NotFoundException;
import com.finance4car.web.exception.UnauthorizedException;
import com.finance4car.web.request.ReadModifiedPlanRequest;
import com.finance4car.web.request.SavePlanActionsRequest;
import com.finance4car.web.request.SavePlanProcessesRequest;
import com.finance4car.web.request.SavePlanRequest;
import com.finance4car.web.response.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Api(tags = "方案", description = "方案接口")
@Slf4j
@RestController
public class PlanController {

    @Autowired
    private Finance4carPlanMapper planMapper;

    @Autowired
    private Finance4carPlanActionMapper actionMapper;

    @Autowired
    private Finance4carPlanProcessMapper processMapper;

    @Autowired
    private Authenticator authenticator;

    @ApiOperation(value = "获取方案列表")
    @GetMapping(value = "/plans")
    public Page<PlanDto> getPlans(
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long memberId = authenticator.getCurrentMemberId();

        Finance4carPlanExample example = new Finance4carPlanExample();

        if (StringUtils.isNotBlank(department)) {
            example.createCriteria()
                    .andMemberIdEqualTo(memberId)
                    .andDepartmentEqualTo(department)
                    .andDeletedEqualTo(Boolean.FALSE);
        } else {
            example.createCriteria()
                    .andMemberIdEqualTo(memberId)
                    .andDeletedEqualTo(Boolean.FALSE);
        }
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("created_at DESC");
        List<Finance4carPlan> plans = planMapper.selectByExample(example);
        long total = planMapper.countByExample(example);

        List<PlanDto> content = Lists.newArrayList();

        for (Finance4carPlan plan : plans) {
            PlanDto dto = new PlanDto();
            dto.setId(plan.getId());
            dto.setName(plan.getPlanName());
            dto.setProcessModified(plan.getProcessModified());
            dto.setActionModified(plan.getActionModified());
            dto.setCreatedAt(plan.getCreatedAt());
            content.add(dto);
        }

        return new Page<>(content, page, size, total);
    }

    @ApiOperation(value = "获取方案详情")
    @GetMapping(value = "/plans/{id}")
    public GetPlanResponse getPlan(@PathVariable Long id) {
        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long memberId = authenticator.getCurrentMemberId();

        Finance4carPlan plan = planMapper.selectByPrimaryKey(id);

        if (Objects.isNull(plan)) {
            throw new NotFoundException("找不到方案, id=" + id);
        }

        if (!memberId.equals(plan.getMemberId())) {
            throw new ForbiddenException("无权限访问");
        }

        Finance4carPlanActionExample actionExample = new Finance4carPlanActionExample();
        actionExample.createCriteria()
                .andPlanIdEqualTo(id)
                .andDeletedEqualTo(Boolean.FALSE);
        List<Finance4carPlanAction> actions = actionMapper.selectByExample(actionExample);

        Finance4carPlanProcessExample processExample = new Finance4carPlanProcessExample();
        processExample.createCriteria()
                .andPlanIdEqualTo(id)
                .andDeletedEqualTo(Boolean.FALSE);
        List<Finance4carPlanProcess> processes = processMapper.selectByExample(processExample);

        GetPlanResponse response = new GetPlanResponse();
        response.setId(plan.getId());
        response.setName(plan.getPlanName());
        response.setDescription(plan.getDescription());
        response.setCurrentGoal(plan.getCurrentGoal());
        response.setChallengeGoal(plan.getChallengeGoal());
        response.setSituation(plan.getSituation());
        response.setPromotion(plan.getPromotion());
        response.setGrossIncrease(plan.getGrossIncrease());
        response.setInCharge(plan.getInCharge());
        response.setContent(plan.getContent());
        response.setImages(plan.getImages());

        for (Finance4carPlanAction action : actions) {
            PlanActionDto dto = new PlanActionDto();
            dto.setId(action.getId());
            dto.setName(action.getActionName());
            dto.setDescription(action.getDescription());
            dto.setOutput(action.getOutput());
            dto.setCommunication(action.getCommunication());
            dto.setResource(action.getResource());
            dto.setInCharge(action.getInCharge());
            dto.setImplementAt(action.getImplementAt());
            dto.setImprovement(action.getImprovement());
            dto.setStartNote(action.getStartNote());
            response.getActions().add(dto);
        }

        for (Finance4carPlanProcess process : processes) {
            PlanProcessDto dto = new PlanProcessDto();
            dto.setDescription(process.getDescription());
            dto.setGrossIncrease(process.getGrossIncrease());
            dto.setFeedback(process.getFeedback());
            dto.setImprovement(process.getImprovement());
            dto.setWeek(process.getWeek());
            dto.setStartAt(process.getStartAt());
            dto.setEndAt(process.getEndAt());
            response.getProcesss().add(dto);
        }

        return response;
    }

    @ApiOperation(value = "保存方案")
    @PostMapping(value = "/plans")
    public SavePlanResponse savePlan(@RequestBody SavePlanRequest body) {
        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long memberId = authenticator.getCurrentMemberId();
        Finance4carPlan plan = new Finance4carPlan();
        plan.setId(body.getId());
        plan.setMemberId(memberId);
        plan.setPlanName(body.getName());
        plan.setDescription(body.getDescription());
        plan.setCurrentGoal(body.getCurrentGoal());
        plan.setChallengeGoal(body.getChallengeGoal());
        plan.setSituation(body.getSituation());
        plan.setPromotion(body.getPromotion());
        plan.setGrossIncrease(body.getGrossIncrease());
        plan.setInCharge(body.getInCharge());
        plan.setContent(body.getContent());
        plan.setImages(body.getImages());

        if (StringUtils.isNotBlank(body.getDepartment())) {
            plan.setDepartment(PlanDepartment.valueOf(body.getDepartment()));
        }

        if (plan.getId() == null) {
            planMapper.insertSelective(plan);
        } else {
            planMapper.updateByPrimaryKeySelective(plan);
        }

        SavePlanResponse response = new SavePlanResponse();
        response.setId(plan.getId());
        return response;
    }

    @ApiOperation(value = "保存方案执行步骤")
    @PostMapping(value = "/plans/{id}/actions")
    public SavePlanActionsResponse savePlanActions(
            @PathVariable Long id,
            @RequestBody SavePlanActionsRequest body) {
        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        SavePlanActionsResponse response = new SavePlanActionsResponse();

        for (PlanActionDto dto : body.getActions()) {
            Finance4carPlanAction action = new Finance4carPlanAction();
            action.setId(dto.getId());
            action.setPlanId(id);
            action.setActionName(dto.getName());
            action.setDescription(dto.getDescription());
            action.setOutput(dto.getOutput());
            action.setCommunication(dto.getCommunication());
            action.setResource(dto.getResource());
            action.setInCharge(dto.getInCharge());
            action.setImplementAt(dto.getImplementAt());
            action.setStartNote(dto.getStartNote());

            if (action.getId() == null) {
                actionMapper.insertSelective(action);
                dto.setId(action.getId());
            } else {
                actionMapper.updateByPrimaryKeySelective(action);
            }

            response.getActions().add(dto);
        }

        return response;
    }

    @ApiOperation(value = "保存方案执行过程")
    @PostMapping(value = "/plans/{id}/processes")
    public SavePlanProcessesResponse savePlanProcesses(
            @PathVariable Long id,
            @RequestBody SavePlanProcessesRequest body) {
        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        SavePlanProcessesResponse response = new SavePlanProcessesResponse();

        for (PlanProcessDto dto : body.getProcesses()) {
            Finance4carPlanProcess process = new Finance4carPlanProcess();
            process.setId(dto.getId());
            process.setPlanId(id);
            process.setDescription(dto.getDescription());
            process.setGrossIncrease(dto.getGrossIncrease());
            process.setFeedback(dto.getFeedback());
            process.setImprovement(dto.getImprovement());
            process.setWeek(dto.getWeek());
            process.setStartAt(dto.getStartAt());
            process.setEndAt(dto.getEndAt());

            if (process.getId() == null) {
                processMapper.insertSelective(process);
                dto.setId(process.getId());
            } else {
                processMapper.updateByPrimaryKeySelective(process);
            }

            response.getProcesses().add(dto);
        }

        return response;
    }

    @ApiOperation(value = "更新方案未读状态")
    @PostMapping(value = "/plans/{id}/read")
    public void readModifiedPlan(
            @PathVariable Long id,
            @RequestBody ReadModifiedPlanRequest request) {
        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long memberId = authenticator.getCurrentMemberId();
        Finance4carPlan plan = planMapper.selectByPrimaryKey(id);

        if (!Objects.equals(plan.getMemberId(), memberId)) {
            throw new ForbiddenException("无权限访问方案, id=" + id);
        }

        Finance4carPlan planToBeUpdated = new Finance4carPlan();
        planToBeUpdated.setId(plan.getId());
        planToBeUpdated.setProcessModified(request.getProcessModified());
        planToBeUpdated.setActionModified(request.getActionModified());
        planMapper.updateByPrimaryKeySelective(planToBeUpdated);
    }

}
