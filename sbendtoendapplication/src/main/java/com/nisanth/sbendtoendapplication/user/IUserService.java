package com.nisanth.sbendtoendapplication.user;

import com.nisanth.sbendtoendapplication.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface IUserService
{
    List<User> getAllUsers();
    User registerUser(RegistrationRequest registrationRequest); // custom dto class
   Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    void updateUser(Long id, String firstName, String lastName, String email);

    void deleteUser(Long id);
}
