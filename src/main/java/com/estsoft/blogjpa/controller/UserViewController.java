package com.estsoft.blogjpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

//    @PostMapping("/login")
//    public String submit() {
//        return "articleList";
//    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}
