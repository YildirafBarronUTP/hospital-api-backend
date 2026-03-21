package com.clinica.hospital_api.service;

import com.clinica.hospital_api.model.Doctor;
import com.clinica.hospital_api.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public Doctor guardarDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public List<Doctor> obtenerTodos() {
        return doctorRepository.findAll();
    }

    public void eliminarDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}