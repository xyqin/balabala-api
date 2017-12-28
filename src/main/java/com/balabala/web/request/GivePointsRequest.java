package com.balabala.web.request;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class GivePointsRequest {

    @Min(1)
    private int points;

}
