package com.leonardo.controlefinanceiro.controller;

import com.leonardo.controlefinanceiro.model.Despesa;
import com.leonardo.controlefinanceiro.model.Usuario;
import com.leonardo.controlefinanceiro.repository.DespesaRepository;
import com.leonardo.controlefinanceiro.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/despesas")
public class DespesaController {

    private final DespesaRepository despesaRepository;
    private final UsuarioRepository usuarioRepository;

    public DespesaController(
            DespesaRepository despesaRepository,
            UsuarioRepository usuarioRepository) {

        this.despesaRepository = despesaRepository;
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

    // LISTAR DESPESAS DO USUÁRIO
    @GetMapping
    public String listar(Model model, HttpSession session) {

        Usuario usuario = obterUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        // Lista somente as despesas do usuário conectado.
        model.addAttribute("despesas",
                despesaRepository.findByUsuarioId(usuario.getId()));

        model.addAttribute("despesa", new Despesa());

        return "despesas";
    }

    // CADASTRAR NOVA DESPESA
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Despesa despesa,
                         HttpSession session) {

        Usuario usuario = obterUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        Despesa nova = new Despesa();

        nova.setDescricao(despesa.getDescricao());
        nova.setValor(despesa.getValor());
        nova.setData(despesa.getData());

        // Vincula a despesa ao usuário conectado.
        nova.setUsuario(usuario);

        despesaRepository.save(nova);

        return "redirect:/despesas";
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

        // Verifica se a despesa pertence ao usuário conectado.
        Despesa despesa = despesaRepository
                .findByIdAndUsuarioId(id, usuario.getId())
                .orElse(null);

        if (despesa == null) {
            return "redirect:/despesas";
        }

        model.addAttribute("despesa", despesa);

        return "editar-despesa";
    }

    // ATUALIZAR DESPESA DO USUÁRIO
    @PostMapping("/atualizar")
    public String atualizar(@ModelAttribute Despesa despesa,
                            HttpSession session) {

        Usuario usuario = obterUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        if (despesa.getId() == null) {
            return "redirect:/despesas";
        }

        // Impede a alteração de despesas de outros usuários.
        Despesa existente = despesaRepository
                .findByIdAndUsuarioId(
                        despesa.getId(), usuario.getId())
                .orElse(null);

        if (existente == null) {
            return "redirect:/despesas";
        }

        existente.setDescricao(despesa.getDescricao());
        existente.setValor(despesa.getValor());
        existente.setData(despesa.getData());

        despesaRepository.save(existente);

        return "redirect:/despesas";
    }

    // EXCLUIR SOMENTE DESPESAS DO USUÁRIO UTILIZANDO POST
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id,
                          HttpSession session) {

        Usuario usuario = obterUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        // Exclui somente se a despesa pertencer ao usuário conectado.
        despesaRepository
                .findByIdAndUsuarioId(id, usuario.getId())
                .ifPresent(despesaRepository::delete);

        return "redirect:/despesas";
    }
}