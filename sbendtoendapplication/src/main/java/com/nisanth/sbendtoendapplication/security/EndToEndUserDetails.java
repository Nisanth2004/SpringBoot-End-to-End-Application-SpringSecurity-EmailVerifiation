package com.nisanth.sbendtoendapplication.security;

import com.nisanth.sbendtoendapplication.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class EndToEndUserDetails implements UserDetails
{
    // get all users from the database to check login users

    private String userName;
    private String password;
    private boolean isEnabled;

    public EndToEndUserDetails(User user)
    {
         this.userName=user.getEmail();
         this.password=user.getPassword();
         this.isEnabled=user.isEnabled();
         this.authorities= Arrays.stream(user.getRoles()
                 .toString()
                 .split(","))
                 .map(SimpleGrantedAuthority::new)
                 .collect(Collectors.toList());
    }

    private List<GrantedAuthority> authorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
