package com.barablah.web.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class GetMemberClassResponse {

    private Long id;

    private String teacher;

    private String englishTeacher;

    private String className;

    private String monitor;

    private String monitorPhoneNumber;

    private List<ClassMemberDto> members = Lists.newArrayList();

}
