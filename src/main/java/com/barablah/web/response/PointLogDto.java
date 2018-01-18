package com.barablah.web.response;

import lombok.Data;

import java.util.Date;

@Data
public class PointLogDto {

    private Long id;

    private String type;

    private int points;

    private Date createdAt;

}
