package com.finance4car.web.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by xyqin on 2017/6/2.
 */
@Data
public class SigninRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
