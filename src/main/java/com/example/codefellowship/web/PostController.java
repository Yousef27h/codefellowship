package com.example.codefellowship.web;

import com.example.codefellowship.domain.ApplicationUser;
import com.example.codefellowship.domain.Post;
import com.example.codefellowship.infrastructure.PostRepository;
import com.example.codefellowship.infrastructure.services.ApplicationUserServiceImp;
import com.example.codefellowship.infrastructure.services.PostServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PostController {

    @Autowired
    PostServiceImp postServiceImp;

    @Autowired
    ApplicationUserServiceImp applicationUserServiceImp;

    /**
     * Add a post
     * @param post
     * @return redirect to posts page
     */
    @PostMapping("/posts")
    RedirectView addPost(@RequestParam String post){

        ApplicationUser applicationUser = applicationUserServiceImp.findAuthenticatedUser();

        postServiceImp.addNewPost(post, applicationUser);
        return new RedirectView("/posts");
    }

    /**
     * display all posts
     * @param model
     * @return html page to render posts
     */
    @GetMapping("/posts")
    public String getPostForm(Model model){

        ApplicationUser applicationUser = applicationUserServiceImp.findAuthenticatedUser();

        model.addAttribute("posts",applicationUser.getPosts());
        return "posts";
    }

}
