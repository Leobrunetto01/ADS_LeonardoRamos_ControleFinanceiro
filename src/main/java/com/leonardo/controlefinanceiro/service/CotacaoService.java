package com.leonardo.controlefinanceiro.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CotacaoService {

    private final RestClient restClient = RestClient.create(
        "https://economia.awesomeapi.com.br"
    );

    // Consulta os valores de dólar e euro na API externa da AwesomeAPI.
    public String buscarCotacoes() {
        return restClient.get()
                .uri("/json/last/USD-BRL,EUR-BRL")
                .retrieve()
                .body(String.class);
    }
}
