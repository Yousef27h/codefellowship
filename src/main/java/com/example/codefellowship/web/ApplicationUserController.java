package com.example.codefellowship.web;

import com.example.codefellowship.domain.ApplicationUser;
import com.example.codefellowship.infrastructure.services.ApplicationUserServiceImp;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ApplicationUserController {

    @Autowired
    ApplicationUserServiceImp applicationUserServiceImp;

    @Autowired
    BCryptPasswordEncoder encoder;

    @GetMapping("/signup")
    public String getSignUpPage(){
        return "signup";
    }

    @GetMapping("/login")
    public String getSignInPage(){ return "login"; }

    @GetMapping("/profile")
    public String getTestPage(Model model){

        ApplicationUser applicationUser = applicationUserServiceImp.findAuthenticatedUser();

        model.addAttribute("firstName", applicationUser.getFirstName());
        model.addAttribute("lastName", applicationUser.getLastName());
        model.addAttribute("dateOfBirth", applicationUser.getDateOfBirth());
        model.addAttribute("bio", applicationUser.getBio());
        model.addAttribute("posts", applicationUser.getPosts());
        return "profile";
    }


    @PostMapping("/signup")
    RedirectView submitSignUpForm(String userName, String firstName, String lastName, String dateOfBirth, String bio, String password, @ModelAttribute("user") User user) throws ParseException {

        ApplicationUser applicationUser = applicationUserServiceImp.createNewUser(userName, password, firstName, lastName, dateOfBirth, bio);

        Authentication authentication = new UsernamePasswordAuthenticationToken(applicationUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new RedirectView("/");
    }


    @GetMapping("/discover")
    public String getUsers(Model model, Principal principal){
        List<ApplicationUser> allUsers = applicationUserServiceImp.getAllUsers();
        ApplicationUser applicationUser = applicationUserServiceImp.getUserByUsername(principal.getName());
        allUsers.removeIf(user -> user == applicationUser);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("followings", applicationUser.getFollowing());
        System.out.println("principal following"+applicationUser.getFollowing());
        return "discover";
    }

    @PostMapping("/follow")
    public RedirectView addFollowing(@RequestParam Long userId){
        ApplicationUser principal = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser applicationUser = applicationUserServiceImp.getUserByUsername(principal.getUsername());
        ApplicationUser user = applicationUserServiceImp.getUserById(userId);
        System.out.println("user Id to follow"+userId);
        System.out.println("principal "+principal);
        applicationUser.addFollowing(user);
        applicationUserServiceImp.saveUser(applicationUser);
        return new RedirectView("/discover");
    }

    @PostMapping("/unfollow")
    public RedirectView removeFollowing(@RequestParam Long userId){
        ApplicationUser principal = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser applicationUser = applicationUserServiceImp.getUserByUsername(principal.getUsername());
        ApplicationUser user = applicationUserServiceImp.getUserById(userId);
        applicationUser.removeFollowing(user);
        applicationUserServiceImp.saveUser(applicationUser);
        return new RedirectView("/discover");
    }

//    @GetMapping("/discover/{userId}")
//    public String getUser(@PathVariable Long userId, Model model){
//        ApplicationUser user = applicationUserServiceImp.getUserById(userId);
//        ApplicationUser principal = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        principal.ge
//        model.addAttribute("user", user);
//        return "userProfile";
//    }
}
