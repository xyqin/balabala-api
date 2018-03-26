package com.barablah.web;

import com.barablah.auth.Authenticator;
import com.barablah.netease.NeteaseClient;
import com.barablah.repository.*;
import com.barablah.wechat.mp.WxMpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by ling on 2018/3/26.
 */
public class BaseController {

    @Autowired
    public BarablahMemberPassportMapper passportMapper;

    @Autowired
    public BarablahMemberCommentMapper commentMapper;


    @Autowired
    public BarablahMemberPointLogMapper pointLogMapper;


    @Autowired
    public BarablahTeacherMajorMapper majorMapper;

    @Autowired
    public BarablahCountryMapper countryMapper;

    @Autowired
    public BarablahCourseMapper courseMapper;

    @Autowired
    public BarablahTeacherHomeworkMapper teacherHomeworkMapper;

    @Autowired
    public BarablahTeacherHomeworkItemMapper teacherHomeworkItemMapper;

    @Autowired
    public Authenticator authenticator;

    @Autowired
    public BarablahMemberMapper memberMapper;

    @Autowired
    public BarablahMemberLessonMapper memberLessonMapper;

    @Autowired
    public BarablahMemberPassportMapper memberPassportMapper;

    @Autowired
    public BarablahMemberHomeworkMapper memberHomeworkMapper;

    @Autowired
    public BarablahMemberHomeworkItemMapper memberHomeworkItemMapper;

    @Autowired
    public BarablahMemberCommentMapper memberCommentMapper;

    @Autowired
    public BarablahMemberPointLogMapper memberPointLogMapper;

    @Autowired
    public BarablahClassLessonMapper lessonMapper;

    @Autowired
    public BarablahTeacherMapper teacherMapper;

    @Autowired
    public BarablahClassMapper classMapper;

    @Autowired
    public BarablahClassMemberMapper classMemberMapper;

    @Autowired
    public BarablahCampusMapper campusMapper;

    @Autowired
    public BarablahTextbookMapper textbookMapper;

    @Autowired
    public BarablahPositionContentMapper positionContentMapper;

    @Autowired
    public NeteaseClient neteaseClient;

    @Autowired
    @Qualifier("wechatAppClient")
    public WxMpClient wxMpClient;

    @Autowired
    public StringRedisTemplate redisTemplate;
}
