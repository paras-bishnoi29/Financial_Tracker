package com.example.controller;

import com.example.service.*;

import jakarta.servlet.http.HttpSession;

import com.example.model.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    @PostMapping("/signup")
    public String signup(@RequestBody User user){
        return service.signup(user);
    }
    
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request, HttpSession session){
        return service.login(request, session);
    }

    @PostMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "Logout Succesfull";
    }
    
    @GetMapping("/about")
    public User getCurrentUser(HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");
        if(userId == null){
            return null;
        }
        return service.getUserById(userId);
    }

}
