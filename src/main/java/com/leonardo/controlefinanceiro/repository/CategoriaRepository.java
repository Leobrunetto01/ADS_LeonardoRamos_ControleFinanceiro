package com.leonardo.controlefinanceiro.repository;

import com.leonardo.controlefinanceiro.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository
        extends JpaRepository<Categoria, Long> {

    // Lista somente as categorias do usuário.
    List<Categoria> findByUsuarioId(Long usuarioId);

    // Busca uma categoria pertencente ao usuário.
    Optional<Categoria> findByIdAndUsuarioId(Long id, Long usuarioId);
}