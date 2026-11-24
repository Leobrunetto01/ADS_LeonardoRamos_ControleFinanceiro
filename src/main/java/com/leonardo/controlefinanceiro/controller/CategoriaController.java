package com.leonardo.controlefinanceiro.controller;

import com.leonardo.controlefinanceiro.model.Categoria;
import com.leonardo.controlefinanceiro.repository.CategoriaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoriaController {

    // Repositório responsável pelas operações de banco de dados das categorias.
    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Lista todas as categorias e prepara um objeto vazio para o formulário.
    @GetMapping("/categorias")
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("categoria", new Categoria());
        return "categorias";
    }

    // Cadastra uma nova categoria recebida do formulário.
    @PostMapping("/categorias/salvar")
    public String salvar(@ModelAttribute Categoria categoria) {
        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }

    // Busca a categoria pelo identificador antes de abrir a tela de edição.
    @GetMapping("/categorias/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Categoria não encontrada"));

        model.addAttribute("categoria", categoria);
        return "editar-categoria";
    }

    // Salva as alterações da categoria já existente.
    @PostMapping("/categorias/atualizar")
    public String atualizar(@ModelAttribute Categoria categoria) {
        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }

    // Remove a categoria pelo identificador informado na URL.
    @GetMapping("/categorias/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        categoriaRepository.deleteById(id);
        return "redirect:/categorias";
    }
}
