package com.example.codefellowship.infrastructure.services;


import com.example.codefellowship.domain.ApplicationUser;

import java.util.List;

public interface ApplicationUserService {

    ApplicationUser findAuthenticatedUser();

    ApplicationUser createNewUser(String userName, String firstName, String lastName, String dateOfBirth, String bio, String password);

    List<ApplicationUser> getAllUsers();

    ApplicationUser getUserById(Long userId);

    ApplicationUser getUserByUsername(String username);

    ApplicationUser saveUser(ApplicationUser applicationUser);


}
