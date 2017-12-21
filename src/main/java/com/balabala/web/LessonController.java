package com.balabala.web;

import com.balabala.domain.BalabalaClass;
import com.balabala.domain.BalabalaClassLesson;
import com.balabala.domain.BalabalaTeacher;
import com.balabala.repository.BalabalaClassLessonMapper;
import com.balabala.repository.BalabalaClassMapper;
import com.balabala.repository.BalabalaTeacherMapper;
import com.balabala.web.response.GetLessonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "课程", description = "课程接口")
@RestController
public class LessonController {

    @Autowired
    private BalabalaClassLessonMapper lessonMapper;

    @Autowired
    private BalabalaClassMapper classMapper;

    @Autowired
    private BalabalaTeacherMapper teacherMapper;

    @ApiOperation(value = "获取课程回顾详情")
    @GetMapping(value = "/lessons/{id}")
    public GetLessonResponse getLesson(@RequestParam Long id) {
        BalabalaClassLesson lesson = lessonMapper.selectByPrimaryKey(id);
        BalabalaClass aClass = classMapper.selectByPrimaryKey(lesson.getClassId());
        BalabalaTeacher teacher = teacherMapper.selectByPrimaryKey(lesson.getTeacherId());

        GetLessonResponse response = new GetLessonResponse();
        response.setId(lesson.getId());
        response.setName(lesson.getLessonName());
        response.setClassName(aClass.getClassName());
        response.setTeacherName(teacher.getFullName());
        response.setDuration(100);
        response.setVideo("http://www.baidu.com");
        return response;
    }

}
