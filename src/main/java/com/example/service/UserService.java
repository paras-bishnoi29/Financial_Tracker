package com.example.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.LoginRequest;
import com.example.model.User;
import com.example.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    
    public UserService(UserRepository repository, BCryptPasswordEncoder encoder){
        this.repository = repository;
        this.encoder = encoder;
    }

    public String signup(User user){
        User existingUser = repository.findByEmail(user.getEmail());

        if(existingUser != null){
            return "User Already Exists, Login Now";
        }
        
        user.setPassword(encoder.encode(user.getPassword()));

        repository.save(user);

        return "Signup succesfull";

    }

    public String login(LoginRequest request, HttpSession session){
        User user = repository.findByEmail(request.getUsermail());
        if(user == null){
            return "User Not Found";
        }
        boolean matched = encoder.matches(request.getPassword(), user.getPassword());
        if(!matched){
            return "Invalid password";
        }
        session.setAttribute("userId", user.getId());
        return "Login Succesfull";
    }

    public User getUserById(int userId) {
        return repository.findById(userId);
}

}
