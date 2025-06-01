package br.com.kwave.naRegua.controllers;

import br.com.kwave.naRegua.models.Barbearia;
import br.com.kwave.naRegua.services.BarbeariaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/negocios")
public class BarbeariaController {

    @Autowired
    private BarbeariaService barbeariaService;

    @GetMapping("/cadastroBarbearia")
    public String showRegisterBarbeariaForm(Model model) {
        model.addAttribute("barbearia", new Barbearia());
        return "cadastroBarbearia";
    }

    @PostMapping("/cadastroBarbearia/save")
    public String saveBarbearia(@ModelAttribute Barbearia barbearia, RedirectAttributes redirectAttributes) {

        if (barbeariaService.emailExists(barbearia.getEmail())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Este email já está cadastrado.");
            return "redirect:/cadastroBarbearia";
        }

        barbeariaService.saveBarbearia(barbearia);
        redirectAttributes.addFlashAttribute("successMessage", "Barbearia cadastrada com sucesso! Faça login.");
        return "redirect:/login";
    }

    //////////////////////////////////////////////////////////
    @GetMapping("/loginBarbearia")
    public String showLoginBarbeariaForm(Model model) {
        model.addAttribute("barbearia", new Barbearia());
        return "loginBarbearia";
    }
}