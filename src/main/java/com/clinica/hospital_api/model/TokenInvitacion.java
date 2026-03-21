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
    private String token;

    @Column(nullable = false)
    private String emailDestino;

    @Column(nullable = false)
    private String rolAsignado;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    private boolean usado = false;
}