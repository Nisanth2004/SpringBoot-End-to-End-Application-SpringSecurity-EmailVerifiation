package com.nisanth.sbendtoendapplication.registration.token;

import com.nisanth.sbendtoendapplication.user.User;
import com.nisanth.sbendtoendapplication.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationTokenService implements IVerificationTokenService
{
    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;


    @Override
    public String validateToken(String token)
    {
        Optional<VerificationToken> theToken=tokenRepository.findByToken(token);
        if(theToken.isEmpty())
        {
            return "INVALID";
        }
        User user=theToken.get().getUser();
        // validate the token
        Calendar calendar=Calendar.getInstance();
        if((theToken.get().getExpirationTime().getTime()-calendar.getTime().getTime())<=0)
        {
            return "EXPIRED";
        }

        // If Token Not Expired
        user.setEnabled(true);
        userRepository.save(user);

    return "VALID";
    }

    @Override
    public void saveVerificationTokenForUser(User user, String token)
    {
         var verificationToken=new VerificationToken(token,user);
         // save the verification token
        tokenRepository.save(verificationToken);
    }

    @Override
    public Optional<VerificationToken> findByToken(String token)
    {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void deleteUserToken(Long id)
    {
           tokenRepository.deleteByUserId(id);
    }
}
