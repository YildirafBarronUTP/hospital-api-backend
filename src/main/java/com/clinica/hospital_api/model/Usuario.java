package com.clinica.hospital_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; // Usaremos el correo como nombre de usuario

    @Column(nullable = false)
    private String password; // Aquí irá el Hash (texto encriptado e ilegible), NUNCA la contraseña real

    @Column(nullable = false)
    private String rol; // Ej: ROLE_ADMIN, ROLE_DOCTOR, ROLE_RECEPCION

    // Vinculamos este usuario de sistema con su perfil de empleado en recursos humanos
    @OneToOne
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;
}