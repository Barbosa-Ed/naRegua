package br.com.kwave.naRegua.controllers;

import br.com.kwave.naRegua.services.BarbeariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private BarbeariaService barbeariaService;

    @GetMapping("/")
    public String index(Model model) {
        var barbearias = barbeariaService.getAllBarbearias();
        System.out.println("Quantidade de barbearias: " + barbearias.size());
        model.addAttribute("barbearias", barbearias);
        return "index";
    }

    @GetMapping("/cadastroBarbearia")
    public String cadastroBarbearia() {
        return "cadastroBarbearia";
    }
}
