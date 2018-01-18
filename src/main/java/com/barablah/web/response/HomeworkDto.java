package com.barablah.web.response;

import lombok.Data;

import java.util.Date;

@Data
public class HomeworkDto {

    private Long id;

    private String name;

    private String teacher;

    private String status;

    private Date closingAt;

}
