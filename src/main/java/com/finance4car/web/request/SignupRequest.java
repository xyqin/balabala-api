package com.finance4car.web.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by xyqin on 2017/4/1.
 */
@Data
public class SignupRequest {

    @NotNull
    @Min(value = 1)
    private Long regionId;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String email;

    private String dealer;

    private String group;

    private String brand;

    private String postalAddress;

    private int hallArea;

    private Date openAt;

}
