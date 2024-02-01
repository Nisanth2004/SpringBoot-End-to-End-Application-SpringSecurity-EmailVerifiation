package com.nisanth.sbendtoendapplication.registration;

import com.nisanth.sbendtoendapplication.user.Role;
import lombok.Data;

import java.util.List;

@Data
public class RegistrationRequest
{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Role> roles;
}
