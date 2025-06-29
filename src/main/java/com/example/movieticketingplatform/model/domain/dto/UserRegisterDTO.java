package com.example.movieticketingplatform.model.domain.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String email;
    private String password;
    private String phone;
    private String code;
    private String username;
}
