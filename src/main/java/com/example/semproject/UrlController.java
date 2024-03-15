package com.example.semproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UrlController {

    private final UserService userService;

    // Constructor-based injection is recommended
    @Autowired
    public UrlController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public ModelAndView showRegisterPage() {
        return new ModelAndView("register.html");
    }

    @GetMapping("/home")
    public ModelAndView showHomePage() {
        return new ModelAndView("home.html");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String email, @RequestParam boolean isPremium) {
        try {
            userService.insertOrUpdateUser(username, password, email, isPremium);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
        try {
            if (userService.verifyUser(username, password)) {
                return ResponseEntity.ok("User verified successfully");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
        }
    }
}
