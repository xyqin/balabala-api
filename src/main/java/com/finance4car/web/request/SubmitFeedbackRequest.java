package com.finance4car.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SubmitFeedbackRequest {

    @NotNull
    private String familyName;

    @NotNull
    private String givenName;

    @NotNull
    private String email;

    @NotNull
    private String job;

    @NotNull
    private String department;

    @NotNull
    private String company;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String country;

    @NotNull
    private String city;

    @NotNull
    private String information;

}
