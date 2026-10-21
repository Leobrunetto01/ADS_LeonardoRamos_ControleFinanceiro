
package com.leonardo.controlefinanceiro.repository;

import com.leonardo.controlefinanceiro.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    @Query("SELECT COALESCE(SUM(r.valor), 0) FROM Receita r")
    Double calcularTotalReceitas();
}
