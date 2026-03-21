package com.clinica.hospital_api.repository;

import com.clinica.hospital_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método personalizado para buscar si un correo ya existe en la base de datos
    Optional<Usuario> findByEmail(String email);
}