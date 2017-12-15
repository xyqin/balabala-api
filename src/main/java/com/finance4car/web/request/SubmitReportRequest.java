package com.finance4car.web.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class SubmitReportRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String content;

}
