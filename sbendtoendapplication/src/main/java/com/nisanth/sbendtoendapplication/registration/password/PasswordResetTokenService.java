package com.nisanth.sbendtoendapplication.registration.password;

import com.nisanth.sbendtoendapplication.user.User;
import com.nisanth.sbendtoendapplication.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService implements IPasswordResetTokenService
{
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;




    @Override
    public String validatePasswordResetToken(String theToken)
    {
        // firstly whether the token is exist in database
        Optional<PasswordResetToken> passwordResetToken=passwordResetTokenRepository.findByToken(theToken);
        // if not dound in db
        if(passwordResetToken.isEmpty())
        {
            return "invalid";
        }
        // if token is found checj its expiration time
        Calendar calendar=Calendar.getInstance();
        if(passwordResetToken.get().getExpirationTime().getTime()-calendar.getTime().getTime()<=0)
        {
            return "expired";
        }

        return "valid";
    }

    @Override
    public Optional<User> findUserByPasswordResettoken(String theToken) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(theToken).get().getUser());
    }

    @Override
    public void resetPassword(User theUser, String newPassword)
    {
        // set the new password in database
        theUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(theUser);

    }

    @Override
    public void createPasswordTokenForUser(User user, String passwordResetToken)
    {
        PasswordResetToken resetToken=new PasswordResetToken(passwordResetToken,user);
        passwordResetTokenRepository.save(resetToken);

    }
}
