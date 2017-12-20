package com.balabala.web;

import com.balabala.web.response.GetLessonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "课程", description = "课程接口")
@RestController
public class LessonController {

    @ApiOperation(value = "获取课程回顾详情")
    @GetMapping(value = "/lessons/{id}")
    public GetLessonResponse getLesson(@RequestParam Long id) {

        return new GetLessonResponse();
    }

}
