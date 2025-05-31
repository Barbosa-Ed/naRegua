package br.com.kwave.naRegua.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/cadastroBarbearia")
    public String cadastroBarbearia() {
        return "cadastroBarbearia";
    }
}