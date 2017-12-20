package com.balabala.web.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class NewLessonResponse {

    private Long id;

    private String name;

    private String teacherName;

    private String teacherUsername;

    private List<ClassMemberDto> members = Lists.newArrayList();

}
