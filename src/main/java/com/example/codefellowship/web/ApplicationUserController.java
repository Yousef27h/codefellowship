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

    /**
     * Register a new user
     * @return signup html form
     */
    @GetMapping("/signup")
    public String getSignUpPage(){
        return "signup";
    }

    /**
     * get a form to login
     * @return login form page
     */
    @GetMapping("/login")
    public String getSignInPage(){ return "login"; }

    /**
     * display principal profile page
     * @param model
     * @return profile page with basic information about principal user
     */
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

    /**
     * Create new user in database and sign in the new user after signing up
     * @param userName
     * @param firstName
     * @param lastName
     * @param dateOfBirth
     * @param bio
     * @param password
     * @param user
     * @return redirect to home page
     * @throws ParseException
     */
    @PostMapping("/signup")
    RedirectView submitSignUpForm(String userName, String firstName, String lastName, String dateOfBirth, String bio, String password, @ModelAttribute("user") User user) throws ParseException {

        ApplicationUser applicationUser = applicationUserServiceImp.createNewUser(userName, password, firstName, lastName, dateOfBirth, bio);

        Authentication authentication = new UsernamePasswordAuthenticationToken(applicationUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new RedirectView("/");
    }

    /**
     * Get all users in database
     * @param model
     * @param principal
     * @return a page to render all user that were retrieved
     */
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

    /**
     * Add new followers to principal user's following list
     * @param userId
     * @return redirect to discover page
     */
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

    /**
     * remove a follower from principal user's following list
     * @param userId
     * @return discover page
     */
    @PostMapping("/unfollow")
    public RedirectView removeFollowing(@RequestParam Long userId){
        ApplicationUser principal = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser applicationUser = applicationUserServiceImp.getUserByUsername(principal.getUsername());
        ApplicationUser user = applicationUserServiceImp.getUserById(userId);
        applicationUser.removeFollowing(user);
        applicationUserServiceImp.saveUser(applicationUser);
        return new RedirectView("/discover");
    }

}
