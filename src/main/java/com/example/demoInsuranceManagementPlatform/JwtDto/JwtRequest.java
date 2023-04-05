package com.example.demoInsuranceManagementPlatform.JwtDto;

import lombok.Data;

@Data
public class JwtRequest {
    private String clientEmail;
    private String clientPassword;
}
