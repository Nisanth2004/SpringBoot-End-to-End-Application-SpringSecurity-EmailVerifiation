package com.nisanth.sbendtoendapplication.registration.token;

import com.nisanth.sbendtoendapplication.user.User;
import jakarta.persistence.OneToOne;

import java.util.Optional;

public interface IVerificationTokenService
{
   String validateToken(String token);
   void saveVerificationTokenForUser(User user, String token);

   Optional<VerificationToken> findByToken(String token);

    void deleteUserToken(Long id);
}
