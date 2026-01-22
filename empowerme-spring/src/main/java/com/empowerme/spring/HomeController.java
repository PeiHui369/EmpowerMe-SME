package com.empowerme.spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "<h1>EmpowerMe is Running Successfully!</h1><p>Docker container verified.</p>";
    }
}
