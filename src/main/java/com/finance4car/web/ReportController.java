package com.finance4car.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance4car.Page;
import com.finance4car.Utils;
import com.finance4car.auth.Authenticator;
import com.finance4car.domain.*;
import com.finance4car.domain.json.Module;
import com.finance4car.repository.Finance4carMemberMapper;
import com.finance4car.repository.Finance4carModuleItemMapper;
import com.finance4car.repository.Finance4carModuleMapper;
import com.finance4car.repository.Finance4carReportMapper;
import com.finance4car.repository.example.Finance4carModuleExample;
import com.finance4car.repository.example.Finance4carModuleItemExample;
import com.finance4car.repository.example.Finance4carReportExample;
import com.finance4car.web.exception.ForbiddenException;
import com.finance4car.web.exception.InternalServerErrorException;
import com.finance4car.web.exception.NotFoundException;
import com.finance4car.web.exception.UnauthorizedException;
import com.finance4car.web.request.SubmitReportRequest;
import com.finance4car.web.response.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Api(tags = "报表", description = "报表接口")
@Slf4j
@RestController
public class ReportController {

    @Autowired
    private Finance4carModuleMapper moduleMapper;

    @Autowired
    private Finance4carModuleItemMapper moduleItemMapper;

    @Autowired
    private Finance4carReportMapper reportMapper;

    @Autowired
    private Finance4carMemberMapper memberMapper;

    @Autowired
    private Authenticator authenticator;

    private ObjectMapper objectMapper = new ObjectMapper();

    @ApiOperation(value = "获取报表结构定义")
    @GetMapping(value = "/reports/specification")
    public GetReportSpecificationsResponse getReportSpecification() {
        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long memberId = authenticator.getCurrentMemberId();
        Finance4carMember member = memberMapper.selectByPrimaryKey(memberId);

        Finance4carModuleExample moduleExample = new Finance4carModuleExample();
        moduleExample.createCriteria()
                .andEnabledEqualTo(Boolean.TRUE)
                .andDeletedEqualTo(Boolean.FALSE);
        moduleExample.setOrderByClause("position DESC");
        List<Finance4carModule> modules = moduleMapper.selectByExample(moduleExample);

        Finance4carModuleItemExample itemExample = new Finance4carModuleItemExample();
        itemExample.createCriteria()
                .andEnabledEqualTo(Boolean.TRUE)
                .andDeletedEqualTo(Boolean.FALSE);
        itemExample.setOrderByClause("position DESC");
        List<Finance4carModuleItem> items = moduleItemMapper.selectByExample(itemExample);

        GetReportSpecificationsResponse response = new GetReportSpecificationsResponse();

        for (Finance4carModule module : modules) {
            if (module.getMemberLevel().ordinal() > member.getLevel().ordinal()) {
                continue;
            }

            Map<String, List<ModuleItemDto>> itemByGroup = Maps.newLinkedHashMap();

            // 组装版块结构
            ModuleDto moduleDto = new ModuleDto();
            moduleDto.setId(module.getId());
            moduleDto.setName(module.getModuleName());

            for (Finance4carModuleItem item : items) {
                if (item.getModuleId().equals(module.getId())) {
                    ModuleItemDto itemDto = new ModuleItemDto();
                    itemDto.setId(item.getId());
                    itemDto.setUnit(item.getUnit());
                    itemDto.setName(item.getItemName());
                    itemDto.setDescription(item.getDescription());
                    itemDto.setFixedBudgetType(item.getFixedBudgetType().name());
                    itemDto.setFixedBudgetExpression(item.getFixedBudgetExpression());
                    itemDto.setFixedBudgetPercent(item.getFixedBudgetPercent());
                    itemDto.setFixedImplementationType(item.getFixedImplementationType().name());
                    itemDto.setFixedImplementationExpression(item.getFixedImplementationExpression());
                    itemDto.setFixedImplementationPercent(item.getFixedImplementationPercent());
                    itemDto.setVariableBudgetType(item.getVariableBudgetType().name());
                    itemDto.setVariableBudgetExpression(item.getVariableBudgetExpression());
                    itemDto.setVariableBudgetPercent(item.getVariableBudgetPercent());
                    itemDto.setVariableImplementationType(item.getVariableImplementationType().name());
                    itemDto.setVariableImplementationExpression(item.getVariableImplementationExpression());
                    itemDto.setVariableImplementationPercent(item.getVariableImplementationPercent());
                    itemDto.setTotalBudgetType(item.getTotalBudgetType().name());
                    itemDto.setTotalBudgetExpression(item.getTotalBudgetExpression());
                    itemDto.setTotalBudgetPercent(item.getTotalBudgetPercent());
                    itemDto.setTotalImplementationType(item.getTotalImplementationType().name());
                    itemDto.setTotalImplementationExpression(item.getTotalImplementationExpression());
                    itemDto.setTotalImplementationPercent(item.getTotalImplementationPercent());

                    List<ModuleItemDto> group = itemByGroup.get(item.getUnit());

                    if (Objects.isNull(group)) {
                        group = Lists.newArrayList();
                        itemByGroup.put(item.getUnit(), group);
                    }

                    group.add(itemDto);
                }
            }

            for (String key : itemByGroup.keySet()) {
                ModuleGroupDto groupDto = new ModuleGroupDto();
                groupDto.setName(key);
                groupDto.setItems(itemByGroup.get(key));
                moduleDto.getGroups().add(groupDto);
            }

            if (MemberLevel.GUEST.equals(module.getMemberLevel())) {
                response.getGuestModules().add(moduleDto);
            } else if (MemberLevel.SILVER.equals(module.getMemberLevel())) {
                response.getSilverModules().add(moduleDto);
            } else if (MemberLevel.GOLD.equals(module.getMemberLevel())) {
                response.getGoldModules().add(moduleDto);
            }
        }

        return response;
    }

