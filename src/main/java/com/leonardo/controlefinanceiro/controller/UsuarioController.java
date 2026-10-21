
package com.leonardo.controlefinanceiro.controller;

import com.leonardo.controlefinanceiro.model.Usuario;
import com.leonardo.controlefinanceiro.repository.UsuarioRepository;
import com.leonardo.controlefinanceiro.repository.ReceitaRepository;
import com.leonardo.controlefinanceiro.repository.DespesaRepository;

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

    @GetMapping("/cadastro")
    public String telaCadastro() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@ModelAttribute Usuario usuario,
                                   Model model) {

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            model.addAttribute("erro", "Este e-mail já está cadastrado.");
            return "cadastro";
        }

        usuarioRepository.save(usuario);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String telaLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String fazerLogin(@RequestParam String email,
                             @RequestParam String senha,
                             Model model) {

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElse(null);

        if (usuario != null && usuario.getSenha().equals(senha)) {
            return "redirect:/inicio?email=" +
                    java.net.URLEncoder.encode(
                            usuario.getEmail(),
                            java.nio.charset.StandardCharsets.UTF_8);
        }

        model.addAttribute("erro", "E-mail ou senha inválidos.");
        return "login";
    }

    @GetMapping("/inicio")
    public String inicio(@RequestParam(required = false) String email,
                         Model model) {

        if (email == null || email.isBlank()) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        if (usuario == null) {
            return "redirect:/login";
        }

        // Calcular o resumo financeiro
        Double totalReceitas = receitaRepository.calcularTotalReceitas();
        Double totalDespesas = despesaRepository.calcularTotalDespesas();
        Double saldo = totalReceitas - totalDespesas;

        // Enviar os valores para a página inicial
        model.addAttribute("usuario", usuario);
        model.addAttribute("totalReceitas", totalReceitas);
        model.addAttribute("totalDespesas", totalDespesas);
        model.addAttribute("saldo", saldo);

        return "inicio";
    }
}
