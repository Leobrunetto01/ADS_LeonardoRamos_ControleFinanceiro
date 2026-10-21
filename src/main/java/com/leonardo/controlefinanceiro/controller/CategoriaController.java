
package com.leonardo.controlefinanceiro.controller;

import com.leonardo.controlefinanceiro.model.Categoria;
import com.leonardo.controlefinanceiro.repository.CategoriaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping("/categorias")
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("categoria", new Categoria());
        return "categorias";
    }

    @PostMapping("/categorias/salvar")
    public String salvar(@ModelAttribute Categoria categoria) {
        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/categorias/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Categoria não encontrada"));

        model.addAttribute("categoria", categoria);
        return "editar-categoria";
    }

    @PostMapping("/categorias/atualizar")
    public String atualizar(@ModelAttribute Categoria categoria) {
        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/categorias/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        categoriaRepository.deleteById(id);
        return "redirect:/categorias";
    }
}
