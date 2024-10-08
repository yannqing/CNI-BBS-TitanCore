package com.titancore.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {
    private String username;
    private String password;
    private String rememberMe;
    private String loginType;

    private String phoneNumber;
    private String emailNumber;
    private String captchaType;//phone/email
    private String captchaCode;
}
