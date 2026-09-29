package com.leonardo.controlefinanceiro.controller;

import com.leonardo.controlefinanceiro.model.Usuario;
import com.leonardo.controlefinanceiro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/cadastro")
    public String telaCadastro() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@ModelAttribute Usuario usuario, Model model) {

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

        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        if (usuario != null && usuario.getSenha().equals(senha)) {
            model.addAttribute("usuario", usuario);
            return "inicio";
        }

        model.addAttribute("erro", "E-mail ou senha inválidos.");
        return "login";
    }
}