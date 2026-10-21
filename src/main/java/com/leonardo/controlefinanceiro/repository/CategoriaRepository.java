
package com.leonardo.controlefinanceiro.repository;

import com.leonardo.controlefinanceiro.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository
        extends JpaRepository<Categoria, Long> {
}
