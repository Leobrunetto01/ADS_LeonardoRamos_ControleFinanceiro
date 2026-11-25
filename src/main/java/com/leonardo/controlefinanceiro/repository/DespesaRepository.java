package com.leonardo.controlefinanceiro.repository;

import com.leonardo.controlefinanceiro.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    // Lista somente as despesas do usuário.
    List<Despesa> findByUsuarioId(Long usuarioId);

    // Busca uma despesa pertencente ao usuário.
    Optional<Despesa> findByIdAndUsuarioId(Long id, Long usuarioId);

    // Soma somente as despesas do usuário.
    @Query("SELECT COALESCE(SUM(d.valor), 0.0) FROM Despesa d WHERE d.usuario.id = :usuarioId")
    Double calcularTotalDespesas(@Param("usuarioId") Long usuarioId);
}