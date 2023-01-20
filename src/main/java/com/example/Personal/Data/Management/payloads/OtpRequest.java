package com.example.Personal.Data.Management.payloads;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OtpRequest {
    @NotEmpty
    private String Id;
}
