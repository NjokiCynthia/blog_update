package com.example.blogs.controller;


import com.example.blogs.exception.CustomException;
import com.example.blogs.mainClasses.Blog;
import com.example.blogs.mainClasses.User;
import com.example.blogs.repository.BlogRepository;
import com.example.blogs.token_util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.ElementCollection;

import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class BlogController {

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    com.example.blogs.repository.UserRespository UserRespository;

    User loggedInUser;

    private void middleWare(String authorization){
        //get jwt token from basic auth authorization (Bearer <token>)
        String token = authorization.substring(7).trim();



        List<User> users = UserRespository.findByEmail(email);
        if(users.size()==0){
            throw new IllegalArgumentException("User is not authorized");
        }
        User loggedInUser = users.get(0);

        //validate token
        if(!checkToken(loggedInUser, token)){
            throw new IllegalArgumentException("Token is not valid");
        }

        this.loggedInUser = loggedInUser;
    }

    private boolean checkToken(User user,String token){
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        return jwtTokenUtil.validateToken(token, user);
    }

    @GetMapping("/blog")
    @ElementCollection
    public List<Blog> index(){
        return blogRepository.findAll();
    }

    @GetMapping("/blog/{id}")
    public Blog show(@PathVariable String id){
        int blogId = Integer.parseInt(id);
        return  blogRepository.findById(blogId).get();
    }

    @PostMapping("/blog/get")
    public Blog getBlog(@RequestBody Map<String, String> body){
        int blogId = Integer.parseInt(body.get("id"));
        //log the variable body to the console
        System.out.println(body);
        return  blogRepository.findById(blogId).get();
    }

    @PostMapping("/blog/search")
    @ElementCollection
    public List<Blog> search(@RequestBody Map<String, String> body){
        String searchTerm = body.get("text");
        return blogRepository.findByTitleContainingOrContentContaining(searchTerm, searchTerm);
    }

    @PostMapping("/blog")
    public Blog create(@RequestBody Map<String, String> body){
        //read authentication token from headers
        String authorization = headers.get("authorization");

        //load middleware
        middleWare(authorization);


        String title = body.get("title");
        String content = body.get("content");
        int status = Integer.parseInt(body.get("status"));
        int priority = Integer.parseInt(body.get("priority"));
        int userId = Integer.parseInt(body.get("userId"));
        //set date to now

        Date modifiedOn = new Date();
        int modifiedBy = this.loggedInUser.getId();
        return blogRepository.save(new Blog(title, content,status,priority,userId,modifiedOn,modifiedBy));

    }

    @PutMapping("/blog/{id}")
    public Blog update(@PathVariable String id, @RequestBody Map<String, String> body){
        try {
            int blogId = Integer.parseInt(body.get("id"));
            // getting blog
            Blog task = blogRepository.findById(blogId).orElse(null);

            if (task == null) {
                throw new CustomException(404, "Blog not found");
            }

            // Check if specific fields are provided in the request body and update them
            if (body.containsKey("title")) {
                task.setTitle(body.get("title"));
            }

            if (body.containsKey("content")) {
                task.setContent(body.get("content"));
            }

            if (body.containsKey("status")) {
                int status = Integer.parseInt(body.get("status"));
                task.setStatus(status);
            }

            if (body.containsKey("priority")) {
                int priority = Integer.parseInt(body.get("priority"));
                task.setPriority(priority);
            }

            return blogRepository.save(task);
        } catch (NumberFormatException e) {
            throw new CustomException(400, "Invalid task ID format");
        } catch (Exception e) {
            throw new CustomException(500, "Internal Server error");
        }
    }

    @DeleteMapping("blog/{id}")
    public boolean delete(@PathVariable String id){
        int blogId = Integer.parseInt(id);
        blogRepository.deleteById(blogId);
        return true;
    }

    @PostMapping("/blog/get-all")
    @ElementCollection
    public List<Blog> getAll(){
        return blogRepository.findAll();
    }




    @PostMapping("blog/delete")
    public boolean deleteBlog(@RequestBody Map<String, String> body){
        int blogId = Integer.parseInt(body.get("id"));

        Blog blog = blogRepository.findById(blogId).orElse(null);

        if (blog == null) {
            throw new CustomException(404, "Task not found");
        } else {
            blogRepository.deleteById(blogId);
            return blog;
        }
    }

}