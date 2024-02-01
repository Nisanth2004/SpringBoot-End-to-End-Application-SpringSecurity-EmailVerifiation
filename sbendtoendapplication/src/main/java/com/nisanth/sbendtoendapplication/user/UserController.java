package com.nisanth.sbendtoendapplication.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController
{
private final IUserService userService;

// get all users from database
    @GetMapping
    public String getUsers(Model model)
    {
        model.addAttribute("users",userService.getAllUsers());
        return "users";
    }

    // edit the user
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model)
    {
        // find the user in database
        Optional<User> user=userService.findById(id);
        model.addAttribute("user",user.get());
        return "update-user";

    }

    // this method is updating the user
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id")Long id,User user)
    {
     userService.updateUser(id,user.getFirstName(),user.getLastName(),user.getEmail());
     return "redirect:/users?update_success";

    }

    // delete the user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id")Long id)
    {
        userService.deleteUser(id);
        return "redirect:/users?delete_success";
    }
}
