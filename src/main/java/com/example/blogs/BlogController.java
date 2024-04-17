package com.example.blogs;


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
    UserRespository UserRespository;

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
        return blogRespository.findAll();
    }

    @GetMapping("/blog/{id}")
    public Blog show(@PathVariable String id){
        int blogId = Integer.parseInt(id);
        return  blogRespository.findById(blogId).get();
    }

    @PostMapping("/blog/get")
    public Blog getBlog(@RequestBody Map<String, String> body){
        int blogId = Integer.parseInt(body.get("id"));
        //log the variable body to the console
        System.out.println(body);
        return  blogRespository.findById(blogId).get();
    }

    @PostMapping("/blog/search")
    @ElementCollection
    public List<Blog> search(@RequestBody Map<String, String> body){
        String searchTerm = body.get("text");
        return blogRespository.findByTitleContainingOrContentContaining(searchTerm, searchTerm);
    }

    @PostMapping("/blog")
    public Blog create(@RequestBody Map<String, String> body){
        String title = body.get("title");
        String content = body.get("content");
        return blogRespository.save(new Blog(title, content));
    }

    @PutMapping("/blog/{id}")
    public Blog update(@PathVariable String id, @RequestBody Map<String, String> body){
        int blogId = Integer.parseInt(id);
        // getting blog
        Blog blog = blogRespository.findById(blogId).get();
        blog.setTitle(body.get("title"));
        blog.setContent(body.get("content"));
        return blogRespository.save(blog);
    }

    @DeleteMapping("blog/{id}")
    public boolean delete(@PathVariable String id){
        int blogId = Integer.parseInt(id);
        blogRespository.deleteById(blogId);
        return true;
    }

    @PostMapping("/blog/get-all")
    @ElementCollection
    public List<Blog> getAll(){
        return blogRespository.findAll();
    }


    @PostMapping("/blog/create")
    public Blog createBlog(@RequestBody Map<String, String> body){
        String title = body.get("title");
        String content = body.get("content");
        return blogRespository.save(new Blog(title, content));
    }

    @PostMapping("/blog/update")
    public Blog updateBlog(@RequestBody Map<String, String> body){
        int blogId = Integer.parseInt(body.get("id"));
        // getting blog
        Blog blog = blogRespository.findById(blogId).get();
        blog.setTitle(body.get("title"));
        blog.setContent(body.get("content"));
        return blogRespository.save(blog);
    }

    @PostMapping("blog/delete")
    public boolean deleteBlog(@RequestBody Map<String, String> body){
        int blogId = Integer.parseInt(body.get("id"));
        blogRespository.deleteById(blogId);
        return true;
    }

}