package com.leonardo.controlefinanceiro.repository;

import com.leonardo.controlefinanceiro.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    @Query("SELECT COALESCE(SUM(d.valor), 0.0) FROM Despesa d")
    Double calcularTotalDespesas();
}