package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RooterController {
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/form")
    public String form() {
        return "pages/form"; 
    }

    @GetMapping("/table")
    public String table() {
        return "pages/table";
    }

    @GetMapping("/log-out")
    public String logout() {
        return "pages/login";
    }

}
