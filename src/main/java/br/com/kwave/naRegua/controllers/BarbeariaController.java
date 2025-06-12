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
import org.springframework.web.multipart.MultipartFile;

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
            return "redirect:/negocios/cadastroBarbearia";
        }

        barbeariaService.saveBarbearia(barbearia);
        redirectAttributes.addFlashAttribute("successMessage", "Barbearia cadastrada com sucesso! Faça login.");
        return "redirect:/negocios/loginBarbearia";
    }

    @GetMapping("/loginBarbearia")
    public String showLoginBarbeariaForm(Model model) {
        model.addAttribute("barbearia", new Barbearia());
        return "loginBarbearia";
    }

    @PostMapping("/loginBarbearia")
    public String loginBarbearia(@RequestParam String email, @RequestParam String senha,
                                 HttpSession session, RedirectAttributes redirectAttributes) {
        Barbearia barbearia = barbeariaService.authenticate(email, senha);

        if (barbearia != null) {
            session.setAttribute("loggedInBarbearia", barbearia);
            return "redirect:/negocios/perfil/" + barbearia.getId();
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Email ou senha inválidos.");
            return "redirect:/negocios/loginBarbearia";
        }
    }

    @GetMapping("/perfil/{id}")
    public String showPerfilBarbearia(@PathVariable String id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Long barbeariaId;
        try {
            barbeariaId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "ID da barbearia inválido.");
            return "redirect:/";
        }
        var barbeariaOptional = barbeariaService.findBarbeariaById(barbeariaId);
        if (barbeariaOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Barbearia não encontrada.");
            return "redirect:/";
        }
        var barbearia = barbeariaOptional.get();
        var servicos = servicoService.getServicosByBarbeariaId(barbeariaId);
        model.addAttribute("barbearia", barbearia);
        model.addAttribute("servicos", servicos);

        Barbearia loggedInBarbearia = (Barbearia) session.getAttribute("loggedInBarbearia");
        boolean isOwner = loggedInBarbearia != null && loggedInBarbearia.getId().equals(barbearia.getId());
        model.addAttribute("isOwner", isOwner);

        return "perfilBarbearia";
    }

    // Método para exibir o formulário de adicionar serviço
    @GetMapping("/adicionarServico/{barbeariaId}")
    public String showAdicionarServicoForm(@PathVariable Long barbeariaId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Barbearia loggedInBarbearia = (Barbearia) session.getAttribute("loggedInBarbearia");

        if (loggedInBarbearia == null || !loggedInBarbearia.getId().equals(barbeariaId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acesso negado. Faça login como a barbearia correta para adicionar serviços.");
            return "redirect:/negocios/loginBarbearia";
        }

        var barbeariaOptional = barbeariaService.findBarbeariaById(barbeariaId);
        if (barbeariaOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Barbearia não encontrada.");
            return "redirect:/";
        }

        Servico novoServico = new Servico();
        novoServico.setBarbearia(loggedInBarbearia);
        model.addAttribute("servico", novoServico);
        model.addAttribute("barbearia", loggedInBarbearia);
        return "adicionarServico";
    }

    // Método para salvar o serviço
    @PostMapping("/adicionarServico/save")
    public String saveServico(@ModelAttribute Servico servico, HttpSession session, RedirectAttributes redirectAttributes) {
        Barbearia loggedInBarbearia = (Barbearia) session.getAttribute("loggedInBarbearia");

        if (loggedInBarbearia == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Você precisa estar logado como uma barbearia para adicionar serviços.");
            return "redirect:/negocios/loginBarbearia";
        }

        servico.setBarbearia(loggedInBarbearia);

        servicoService.saveServico(servico);
        redirectAttributes.addFlashAttribute("successMessage", "Serviço adicionado com sucesso!");
        return "redirect:/negocios/perfil/" + loggedInBarbearia.getId();
    }


    @GetMapping("/logoutBarbearia")
    public String logoutBarbearia(HttpSession session) {
        session.removeAttribute("loggedInBarbearia");
        return "redirect:/negocios/loginBarbearia";
    }

    @PostMapping("/perfil/{id}/uploadFotoPerfil")
    public String uploadFotoPerfil(@PathVariable Long id,
                                   @RequestParam("file") MultipartFile file,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Barbearia loggedInBarbearia = (Barbearia) session.getAttribute("loggedInBarbearia");
        if (loggedInBarbearia == null || !loggedInBarbearia.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acesso negado para upload de foto de perfil.");
            return "redirect:/negocios/perfil/" + id;
        }

        try {
            String fileUrl = barbeariaService.saveFotoPerfil(file, id);
            loggedInBarbearia.setUrlFotoPerfil(fileUrl);
            session.setAttribute("loggedInBarbearia", loggedInBarbearia);
            redirectAttributes.addFlashAttribute("successMessage", "Foto de perfil atualizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao fazer upload da foto de perfil: " + e.getMessage());
        }
        return "redirect:/negocios/perfil/" + id;
    }

    @PostMapping("/perfil/{id}/uploadFotoCapa")
    public String uploadFotoCapa(@PathVariable Long id,
                                 @RequestParam("file") MultipartFile file,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Barbearia loggedInBarbearia = (Barbearia) session.getAttribute("loggedInBarbearia");
        if (loggedInBarbearia == null || !loggedInBarbearia.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acesso negado para upload de foto de capa.");
            return "redirect:/negocios/perfil/" + id;
        }

        try {
            String fileUrl = barbeariaService.saveFotoCapa(file, id);
            loggedInBarbearia.setUrlFotoCapa(fileUrl);
            session.setAttribute("loggedInBarbearia", loggedInBarbearia);
            redirectAttributes.addFlashAttribute("successMessage", "Foto de capa atualizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao fazer upload da foto de capa: " + e.getMessage());
        }
        return "redirect:/negocios/perfil/" + id;
    }
}