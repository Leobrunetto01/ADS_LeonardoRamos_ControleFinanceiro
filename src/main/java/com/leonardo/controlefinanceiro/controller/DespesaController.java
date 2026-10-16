
package com.leonardo.controlefinanceiro.controller;

import com.leonardo.controlefinanceiro.model.Despesa;
import com.leonardo.controlefinanceiro.repository.DespesaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/despesas")
public class DespesaController {

    private final DespesaRepository despesaRepository;

    public DespesaController(DespesaRepository despesaRepository) {
        this.despesaRepository = despesaRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("despesas", despesaRepository.findAll());
        model.addAttribute("despesa", new Despesa());

        return "despesas";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Despesa despesa) {
        despesaRepository.save(despesa);
        return "redirect:/despesas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Despesa despesa = despesaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Despesa não encontrada"));

        model.addAttribute("despesa", despesa);
        return "editar-despesa";
    }

    @PostMapping("/atualizar")
    public String atualizar(@ModelAttribute Despesa despesa) {
        despesaRepository.save(despesa);
        return "redirect:/despesas";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        despesaRepository.deleteById(id);
        return "redirect:/despesas";
    }
}
