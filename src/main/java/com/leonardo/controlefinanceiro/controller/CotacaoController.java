package com.leonardo.controlefinanceiro.controller;

import com.leonardo.controlefinanceiro.service.CotacaoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CotacaoController {

    private final CotacaoService cotacaoService;

    public CotacaoController(CotacaoService cotacaoService) {
        this.cotacaoService = cotacaoService;
    }

    // Expõe as cotações atualizadas em formato JSON para chamadas HTTP.
    @GetMapping("/cotacoes")
    public String consultarCotacoes() {
        return cotacaoService.buscarCotacoes();
    }
}
