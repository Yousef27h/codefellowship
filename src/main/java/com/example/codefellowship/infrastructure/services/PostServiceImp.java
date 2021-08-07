package com.example.codefellowship.infrastructure.services;

import com.example.codefellowship.domain.ApplicationUser;
import com.example.codefellowship.domain.Post;
import com.example.codefellowship.infrastructure.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImp implements PostService{

    @Autowired
    PostRepository postRepository;

    @Override
    public void addNewPost(String post, ApplicationUser applicationUser) {
        Post postNew = new Post(post);
        postNew.setApplicationUser(applicationUser);
        postRepository.save(postNew);
    }
}
