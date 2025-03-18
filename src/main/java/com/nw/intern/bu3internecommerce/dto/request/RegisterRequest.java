package com.nw.intern.bu3internecommerce.dto.request;

import lombok.Data;

import java.util.Date;
@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Date dateOfBirth;
}
