package com.nisanth.sbendtoendapplication.registration.password;

import com.nisanth.sbendtoendapplication.user.User;

import java.util.Optional;

public interface IPasswordResetTokenService
{

    String validatePasswordResetToken(String theToken);

    Optional<User> findUserByPasswordResettoken(String theToken);

    void resetPassword(User theUser, String password);

    void createPasswordTokenForUser(User user, String passwordResetToken);
}
