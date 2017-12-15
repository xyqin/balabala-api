package com.finance4car.web;

import com.finance4car.domain.Finance4carFeedback;
import com.finance4car.domain.Finance4carRegion;
import com.finance4car.repository.Finance4carFeedbackMapper;
import com.finance4car.repository.Finance4carRegionMapper;
import com.finance4car.repository.example.Finance4carRegionExample;
import com.finance4car.web.exception.BadRequestException;
import com.finance4car.web.exception.InternalServerErrorException;
import com.finance4car.web.request.SubmitFeedbackRequest;
import com.finance4car.web.response.RegionDto;
import com.finance4car.web.response.UploadImageResponse;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Api(tags = "通用", description = "通用接口")
@Slf4j
@RestController
public class CommonController {

    private static final String[] SUPPORTED_IMAGE_EXTENSIONS = {"jpg", "jpeg", "png", "gif"};

    private static final int MAX_IMAGE_SIZE_KB = 2048;

    @Value("${storage.local}")
    private String imageStorage;

    @Value("${storage.link}")
    private String imageLink;

    @Autowired
    private Finance4carRegionMapper regionMapper;

    @Autowired
    private Finance4carFeedbackMapper feedbackMapper;

    @ApiOperation(value = "获取地区列表")
    @GetMapping(value = "/regions")
    public List<RegionDto> getRegions() {
        Finance4carRegionExample example = new Finance4carRegionExample();
        example.createCriteria().andParentIdEqualTo(0L).andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("position DESC");
        List<Finance4carRegion> regions = regionMapper.selectByExample(example);

        List<RegionDto> response = Lists.newArrayList();

        for (Finance4carRegion region : regions) {
            RegionDto dto = new RegionDto();
            dto.setId(region.getId());
            dto.setName(region.getRegionName());
            response.add(dto);
        }

        return response;
    }

    @ApiOperation(value = "提交反馈")
    @PostMapping(value = "/feedbacks")
    public void submitFeedback(@Validated @RequestBody SubmitFeedbackRequest request) {
        Finance4carFeedback feedback = new Finance4carFeedback();
        feedback.setFamilyName(request.getFamilyName());
        feedback.setGivenName(request.getGivenName());
        feedback.setEmail(request.getEmail());
        feedback.setJob(request.getJob());
        feedback.setDepartment(request.getDepartment());
        feedback.setCompany(request.getCompany());
        feedback.setPhoneNumber(request.getPhoneNumber());
        feedback.setCountry(request.getCountry());
        feedback.setCity(request.getCity());
        feedback.setInformation(request.getInformation());
        feedbackMapper.insertSelective(feedback);
    }

    @ApiOperation(value = "上传图片")
    @PostMapping(value = "/images/upload")
    public UploadImageResponse uploadImage(@RequestParam("file") Part file) {
        int fileSizeInKB = BigInteger.valueOf(file.getSize()).divide(BigInteger.valueOf(1024)).intValue();
        String submittedFilename = file.getSubmittedFileName();

        if (!FilenameUtils.isExtension(submittedFilename, SUPPORTED_IMAGE_EXTENSIONS)) {
            throw new BadRequestException("不支持的文件类型");
        }

        if (fileSizeInKB > MAX_IMAGE_SIZE_KB) {
            throw new BadRequestException("文件大小不能超过" + MAX_IMAGE_SIZE_KB + "KB");
        }

        String currentDate = null;
        String filename = null;

        try {
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            currentDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
            String localPath = imageStorage + "/image/" + currentDate + "/";
            filename = DigestUtils.md5Hex(bytes) + "." + FilenameUtils.getExtension(submittedFilename);
            File local = new File(localPath + filename);
            FileUtils.writeByteArrayToFile(local, bytes);
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        String link = imageLink + "/image/" + currentDate + "/" + filename;
        UploadImageResponse response = new UploadImageResponse();
        response.setLink(link);
        return response;
    }

}
