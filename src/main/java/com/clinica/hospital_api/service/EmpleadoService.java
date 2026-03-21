package com.clinica.hospital_api.service;

import com.clinica.hospital_api.model.Empleado;
import com.clinica.hospital_api.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public Empleado guardarEmpleado(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    public List<Empleado> obtenerTodos() {
        return empleadoRepository.findAll();
    }

    public void eliminarEmpleado(Long id) {
        empleadoRepository.deleteById(id);
    }
}