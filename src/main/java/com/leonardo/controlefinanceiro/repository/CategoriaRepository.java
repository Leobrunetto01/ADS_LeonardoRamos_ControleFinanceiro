package com.leonardo.controlefinanceiro.repository;

import com.leonardo.controlefinanceiro.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositório com operações CRUD para persistir categorias no banco de dados.
public interface CategoriaRepository
        extends JpaRepository<Categoria, Long> {
}
