package com.xpressbox.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/devoluciones")
public class ReturnsController {

    @GetMapping("/returns")
    public String mostrarReturnsPage() {
        return "devoluciones/returns"; 
    }
}
