package com.csye6220.finalProject.controller;

import com.csye6220.finalProject.dto.AuthResponse;
import com.csye6220.finalProject.dto.LoginRequest;
import com.csye6220.finalProject.dto.RegisterRequest;
import com.csye6220.finalProject.service.AuthService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        super();
        this.authService = authService;
    }

    @GetMapping("/showLogin")
    public String showLogin(){
        return "login";
    }

    @GetMapping("/showSignup")
    public String showSignup(){
        return "registration";
    }


    @PostMapping("/signup")
    public String signup(@RequestParam("email") String email, @RequestParam("username") String username, @RequestParam("password") String password, HttpSession session){
        RegisterRequest registerRequest = new RegisterRequest(email, username, password);
        authService.signup(registerRequest);
        session.setAttribute("registrationAlert", true);
        return "redirect:/api/auth/showLogin";
    }
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session){
        LoginRequest loginRequest = new LoginRequest(username, password);
        try{
            String token = authService.login(loginRequest);
            System.out.println(token);
            session.setAttribute("token", token);
            session.setAttribute("loginAlert", true);
            return "redirect:/home";
        }catch (Exception e){
            session.setAttribute("loginError", e.getMessage());
            return "redirect:/api/auth/showLogin";
        }

    }
    @PostMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("token");
        return "redirect:/home";
    }
}