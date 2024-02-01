package com.nisanth.sbendtoendapplication.event;

import com.nisanth.sbendtoendapplication.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent
{
    private User user;
    private String confirmationUrl;

    public RegistrationCompleteEvent(User user, String confirmationUrl)
    {
        super(user);
       this.user=user;
       this.confirmationUrl=confirmationUrl;
    }
}
