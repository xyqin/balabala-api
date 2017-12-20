package com.balabala.service;

public interface VerificationService {

    String newVerificationCode(String phoneNumber);

    boolean verify(String phoneNumber, String code);

}
