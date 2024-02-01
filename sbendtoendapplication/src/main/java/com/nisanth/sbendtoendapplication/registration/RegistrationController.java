package com.nisanth.sbendtoendapplication.registration;

import com.nisanth.sbendtoendapplication.event.RegistrationCompleteEvent;
import com.nisanth.sbendtoendapplication.event.listener.RegistrationCompleteEventListener;
import com.nisanth.sbendtoendapplication.registration.password.IPasswordResetTokenService;
import com.nisanth.sbendtoendapplication.registration.password.PasswordResetTokenService;
import com.nisanth.sbendtoendapplication.registration.token.VerificationToken;
import com.nisanth.sbendtoendapplication.registration.token.VerificationTokenRepository;
import com.nisanth.sbendtoendapplication.registration.token.VerificationTokenService;
import com.nisanth.sbendtoendapplication.user.IUserService;
import com.nisanth.sbendtoendapplication.user.User;
import com.nisanth.sbendtoendapplication.utility.UrlUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController
{
    private final IUserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenService tokenService;
    private final RegistrationCompleteEventListener eventListener;

    private final IPasswordResetTokenService passwordResetTokenService;

    // give default registration form
    @GetMapping("/registration-form")
    public String showRegistrationForm(Model model)
    {
        model.addAttribute("user",new RegistrationRequest());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationRequest registration,
                               HttpServletRequest request)
    {
        User user=userService.registerUser(registration);
        // publish the verification email event here
        publisher.publishEvent(new RegistrationCompleteEvent(user, UrlUtil.getApplicationUrl(request)));
        return "redirect:/registration/registration-form?success";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        Optional<VerificationToken> theToken = tokenService.findByToken(token);
        if (theToken.isPresent() && theToken.get().getUser().isEnabled()) {
            return "redirect:/login?verified";
        }
        String verificationResult = tokenService.validateToken(token);
        switch (verificationResult.toLowerCase()) {
            case "expired":
                return "redirect:/error?expired";
            case "valid":
                return "redirect:/login?valid";
            default:
                return "redirect:/error?invalid";
        }
    }
    @GetMapping("/forgot-password-request")
    public String forgotPassword()
    {
        return "forgot-password-form";
    }


    @PostMapping("/forgot-password")
    public String resetPasswordRequest(HttpServletRequest request,Model model)
    {
        // generate new token and save into the database
        String email=request.getParameter("email");

        // find the user by email
        Optional<User> user=userService.findByEmail(email);
        if(user==null)
        {
            return "redirect:/registration/forgot-password-request?not_found";
        }
        // if user is found -create a new token
        String passwordResetToken= UUID.randomUUID().toString();
        // save the token in database
        passwordResetTokenService.createPasswordTokenForUser(user.get(),passwordResetToken);
        // send password reset  verification email to user
        String url=UrlUtil.getApplicationUrl(request)+"/registration/password-reset-form?token="+passwordResetToken;
        try {
            eventListener.sendPasswordResetVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {

            model.addAttribute("error",e.getMessage());
        }
        // if email sent successfully
        return "redirect:/registration/forgot-password-request?success";

    }

    @GetMapping("/password-reset-form")
    public String passwordResetForm(@RequestParam("token") String token,Model model)
    {
        model.addAttribute("token",token);
        return "password-reset-form";
    }

    @PostMapping("reset-password")
    public String resetPassword(HttpServletRequest request)
    {
        // get token which is present in hidden field in password-reset-form
        String theToken=request.getParameter("token");
        String password=request.getParameter("password");

        // validate the reset token in passowrdtokenservice class
        String tokenVerificationResult=passwordResetTokenService.validatePasswordResetToken(theToken);
        if(!tokenVerificationResult.equalsIgnoreCase("valid"))
        {
            return "redirect:/error?invalid_token";
        }
        // otherwise extract the user
        Optional<User> theUser=passwordResetTokenService.findUserByPasswordResettoken(theToken);
        // if the user is fpund
        if(theUser.isPresent())
        {
            passwordResetTokenService.resetPassword(theUser.get(),password);
            return "redirect:/login?reset_success";
        }
     return "redirect:/error?not_found";

    }
}
