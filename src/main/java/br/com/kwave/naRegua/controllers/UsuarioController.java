package br.com.kwave.naRegua.controllers;

import br.com.kwave.naRegua.models.Usuario;
import br.com.kwave.naRegua.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // --- Rotas de Autenticação e Cadastro ---

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @GetMapping("/cadastro")
    public String showRegisterForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        // FAZER VALIDAÇÃO
        usuarioService.saveUser(usuario);
        redirectAttributes.addFlashAttribute("successMessage", "Usuário cadastrado com sucesso! Faça login.");
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String senha, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.authenticate(email, senha);

        if (usuario != null) {
            session.setAttribute("loggedInUser", usuario);
            return "redirect:/index";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Email ou senha inválidos.");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // --- Rotas Protegidas (TEM QUE TESTAR) ---
    @GetMapping("/index")
    public String showIndexPage(HttpSession session, Model model) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "index";
    }
}