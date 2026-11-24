package com.leonardo.controlefinanceiro.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonardo.controlefinanceiro.model.Usuario;
import com.leonardo.controlefinanceiro.repository.UsuarioRepository;
import com.leonardo.controlefinanceiro.repository.ReceitaRepository;
import com.leonardo.controlefinanceiro.repository.DespesaRepository;
import com.leonardo.controlefinanceiro.service.CotacaoService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private CotacaoService cotacaoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/cadastro")
    public String telaCadastro() {
        return "cadastro";
    }

    // Impede o cadastro de dois usuários com o mesmo e-mail.
    @PostMapping("/cadastro")
    public String cadastrarUsuario(
            @ModelAttribute Usuario usuario,
            Model model) {

        if (usuarioRepository.findByEmail(
                usuario.getEmail()).isPresent()) {

            model.addAttribute(
                "erro", "Este e-mail já está cadastrado."
            );
            return "cadastro";
        }

        usuarioRepository.save(usuario);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String telaLogin() {
        return "login";
    }

    // Valida as credenciais e cria a sessão do usuário autenticado.
    @PostMapping("/login")
    public String fazerLogin(
            @RequestParam String email,
            @RequestParam String senha,
            HttpSession session,
            Model model) {

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElse(null);

        if (usuario != null &&
                usuario.getSenha().equals(senha)) {

            session.setAttribute(
                "usuarioId", usuario.getId()
            );

            return "redirect:/inicio";
        }

        model.addAttribute(
            "erro", "E-mail ou senha inválidos."
        );
        return "login";
    }

    // Página inicial protegida: exige que exista um usuário autenticado na sessão.
    @GetMapping("/inicio")
    public String inicio(HttpSession session, Model model) {

        Object usuarioId = session.getAttribute("usuarioId");

        if (!(usuarioId instanceof Long)) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioRepository
                .findById((Long) usuarioId)
                .orElse(null);

        if (usuario == null) {
            session.invalidate();
            return "redirect:/login";
        }

        // Calcula o resumo financeiro usado no painel inicial.
        Double totalReceitas =
                receitaRepository.calcularTotalReceitas();

        Double totalDespesas =
                despesaRepository.calcularTotalDespesas();

        if (totalReceitas == null) {
            totalReceitas = 0.0;
        }

        if (totalDespesas == null) {
            totalDespesas = 0.0;
        }

        Double saldo = totalReceitas - totalDespesas;

        model.addAttribute("usuario", usuario);
        model.addAttribute("totalReceitas", totalReceitas);
        model.addAttribute("totalDespesas", totalDespesas);
        model.addAttribute("saldo", saldo);

        // Integração com a AwesomeAPI
        try {
            String resposta = cotacaoService.buscarCotacoes();

            JsonNode json = objectMapper.readTree(resposta);

            String dolar = json.path("USDBRL")
                    .path("bid").asText();

            String euro = json.path("EURBRL")
                    .path("bid").asText();

            model.addAttribute("dolar", dolar);
            model.addAttribute("euro", euro);

        } catch (Exception e) {

            // Mantém a tela funcionando se a API falhar
            model.addAttribute("dolar", "Indisponível");
            model.addAttribute("euro", "Indisponível");
        }

        return "inicio";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