    @ApiOperation(value = "获取我的提交报表列表")
    @GetMapping(value = "/reports")
    public Page<ReportDto> getReports(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long memberId = authenticator.getCurrentMemberId();

        Finance4carReportExample example = new Finance4carReportExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andMonthEqualTo(0)
                .andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("created_at DESC");
        List<Finance4carReport> reports = reportMapper.selectByExample(example);
        long total = reportMapper.countByExample(example);

        List<ReportDto> content = Lists.newArrayList();

        for (Finance4carReport report : reports) {
            ReportDto dto = new ReportDto();
            dto.setId(report.getId());
            dto.setName(report.getReportName());
            dto.setStartAt(report.getStartAt());
            dto.setEndAt(report.getEndAt());
            content.add(dto);
        }

        return new Page<>(content, page, size, total);
    }

    @ApiOperation(value = "获取我的月报列表")
    @GetMapping(value = "/reports/monthly")
    public Page<ReportDto> getReportsMonthly(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long memberId = authenticator.getCurrentMemberId();

        Finance4carReportExample example = new Finance4carReportExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andMonthGreaterThan(0)
                .andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow((page - 1) * size);
        example.setPageSize(size);
        example.setOrderByClause("created_at DESC");
        List<Finance4carReport> reports = reportMapper.selectByExample(example);
        long total = reportMapper.countByExample(example);

        List<ReportDto> content = Lists.newArrayList();

        for (Finance4carReport report : reports) {
            ReportDto dto = new ReportDto();
            dto.setId(report.getId());
            dto.setName(report.getReportName());
            dto.setMonth(report.getMonth());
            content.add(dto);
        }

        return new Page<>(content, page, size, total);
    }

    @ApiOperation(value = "获取报表详情")
    @GetMapping(value = "/reports/{id}")
    public GetReportResponse getReport(@PathVariable Long id) {
        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long memberId = authenticator.getCurrentMemberId();

        Finance4carReport report = reportMapper.selectByPrimaryKey(id);

        if (Objects.isNull(report)) {
            throw new NotFoundException("找不到报表, id=" + id);
        }

        if (!memberId.equals(report.getMemberId())) {
            throw new ForbiddenException("无权限访问");
        }

        GetReportResponse response = new GetReportResponse();
        response.setId(report.getId());
        response.setName(report.getReportName());
        response.setMonth(report.getMonth());
        response.setStartAt(report.getStartAt());
        response.setEndAt(report.getEndAt());
        response.setContent(report.getContent());
        return response;
    }

