package com.nisanth.sbendtoendapplication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class EndToEndSecurirtyDemo
{

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http.csrf(c -> c.disable())
                .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers("/login","/","/error","/registration/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())

                 .formLogin(formLogin ->
                         formLogin
                                 .loginPage("/login")
                                 .permitAll()
                                 .usernameParameter("email")
                                 .defaultSuccessUrl("/")
                 )
                .logout(logout->
                        logout.invalidateHttpSession(true)
                                .clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/"));
              return http.build();
    }
}
