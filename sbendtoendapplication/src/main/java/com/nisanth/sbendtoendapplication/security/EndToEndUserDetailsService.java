package com.nisanth.sbendtoendapplication.security;

import com.nisanth.sbendtoendapplication.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EndToEndUserDetailsService implements UserDetailsService
{
    // get all users from the database to check login users
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).map(EndToEndUserDetails::new)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
    }
}
