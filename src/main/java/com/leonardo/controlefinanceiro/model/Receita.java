package com.leonardo.controlefinanceiro.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "receitas")
public class Receita {

    // Identificador único da receita.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dados principais da movimentação financeira.
    private String descricao;
    private Double valor;
    private LocalDate data;

    public Receita() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}