    @ApiOperation(value = "提交报表")
    @PostMapping(value = "/reports")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void submitReport(@Validated @RequestBody SubmitReportRequest body) {
        // 本月未提交，则从本月1号到当前日期作为周期
        // 本月已提交，则从本月提交的最后一天到当前日期作为周期
        // 提交之后合并数据到本月报表

        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long memberId = authenticator.getCurrentMemberId();

        Date startAt = null;
        Date endAt = null;

        Finance4carReportExample example = new Finance4carReportExample();
        example.createCriteria()
                .andMonthEqualTo(0)
                .andDeletedEqualTo(Boolean.FALSE);
        example.setStartRow(0);
        example.setPageSize(1);
        example.setOrderByClause("created_at DESC");
        List<Finance4carReport> reports = reportMapper.selectByExample(example);

        Finance4carReport reportToBeSave = new Finance4carReport();

        if (CollectionUtils.isEmpty(reports)) { // 第一次从本月开始计算
            endAt = new Date();
            startAt = Utils.getFirstDayOfMonth(endAt);
        } else {
            endAt = new Date();
            Finance4carReport latestReport = reports.get(0);

            if (DateUtils.isSameDay(latestReport.getEndAt(), endAt)) { // 当天已提交则更新
                startAt = latestReport.getStartAt();
                reportToBeSave.setId(latestReport.getId());
            } else if (Utils.isSameMonth(latestReport.getEndAt(), endAt)) { // 本月已提交
                startAt = DateUtils.addDays(latestReport.getEndAt(), 1);
            } else { // 本月未提交
                startAt = Utils.getFirstDayOfMonth(endAt);
            }
        }

        reportToBeSave.setMemberId(memberId);
        reportToBeSave.setStartAt(startAt);
        reportToBeSave.setEndAt(endAt);
        reportToBeSave.setReportName(body.getName());
        reportToBeSave.setContent(body.getContent());

        if (Objects.isNull(reportToBeSave.getId())) {
            reportMapper.insertSelective(reportToBeSave);
        } else {
            reportMapper.updateByPrimaryKeySelective(reportToBeSave);
        }

        try {
            makeMonthlyReport(memberId);
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    private void makeMonthlyReport(Long memberId) throws IOException {
        // 根据临时报告生成本月数据报表
        Date now = new Date();
        Finance4carReportExample example = new Finance4carReportExample();
        example.clear();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andMonthEqualTo(0)
                .andEndAtGreaterThan(Utils.getFirstDayOfMonth(now))
                .andDeletedEqualTo(Boolean.FALSE);
        List<Finance4carReport> reports = reportMapper.selectByExample(example);
        Map<Long, Module> moduleById = Maps.newHashMap();

        for (Finance4carReport report : reports) {
            List<Module> modules = objectMapper.readValue(report.getContent(), new TypeReference<List<Module>>() {
            });

            for (Module module : modules) {
                Module monthlyModule = moduleById.get(module.getId());

                if (Objects.isNull(monthlyModule)) {
                    monthlyModule = new Module();
                    moduleById.put(module.getId(), monthlyModule);
                }

                monthlyModule.merge(module);
            }
        }

        List<Module> monthlyModules = Lists.newArrayList(moduleById.values());
        String content = objectMapper.writeValueAsString(monthlyModules);
        int month = Integer.valueOf(DateFormatUtils.format(now, "yyyyMM"));

        example.clear();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andMonthEqualTo(month)
                .andDeletedEqualTo(Boolean.FALSE);
        List<Finance4carReport> monthlyReports = reportMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(monthlyReports)) {
            Finance4carReport reportToBeCreated = new Finance4carReport();
            reportToBeCreated.setMemberId(memberId);
            reportToBeCreated.setMonth(month);
            reportToBeCreated.setReportName(month + "财务报表");
            reportToBeCreated.setContent(content);
            reportMapper.insertSelective(reportToBeCreated);
        } else {
            Finance4carReport reportToBeUpdated = new Finance4carReport();
            reportToBeUpdated.setId(monthlyReports.get(0).getId());
            reportToBeUpdated.setContent(content);
            reportMapper.updateByPrimaryKeySelective(reportToBeUpdated);
        }
    }

}
