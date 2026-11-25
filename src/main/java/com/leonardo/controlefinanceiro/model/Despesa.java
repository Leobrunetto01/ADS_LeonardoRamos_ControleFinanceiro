package com.leonardo.controlefinanceiro.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "despesas")
public class Despesa {

    // Identificador único da despesa.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Descrição, valor monetário e data da despesa.
    private String descricao;
    private Double valor;
    private LocalDate data;

    // Identifica o usuário proprietário da despesa.
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Despesa() {
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

    // Retorna o usuário proprietário da despesa.
    public Usuario getUsuario() {
        return usuario;
    }

    // Define o usuário proprietário da despesa.
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}