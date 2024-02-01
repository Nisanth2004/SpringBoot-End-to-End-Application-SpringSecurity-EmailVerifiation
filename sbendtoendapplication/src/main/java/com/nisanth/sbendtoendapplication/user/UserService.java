package com.nisanth.sbendtoendapplication.user;

import com.nisanth.sbendtoendapplication.registration.RegistrationRequest;
import com.nisanth.sbendtoendapplication.registration.token.VerificationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;

    @Override
    public List<User> getAllUsers()
    {
        // get all users from the database
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest registartion)
    {
        var user=new User(registartion.getFirstName(),
                registartion.getLastName(),
                registartion.getEmail(),
                passwordEncoder.encode(registartion.getPassword()),
                Arrays.asList(new Role("ROLE_USER")));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email)
    {
        return Optional.ofNullable(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @Override
    public Optional<User> findById(Long id)
    {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public void updateUser(Long id, String firstName, String lastName, String email)
    {
        userRepository.update(firstName,lastName,email,id);

    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
       Optional<User> theUser= userRepository.findById(id);
       // chcek if user is present
        theUser.ifPresent((user->verificationTokenService.deleteUserToken(user.getId())));
        userRepository.deleteById(id);


    }
}
