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
import org.springframework.web.bind.annotation.PathVariable;

import br.com.kwave.naRegua.models.Servico;
import br.com.kwave.naRegua.services.ServicoService;

@Controller
@RequestMapping("/negocios")
public class BarbeariaController {

    @Autowired
    private BarbeariaService barbeariaService;

    @Autowired
    private ServicoService servicoService;

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

    @GetMapping("/perfil/{id}")
    public String showPerfilBarbearia(@PathVariable String id, Model model) {
        Long barbeariaId;
        try {
            barbeariaId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return "redirect:/";
        }
        var barbeariaOptional = barbeariaService.findBarbeariaById(barbeariaId);
        if (barbeariaOptional.isEmpty()) {
            return "redirect:/";
        }
        var barbearia = barbeariaOptional.get();
        var servicos = servicoService.getServicosByBarbeariaId(barbeariaId);
        model.addAttribute("barbearia", barbearia);
        model.addAttribute("servicos", servicos);
        return "perfilBarbearia";
    }

    @GetMapping("/adicionarServico/{id}")
    public String showAdicionarServicoForm(@PathVariable Long id, Model model) {
        var barbeariaOptional = barbeariaService.findBarbeariaById(id);
        if (barbeariaOptional.isEmpty()) {
            return "redirect:/";
        }
        var barbearia = barbeariaOptional.get();
        model.addAttribute("barbearia", barbearia);
        model.addAttribute("servico", new Servico());
        return "adicionarServico";
    }

    @PostMapping("/adicionarServico/save")
    public String saveServico(@ModelAttribute Servico servico, RedirectAttributes redirectAttributes) {
        servicoService.saveServico(servico);
        redirectAttributes.addFlashAttribute("successMessage", "Serviço adicionado com sucesso!");
        return "redirect:/negocios/perfil/" + servico.getBarbearia().getId();
    }
}