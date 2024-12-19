package com.patient_service.patient_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.patient_service.patient_service.models.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findBySocialSecurityNumber(String socialSecurityNumber);
    boolean existsByEmail(String email);
    boolean existsBySocialSecurityNumber(String socialSecurityNumber);
}
