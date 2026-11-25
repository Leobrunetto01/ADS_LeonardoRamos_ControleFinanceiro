package com.leonardo.controlefinanceiro.model;

import jakarta.persistence.*;

@Entity
public class Categoria {

    // Identificador gerado automaticamente no banco de dados.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome da categoria e seu tipo, como receita ou despesa.
    private String nome;
    private String tipo;

    // Identifica o usuário proprietário da categoria.
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Categoria() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // Retorna o usuário proprietário da categoria.
    public Usuario getUsuario() {
        return usuario;
    }

    // Define o usuário proprietário da categoria.
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}