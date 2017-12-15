package com.finance4car.web;

import com.finance4car.auth.Authenticator;
import com.finance4car.domain.Finance4carMember;
import com.finance4car.domain.Finance4carRegion;
import com.finance4car.domain.MemberLevel;
import com.finance4car.repository.Finance4carMemberMapper;
import com.finance4car.repository.Finance4carRegionMapper;
import com.finance4car.repository.example.Finance4carMemberExample;
import com.finance4car.web.exception.BadRequestException;
import com.finance4car.web.exception.ForbiddenException;
import com.finance4car.web.exception.UnauthorizedException;
import com.finance4car.web.request.SigninRequest;
import com.finance4car.web.request.SignupRequest;
import com.finance4car.web.request.UpdateMemberInfoRequest;
import com.finance4car.web.response.GetMemberResponse;
import com.finance4car.web.response.SigninResponse;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * Created by xyqin on 2017/4/1.
 */
@Api(tags = "会员", description = "会员、账号相关接口")
@Slf4j
@RestController
public class MemberController {

    @Autowired
    private Finance4carMemberMapper memberMapper;

    @Autowired
    private Finance4carRegionMapper regionMapper;

    @Autowired
    private Authenticator authenticator;

    /* 会员信息及账号相关接口 */

    @ApiOperation(value = "注册会员")
    @PostMapping(value = "/members/signup")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void signup(@Validated @RequestBody SignupRequest request) {
        Finance4carMemberExample example = new Finance4carMemberExample();
        example.createCriteria()
                .andPhoneNumberEqualTo(request.getPhoneNumber())
                .andDeletedEqualTo(Boolean.FALSE);
        List<Finance4carMember> members = memberMapper.selectByExample(example);

        if (CollectionUtils.isNotEmpty(members)) {
            throw new BadRequestException("手机号已被注册");
        }

        example.clear();
        example.createCriteria()
                .andEmailEqualTo(request.getEmail())
                .andDeletedEqualTo(Boolean.FALSE);
        members = memberMapper.selectByExample(example);

        if (CollectionUtils.isNotEmpty(members)) {
            throw new BadRequestException("邮箱地址已被注册");
        }

        Finance4carMember member = new Finance4carMember();
        member.setRegionId(request.getRegionId());
        member.setPhoneNumber(StringUtils.trim(request.getPhoneNumber()));
        member.setEmail(StringUtils.trim(request.getEmail()));
        member.setPassword(DigestUtils.md5Hex(StringUtils.trim(request.getPassword())));
        member.setMemberName(request.getName());
        member.setLevel(MemberLevel.GUEST);
        member.setDealer(request.getDealer());
        member.setUnderGroup(request.getGroup());
        member.setBrand(request.getBrand());
        member.setPostalAddress(request.getPostalAddress());
        member.setHallArea(request.getHallArea());
        member.setOpenAt(request.getOpenAt());
        memberMapper.insertSelective(member);

        // 往session中设置会员ID
        authenticator.newSession(member.getId());
    }

    @ApiOperation(value = "手机号登录")
    @PostMapping(value = "/members/signin")
    public SigninResponse signin(@Validated @RequestBody SigninRequest request) {
        List<Finance4carMember> members = Lists.newArrayList();
        Finance4carMemberExample example1 = new Finance4carMemberExample();
        example1.createCriteria().
                andPhoneNumberEqualTo(request.getUsername()).
                andDeletedEqualTo(Boolean.FALSE);
        members.addAll(memberMapper.selectByExample(example1));

        Finance4carMemberExample example2 = new Finance4carMemberExample();
        example2.createCriteria().
                andEmailEqualTo(request.getUsername()).
                andDeletedEqualTo(Boolean.FALSE);
        members.addAll(memberMapper.selectByExample(example2));

        if (CollectionUtils.isEmpty(members)) {
            throw new ForbiddenException("账号尚未注册");
        }

        Finance4carMember member = members.get(0);

        if (!DigestUtils.md5Hex(request.getPassword()).equals(member.getPassword())) {
            throw new UnauthorizedException("密码错误");
        }

        // 往session中设置会员ID
        authenticator.newSession(member.getId());

        SigninResponse response = new SigninResponse();
        response.setMemberId(member.getId());
        response.setMemberName(member.getMemberName());
        response.setPhoneNumber(member.getPhoneNumber());
        response.setEmail(member.getEmail());
        return response;
    }

    @ApiOperation(value = "获取会员信息")
    @GetMapping(value = "/members")
    public GetMemberResponse getMember() {
        if (!authenticator.authenticate()) {
            throw new UnauthorizedException("当前请求需要用户验证");
        }

        Long memberId = authenticator.getCurrentMemberId();
        Finance4carMember member = memberMapper.selectByPrimaryKey(memberId);
        Finance4carRegion region = regionMapper.selectByPrimaryKey(member.getRegionId());

        GetMemberResponse response = new GetMemberResponse();
        response.setId(member.getId());
        response.setName(member.getMemberName());
        response.setLevel(member.getLevel().name());
        response.setPhoneNumber(member.getPhoneNumber());
        response.setEmail(member.getEmail());
        response.setDealer(member.getDealer());
        response.setGroup(member.getUnderGroup());
        response.setBrand(member.getBrand());
        response.setPostalAddress(member.getPostalAddress());
        response.setHallArea(member.getHallArea());
        response.setOpenAt(member.getOpenAt());
        response.setRegionId(member.getRegionId());

        if (Objects.nonNull(region)) {
            response.setRegion(region.getRegionName());
        }

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
        Finance4carMember member = new Finance4carMember();
        member.setId(memberId);
        member.setRegionId(request.getRegionId());
        member.setMemberName(request.getName());
        member.setDealer(request.getDealer());
        member.setUnderGroup(request.getGroup());
        member.setBrand(request.getBrand());
        member.setPostalAddress(request.getPostalAddress());
        member.setHallArea(request.getHallArea());
        member.setOpenAt(request.getOpenAt());
        memberMapper.updateByPrimaryKeySelective(member);
    }

    @ApiOperation(value = "会员登出")
    @PostMapping(value = "/members/signout")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void signout() {
        authenticator.invalidateSession();
    }

}
