package com.practitioner_service.practitioner_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.practitioner_service.practitioner_service.models.Practitioner;

public interface PractitionerRepository extends JpaRepository<Practitioner, Long> {
    Optional<Practitioner> findByEmail(String email);
    Optional<Practitioner> findByLicenseNumber(String licenseNumber);
} 