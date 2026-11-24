package com.leonardo.controlefinanceiro.repository;

import com.leonardo.controlefinanceiro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring Data gera a consulta com base no nome do método.
    Optional<Usuario> findByEmail(String email);
}