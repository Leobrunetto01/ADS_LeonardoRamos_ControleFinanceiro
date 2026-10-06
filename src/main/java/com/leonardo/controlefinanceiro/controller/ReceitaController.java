package com.leonardo.controlefinanceiro.controller;

import com.leonardo.controlefinanceiro.model.Receita;
import com.leonardo.controlefinanceiro.repository.ReceitaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/receitas")
public class ReceitaController {

    private final ReceitaRepository receitaRepository;

    public ReceitaController(ReceitaRepository receitaRepository) {
        this.receitaRepository = receitaRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("receitas", receitaRepository.findAll());
        model.addAttribute("receita", new Receita());

        return "receitas";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Receita receita) {
        receitaRepository.save(receita);

        return "redirect:/receitas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Receita não encontrada"));

        model.addAttribute("receita", receita);

        return "editar-receita";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {

        receitaRepository.deleteById(id);

        return "redirect:/receitas";
    }
}