package com.alpha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping(value = {"/", "/index.html", "/index"})
    public String getIndexPage() {
        return "/index";
    }
}
