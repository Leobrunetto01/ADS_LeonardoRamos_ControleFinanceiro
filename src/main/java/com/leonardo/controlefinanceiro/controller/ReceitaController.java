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

    // LISTAR RECEITAS
    @GetMapping
    public String listar(Model model) {

        model.addAttribute("receitas", receitaRepository.findAll());
        model.addAttribute("receita", new Receita());

        return "receitas";
    }

    // CADASTRAR OU ATUALIZAR RECEITA
    // Se o objeto possuir ID, atualiza uma receita existente; caso contrário, cria uma nova.
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Receita receita) {

        if (receita.getId() != null) {

            Receita existente = receitaRepository.findById(receita.getId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Receita não encontrada"));

            existente.setDescricao(receita.getDescricao());
            existente.setValor(receita.getValor());
            existente.setData(receita.getData());

            receitaRepository.save(existente);

        } else {

            receitaRepository.save(receita);
        }

        return "redirect:/receitas";
    }

    // ABRIR TELA DE EDIÇÃO
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Receita não encontrada"));

        model.addAttribute("receita", receita);

        return "editar-receita";
    }

    // EXCLUIR RECEITA
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {

        receitaRepository.deleteById(id);

        return "redirect:/receitas";
    }
}
