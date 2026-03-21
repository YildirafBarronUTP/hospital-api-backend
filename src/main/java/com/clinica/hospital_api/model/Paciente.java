package com.clinica.hospital_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 15)
    private String dni;

    private String sexo;
    private String telefono;
    private String correo;
    private String direccion;

    @Column(columnDefinition = "varchar(20) default 'En Espera'")
    private String estado;
}