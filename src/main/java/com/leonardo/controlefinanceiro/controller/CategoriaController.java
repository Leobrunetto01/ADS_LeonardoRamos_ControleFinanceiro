package com.leonardo.controlefinanceiro.controller;

import com.leonardo.controlefinanceiro.model.Categoria;
import com.leonardo.controlefinanceiro.model.Usuario;
import com.leonardo.controlefinanceiro.repository.CategoriaRepository;
import com.leonardo.controlefinanceiro.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public CategoriaController(
            CategoriaRepository categoriaRepository,
            UsuarioRepository usuarioRepository) {

        this.categoriaRepository = categoriaRepository;
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

    // LISTAR CATEGORIAS DO USUÁRIO
    @GetMapping("/categorias")
    public String listar(Model model, HttpSession session) {

        Usuario usuario = obterUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        // Lista somente as categorias do usuário conectado.
        model.addAttribute("categorias",
                categoriaRepository.findByUsuarioId(usuario.getId()));

        model.addAttribute("categoria", new Categoria());

        return "categorias";
    }

    // CADASTRAR NOVA CATEGORIA
    @PostMapping("/categorias/salvar")
    public String salvar(@ModelAttribute Categoria categoria,
                         HttpSession session) {

        Usuario usuario = obterUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        Categoria nova = new Categoria();

        nova.setNome(categoria.getNome());
        nova.setTipo(categoria.getTipo());

        // Vincula a categoria ao usuário conectado.
        nova.setUsuario(usuario);

        categoriaRepository.save(nova);

        return "redirect:/categorias";
    }

    // ABRIR TELA DE EDIÇÃO
    @GetMapping("/categorias/editar/{id}")
    public String editar(@PathVariable Long id,
                         Model model,
                         HttpSession session) {

        Usuario usuario = obterUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        // Verifica se a categoria pertence ao usuário conectado.
        Categoria categoria = categoriaRepository
                .findByIdAndUsuarioId(id, usuario.getId())
                .orElse(null);

        if (categoria == null) {
            return "redirect:/categorias";
        }

        model.addAttribute("categoria", categoria);

        return "editar-categoria";
    }

    // ATUALIZAR CATEGORIA DO USUÁRIO
    @PostMapping("/categorias/atualizar")
    public String atualizar(@ModelAttribute Categoria categoria,
                            HttpSession session) {

        Usuario usuario = obterUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        if (categoria.getId() == null) {
            return "redirect:/categorias";
        }

        // Impede a alteração de categorias de outros usuários.
        Categoria existente = categoriaRepository
                .findByIdAndUsuarioId(
                        categoria.getId(), usuario.getId())
                .orElse(null);

        if (existente == null) {
            return "redirect:/categorias";
        }

        existente.setNome(categoria.getNome());
        existente.setTipo(categoria.getTipo());

        categoriaRepository.save(existente);

        return "redirect:/categorias";
    }

    // EXCLUIR SOMENTE CATEGORIAS DO USUÁRIO UTILIZANDO POST
    @PostMapping("/categorias/excluir/{id}")
    public String excluir(@PathVariable Long id,
                          HttpSession session) {

        Usuario usuario = obterUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        // Exclui somente se a categoria pertencer ao usuário conectado.
        categoriaRepository
                .findByIdAndUsuarioId(id, usuario.getId())
                .ifPresent(categoriaRepository::delete);

        return "redirect:/categorias";
    }
}