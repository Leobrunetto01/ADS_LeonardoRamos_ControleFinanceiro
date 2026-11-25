package com.leonardo.controlefinanceiro.controller;

import com.leonardo.controlefinanceiro.model.Receita;
import com.leonardo.controlefinanceiro.model.Usuario;
import com.leonardo.controlefinanceiro.repository.ReceitaRepository;
import com.leonardo.controlefinanceiro.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/receitas")
public class ReceitaController {

    private final ReceitaRepository receitaRepository;
    private final UsuarioRepository usuarioRepository;

    public ReceitaController(
            ReceitaRepository receitaRepository,
            UsuarioRepository usuarioRepository) {

        this.receitaRepository = receitaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Identifica o usuário conectado ao sistema.
    private Usuario obterUsuario(HttpSession session) {

        Object usuarioId = session.getAttribute("usuarioId");

        if (!(usuarioId instanceof Long)) {
            return null;
        }

        return usuarioRepository.findById((Long) usuarioId)
                .orElse(null);
    }

    // LISTAR RECEITAS DO USUÁRIO
    @GetMapping
    public String listar(Model model, HttpSession session) {

        Usuario usuario = obterUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("receitas",
                receitaRepository.findByUsuarioId(usuario.getId()));

        model.addAttribute("receita", new Receita());

        return "receitas";
    }

    // CADASTRAR OU ATUALIZAR RECEITA
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Receita receita,
                         HttpSession session) {

        Usuario usuario = obterUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        if (receita.getId() != null) {

            // Verifica se a receita pertence ao usuário conectado.
            Receita existente = receitaRepository
                    .findByIdAndUsuarioId(
                            receita.getId(), usuario.getId())
                    .orElse(null);

            if (existente == null) {
                return "redirect:/receitas";
            }

            existente.setDescricao(receita.getDescricao());
            existente.setValor(receita.getValor());
            existente.setData(receita.getData());

            receitaRepository.save(existente);

        } else {

            // Vincula a nova receita ao usuário conectado.
            Receita nova = new Receita();

            nova.setDescricao(receita.getDescricao());
            nova.setValor(receita.getValor());
            nova.setData(receita.getData());
            nova.setUsuario(usuario);

            receitaRepository.save(nova);
        }

        return "redirect:/receitas";
    }

    // ABRIR TELA DE EDIÇÃO
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id,
                         Model model,
                         HttpSession session) {

        Usuario usuario = obterUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        // Impede a edição de receitas de outros usuários.
        Receita receita = receitaRepository
                .findByIdAndUsuarioId(id, usuario.getId())
                .orElse(null);

        if (receita == null) {
            return "redirect:/receitas";
        }

        model.addAttribute("receita", receita);

        return "editar-receita";
    }

    // EXCLUIR SOMENTE RECEITAS DO USUÁRIO UTILIZANDO POST
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id,
                          HttpSession session) {

        Usuario usuario = obterUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        // Exclui somente se a receita pertencer ao usuário conectado.
        receitaRepository
                .findByIdAndUsuarioId(id, usuario.getId())
                .ifPresent(receitaRepository::delete);

        return "redirect:/receitas";
    }
}