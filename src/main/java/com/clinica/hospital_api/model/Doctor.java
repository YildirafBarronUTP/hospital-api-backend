package com.clinica.hospital_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "doctores")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 15)
    private String dni;

    @Column(nullable = false)
    private String especialidad;

    private String telefono;
    private String correo;

    @Column(columnDefinition = "varchar(20) default 'Libre'")
    private String estado;
}