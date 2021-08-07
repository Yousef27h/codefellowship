package com.example.codefellowship.infrastructure.services;

import com.example.codefellowship.domain.ApplicationUser;

public interface PostService {

    void addNewPost(String post, ApplicationUser applicationUser);
}
