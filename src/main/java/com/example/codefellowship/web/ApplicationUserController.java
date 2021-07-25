package com.example.codefellowship.web;

import com.example.codefellowship.domain.ApplicationUser;
import com.example.codefellowship.infrastructure.applicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ApplicationUserController {

    @Autowired
    applicationUserRepository applicationUserRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @GetMapping("/signup")
    public String getSignUpPage(){
        return "signup";
    }

    @GetMapping("/login")
    public String getSignInPage(){ return "login"; }

    @GetMapping("/test")
    public String getTestPage(){ return "test"; }


    @PostMapping("/signup")
    RedirectView submitSignUpForm(String userName, String firstName, String lastName, String dateOfBirth, String bio, String password) throws ParseException {
        Date DOB = new SimpleDateFormat("dd-MM-yyyy").parse(dateOfBirth);
        ApplicationUser applicationUser = new ApplicationUser(userName, encoder.encode(password), firstName, lastName, DOB, bio);
        applicationUserRepository.save(applicationUser);
        return new RedirectView("/");
    }

}
