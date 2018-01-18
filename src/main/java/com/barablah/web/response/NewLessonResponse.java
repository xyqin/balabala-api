package com.barablah.web.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class NewLessonResponse {

    private Long id;

    private String name;

    private String teacherName;

    private String teacherUsername;

    private Date startAt;

    private Date endAt;

    private String accid;

    private String token;

    private String room;

    private Date timestamp = new Date();

    private List<ClassMemberDto> members = Lists.newArrayList();

    private List<ClassMemberDto> probationalMembers = Lists.newArrayList();

}
