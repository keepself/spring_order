package com.encore.ordering.member.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {
    @NotEmpty(message = "Email Is Essential")
    @Email(message = "Email Is Not Valid")
    private String email;
    @NotEmpty(message = "Password Is Essential")
    @Size(min = 4, message = "Minimum Length Is 4")
    private String password;
}
