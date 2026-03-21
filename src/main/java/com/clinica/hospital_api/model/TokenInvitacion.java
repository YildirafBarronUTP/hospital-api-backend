package com.clinica.hospital_api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tokens_invitacion")
public class TokenInvitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token; // Ej: "abc123xyz987"

    @Column(nullable = false)
    private String emailDestino; // A quién se lo enviamos

    @Column(nullable = false)
    private String rolAsignado; // Qué rol tendrá cuando acepte la invitación

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion; // Para que el link muera en 24 horas

    private boolean usado = false; // Cambiará a true cuando el usuario cree su cuenta
}