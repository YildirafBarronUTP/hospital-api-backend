package com.clinica.hospital_api.repository;

import com.clinica.hospital_api.model.TokenInvitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenInvitacionRepository extends JpaRepository<TokenInvitacion, Long> {
    // Método para buscar si un token específico existe y es válido
    Optional<TokenInvitacion> findByToken(String token);
}