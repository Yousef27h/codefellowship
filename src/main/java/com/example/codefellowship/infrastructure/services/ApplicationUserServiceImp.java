package com.example.codefellowship.infrastructure.services;

import com.example.codefellowship.domain.ApplicationUser;
import com.example.codefellowship.infrastructure.applicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ApplicationUserService")
public class ApplicationUserServiceImp implements ApplicationUserService{

    @Autowired
    com.example.codefellowship.infrastructure.applicationUserRepository applicationUserRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Override
    public ApplicationUser findAuthenticatedUser() {
        ApplicationUser principalUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser applicationUser = applicationUserRepository.findApplicationUserByUserName(principalUser.getUserName());
        return applicationUser;
    }

    @Override
    public ApplicationUser createNewUser(String userName, String firstName, String lastName, String dateOfBirth, String bio, String password) {
        ApplicationUser applicationUser = new ApplicationUser(userName, encoder.encode(password), firstName, lastName, dateOfBirth, bio);
        applicationUserRepository.save(applicationUser);
        return applicationUser;
    }

    @Override
    public List<ApplicationUser> getAllUsers() {
        return applicationUserRepository.findAll();
    }

    @Override
    public ApplicationUser getUserById(Long userId) {
        return applicationUserRepository.getById(userId);
    }

    @Override
    public ApplicationUser getUserByUsername(String username) {
        return applicationUserRepository.findApplicationUserByUserName(username);
    }

    @Override
    public ApplicationUser saveUser(ApplicationUser applicationUser) {
        return applicationUserRepository.save(applicationUser);
    }

}
