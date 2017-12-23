package com.balabala.web;

import com.balabala.domain.BalabalaCampus;
import com.balabala.domain.BalabalaRegion;
import com.balabala.repository.BalabalaCampusMapper;
import com.balabala.repository.BalabalaRegionMapper;
import com.balabala.repository.example.BalabalaCampusExample;
import com.balabala.repository.example.BalabalaRegionExample;
import com.balabala.service.VerificationService;
import com.balabala.web.exception.BadRequestException;
import com.balabala.web.exception.InternalServerErrorException;
import com.balabala.web.response.CampusDto;
import com.balabala.web.response.RegionDto;
import com.balabala.web.response.UploadResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    private static final String[] SUPPORTED_IMAGE_EXTENSIONS = {"jpg", "jpeg"};

    private static final String[] SUPPORTED_VIDEO_EXTENSIONS = {"mp4", "flv"};

    private static final int MAX_IMAGE_SIZE_KB = 2048;

    @Value("${storage.local}")
    private String imageStorage;

    @Value("${storage.link}")
    private String imageLink;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private BalabalaCampusMapper campusMapper;

    @Autowired
    private BalabalaRegionMapper regionMapper;

    @ApiOperation(value = "获取校区列表")
    @GetMapping(value = "/campuses")
    public List<CampusDto> getCampuses() {
        BalabalaCampusExample example = new BalabalaCampusExample();
        example.createCriteria().andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("created_at DESC");
        List<BalabalaCampus> campuses = campusMapper.selectByExample(example);
        List<CampusDto> response = Lists.newArrayList();

        for (BalabalaCampus campus : campuses) {
            CampusDto dto = new CampusDto();
            dto.setId(campus.getId());
            dto.setName(campus.getCampusName());
            response.add(dto);
        }

        return response;
    }

    @ApiOperation(value = "获取手机验证码")
    @GetMapping(value = "/verifications/code")
    public void newVerificationCode(@RequestParam String number) {
        String code = verificationService.newVerificationCode(number);

        // TODO 发送手机验证码

    }

    @ApiOperation(value = "获取地区列表")
    @GetMapping(value = "/regions")
    public List<RegionDto> getRegions() {
        BalabalaRegionExample example = new BalabalaRegionExample();
        example.createCriteria().andParentIdEqualTo(0L).andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("position DESC");
        List<BalabalaRegion> regions = regionMapper.selectByExample(example);

        List<RegionDto> response = Lists.newArrayList();

        for (BalabalaRegion region : regions) {
            RegionDto dto = new RegionDto();
            dto.setId(region.getId());
            dto.setName(region.getRegionName());
            response.add(dto);
        }

        return response;
    }

    @ApiOperation(value = "上传图片文件")
    @PostMapping(value = "/storage/images")
    public UploadResponse uploadImage(@RequestParam("file") Part file) {
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
        UploadResponse response = new UploadResponse();
        response.setLink(link);
        return response;
    }

    @ApiOperation(value = "上传图片文件")
    @PostMapping(value = "/storage/videos")
    public UploadResponse uploadVideo(@RequestParam("file") Part file) {
        String submittedFilename = file.getSubmittedFileName();

        if (!FilenameUtils.isExtension(submittedFilename, SUPPORTED_VIDEO_EXTENSIONS)) {
            throw new BadRequestException("不支持的文件类型");
        }

        String currentDate = null;
        String filename = null;

        try {
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            currentDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
            String localPath = imageStorage + "/video/" + currentDate + "/";
            filename = DigestUtils.md5Hex(bytes) + "." + FilenameUtils.getExtension(submittedFilename);
            File local = new File(localPath + filename);
            FileUtils.writeByteArrayToFile(local, bytes);
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        String link = imageLink + "/video/" + currentDate + "/" + filename;
        UploadResponse response = new UploadResponse();
        response.setLink(link);
        return response;
    }

}
