package com.leonardo.controlefinanceiro.repository;

import com.leonardo.controlefinanceiro.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    // Lista somente as receitas do usuário.
    List<Receita> findByUsuarioId(Long usuarioId);

    // Busca uma receita pertencente ao usuário.
    Optional<Receita> findByIdAndUsuarioId(Long id, Long usuarioId);

    // Soma somente as receitas do usuário.
    @Query("SELECT COALESCE(SUM(r.valor), 0) FROM Receita r WHERE r.usuario.id = :usuarioId")
    Double calcularTotalReceitas(@Param("usuarioId") Long usuarioId);
}