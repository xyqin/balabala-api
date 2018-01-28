package com.barablah.web;

import com.barablah.domain.BarablahCampus;
import com.barablah.domain.BarablahPositionContent;
import com.barablah.domain.BarablahRegion;
import com.barablah.netease.NeteaseClient;
import com.barablah.netease.request.SmsSendCodeRequest;
import com.barablah.netease.response.SmsSendCodeResponse;
import com.barablah.repository.BarablahCampusMapper;
import com.barablah.repository.BarablahPositionContentMapper;
import com.barablah.repository.BarablahRegionMapper;
import com.barablah.repository.example.BarablahCampusExample;
import com.barablah.repository.example.BarablahPositionContentExample;
import com.barablah.repository.example.BarablahRegionExample;
import com.barablah.web.response.CampusDto;
import com.barablah.web.response.PositionContentDto;
import com.barablah.web.response.RegionDto;
import com.barablah.web.response.UploadResponse;
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

    private static final String[] SUPPORTED_VIDEO_EXTENSIONS = {"mp4", "flv"};

    private static final String[] SUPPORTED_AUDIO_EXTENSIONS = {"mp3", "caf", "wav"};

    private static final int MAX_IMAGE_SIZE_KB = 4096;

    @Value("${storage.local}")
    private String fileStorage;

    @Value("${storage.link}")
    private String fileLink;

    @Autowired
    private BarablahCampusMapper campusMapper;

    @Autowired
    private BarablahRegionMapper regionMapper;

    @Autowired
    private BarablahPositionContentMapper positionContentMapper;

    @Autowired
    private NeteaseClient neteaseClient;

    @ApiOperation(value = "获取手机验证码")
    @GetMapping(value = "/verifications/code")
    public ApiEntity newVerificationCode(@RequestParam String number) {
        SmsSendCodeRequest sendCodeRequest = new SmsSendCodeRequest();
        sendCodeRequest.setMobile(number);
        SmsSendCodeResponse sendCodeResponse = null;
        try {
            sendCodeResponse = neteaseClient.execute(sendCodeRequest);
        } catch (IOException e) {
            log.error("controller:verifications:code:调用网易云发送短信验证码失败", e);
            return new ApiEntity(ApiStatus.STATUS_500);
        }

        if (!sendCodeResponse.isSuccess()) {
            log.error("controller:verifications:code:调用网易云发送短信验证码失败, code=" + sendCodeResponse.getCode());
            return new ApiEntity(ApiStatus.STATUS_500);
        }

        return new ApiEntity();
    }

    @ApiOperation(value = "获取校区列表")
    @GetMapping(value = "/campuses")
    public ApiEntity<List<CampusDto>> getCampuses() {
        BarablahCampusExample example = new BarablahCampusExample();
        example.createCriteria().andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("created_at DESC");
        List<BarablahCampus> campuses = campusMapper.selectByExample(example);
        List<CampusDto> response = Lists.newArrayList();

        for (BarablahCampus campus : campuses) {
            CampusDto dto = new CampusDto();
            dto.setId(campus.getId());
            dto.setName(campus.getCampusName());
            response.add(dto);
        }

        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "获取地区列表")
    @GetMapping(value = "/regions")
    public ApiEntity<List<RegionDto>> getRegions() {
        BarablahRegionExample example = new BarablahRegionExample();
        example.createCriteria().andParentIdEqualTo(0L).andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("position DESC");
        List<BarablahRegion> regions = regionMapper.selectByExample(example);

        List<RegionDto> response = Lists.newArrayList();

        for (BarablahRegion region : regions) {
            RegionDto dto = new RegionDto();
            dto.setId(region.getId());
            dto.setName(region.getRegionName());
            response.add(dto);
        }

        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "上传图片文件")
    @PostMapping(value = "/storage/images")
    public ApiEntity<UploadResponse> uploadImage(@RequestParam("file") Part file) {
        int fileSizeInKB = BigInteger.valueOf(file.getSize()).divide(BigInteger.valueOf(1024)).intValue();
        String submittedFilename = file.getSubmittedFileName();

        if (!FilenameUtils.isExtension(submittedFilename, SUPPORTED_IMAGE_EXTENSIONS)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "不支持的文件类型");
        }

        if (fileSizeInKB > MAX_IMAGE_SIZE_KB) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "文件大小不能超过" + MAX_IMAGE_SIZE_KB + "KB");
        }

        String currentDate = null;
        String filename = null;

        try {
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            currentDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
            String localPath = fileStorage + "/image/" + currentDate + "/";
            filename = DigestUtils.md5Hex(bytes) + "." + FilenameUtils.getExtension(submittedFilename);
            File local = new File(localPath + filename);
            FileUtils.writeByteArrayToFile(local, bytes);
        } catch (IOException e) {
            log.error("上传图片失败", e);
            return new ApiEntity<>(ApiStatus.STATUS_500.getCode(), e.getMessage());
        }

        String link = fileLink + "/image/" + currentDate + "/" + filename;
        UploadResponse response = new UploadResponse();
        response.setLink(link);
        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "上传视频文件")
    @PostMapping(value = "/storage/videos")
    public ApiEntity<UploadResponse> uploadVideo(@RequestParam("file") Part file) {
        String submittedFilename = file.getSubmittedFileName();

        if (!FilenameUtils.isExtension(submittedFilename, SUPPORTED_VIDEO_EXTENSIONS)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "不支持的文件类型");
        }

        String currentDate = null;
        String filename = null;

        try {
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            currentDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
            String localPath = fileStorage + "/video/" + currentDate + "/";
            filename = DigestUtils.md5Hex(bytes) + "." + FilenameUtils.getExtension(submittedFilename);
            File local = new File(localPath + filename);
            FileUtils.writeByteArrayToFile(local, bytes);
        } catch (IOException e) {
            log.error("上传视频失败", e);
            return new ApiEntity(ApiStatus.STATUS_500.getCode(), e.getMessage());
        }

        String link = fileLink + "/video/" + currentDate + "/" + filename;
        UploadResponse response = new UploadResponse();
        response.setLink(link);
        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "上传录音文件")
    @PostMapping(value = "/storage/audios")
    public ApiEntity<UploadResponse> uploadAudio(@RequestParam("file") Part file) {
        String submittedFilename = file.getSubmittedFileName();

        if (!FilenameUtils.isExtension(submittedFilename, SUPPORTED_AUDIO_EXTENSIONS)) {
            return new ApiEntity(ApiStatus.STATUS_400.getCode(), "不支持的文件类型");
        }

        String currentDate = null;
        String filename = null;

        try {
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            currentDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
            String localPath = fileStorage + "/audio/" + currentDate + "/";
            filename = DigestUtils.md5Hex(bytes) + "." + FilenameUtils.getExtension(submittedFilename);
            File local = new File(localPath + filename);
            FileUtils.writeByteArrayToFile(local, bytes);
        } catch (IOException e) {
            log.error("上传录音失败", e);
            return new ApiEntity(ApiStatus.STATUS_500.getCode(), e.getMessage());
        }

        String link = fileLink + "/audio/" + currentDate + "/" + filename;
        UploadResponse response = new UploadResponse();
        response.setLink(link);
        return new ApiEntity<>(response);
    }

    @ApiOperation(value = "获取地区列表")
    @GetMapping(value = "/positions/{id}/contents")
    public ApiEntity<List<PositionContentDto>> getPositionContents(@PathVariable Long id) {
        Date now = new Date();
        BarablahPositionContentExample example = new BarablahPositionContentExample();
        example.createCriteria()
                .andPositionIdEqualTo(id)
                .andStartAtLessThan(now)
                .andEndAtGreaterThan(now)
                .andDeletedEqualTo(Boolean.FALSE);
        example.setOrderByClause("position DESC");
        List<BarablahPositionContent> contents = positionContentMapper.selectByExample(example);
        List<PositionContentDto> response = Lists.newArrayList();

        for (BarablahPositionContent content : contents) {
            PositionContentDto dto = new PositionContentDto();
            dto.setId(content.getId());
            dto.setName(content.getContentName());
            dto.setImage(content.getImage());
            dto.setLink(content.getLink());
            response.add(dto);
        }

        return new ApiEntity<>(response);
    }

}
