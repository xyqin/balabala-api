package com.barablah.web.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class ClassDto {

    private Long id;

    private String name;

    private String courseName;

    private List<ClassMemberDto> members = Lists.newArrayList();

    private List<LessonDto> lessons = Lists.newArrayList();

}