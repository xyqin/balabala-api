package com.barablah.web;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.barablah.Utils;
import com.barablah.domain.BarablahCampus;
import com.barablah.domain.BarablahPositionContent;
import com.barablah.domain.BarablahRegion;
import com.barablah.netease.NeteaseClient;
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
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation(value = "获取手机验证码")
    @GetMapping(value = "/verifications/code")
    public ApiEntity newVerificationCode(
            @ApiParam(required = true, value = "手机号") @RequestParam String number,
            @ApiParam(required = true, value = "验证码类型,注册SIGNUP|重置密码PASSWORD|绑定手机号BIND") @RequestParam String type) {
        String code = Utils.randomNumeric(6);

        try {
            boolean success = false;

            if ("SIGNUP".equals(type.toUpperCase())) {
                success = sendSmsOverAliyun(number, "SMS_119840074", code);
            } else if ("PASSWORD".equals(type.toUpperCase())) {
                success = sendSmsOverAliyun(number, "SMS_119840073", code);
            } else if ("BIND".equals(type.toUpperCase())) {
                success = sendSmsOverAliyun(number, "SMS_119840078", code);
            } else {
                return new ApiEntity(ApiStatus.STATUS_400.getCode(), "不合法的类型，type必须为SIGNUP|PASSWORD|BIND");
            }

            if (!success) {
                return new ApiEntity(ApiStatus.STATUS_500);
            }

            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            ops.set("verifications:code:" + code, number, 5, TimeUnit.MINUTES);
        } catch (ClientException e) {
            log.error("controller:verifications:code:发送短信验证码失败", e);
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

    private boolean sendSmsOverAliyun(String number, String template, String code) throws ClientException {
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //替换成你的AK
        final String accessKeyId = "LTAI3KgKAm8HGNfF";//你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = "gXtneBCmfNofpaxNOf4bAnqt0RGZL6";//你的accessKeySecret，参考本文档步骤2
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumbers(number);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("叭啦教育");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(template);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        System.out.println(sendSmsResponse.toString());
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            log.info("sms:aliyun:发送成功");
            return true;
        } else {
            log.warn("sms:aliyun:发送失败, code={}, msg={}", sendSmsResponse.getCode(), sendSmsResponse.getMessage());
            return false;
        }
    }

}
