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

    // Lista as despesas e disponibiliza um objeto vazio para o formulário.
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("despesas", despesaRepository.findAll());
        model.addAttribute("despesa", new Despesa());

        return "despesas";
    }

    // Persiste os dados enviados pelo formulário de nova despesa.
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Despesa despesa) {
        despesaRepository.save(despesa);
        return "redirect:/despesas";
    }

    // Localiza a despesa pelo ID para preencher o formulário de edição.
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Despesa despesa = despesaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Despesa não encontrada"));

        model.addAttribute("despesa", despesa);
        return "editar-despesa";
    }

    // Atualiza a despesa existente no banco de dados.
    @PostMapping("/atualizar")
    public String atualizar(@ModelAttribute Despesa despesa) {
        despesaRepository.save(despesa);
        return "redirect:/despesas";
    }

    // Exclui a despesa informada e retorna à lista.
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        despesaRepository.deleteById(id);
        return "redirect:/despesas";
    }
}
