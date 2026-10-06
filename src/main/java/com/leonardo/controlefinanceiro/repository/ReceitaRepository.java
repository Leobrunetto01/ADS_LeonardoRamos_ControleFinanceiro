package com.leonardo.controlefinanceiro.repository;

import com.leonardo.controlefinanceiro.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
}