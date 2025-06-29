package com.example.movieticketingplatform.model.domain.dto;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String email;
    private String code;
    private String newPassword;
}
