package com.wexad.spring_security_refresh_token.controller;

import lombok.Getter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "Welcome to Spring Security Refresh Token";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "Admin";
    }
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String user() {
        return "User";
    }
}
