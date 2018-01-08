package com.balabala.web.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class GivePointsRequest {

    @NotBlank
    private String expression;

}
