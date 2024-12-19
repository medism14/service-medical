package com.medical_record_service.medical_record_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

import com.medical_record_service.medical_record_service.models.MedicalRecord;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByPatientEmail(String patientEmail);
    Optional<MedicalRecord> findByPatientSocialSecurityNumber(String ssn);
    
    @Query("SELECT DISTINCT m FROM MedicalRecord m JOIN m.medicalHistory h WHERE h.practitionerEmail = :practitionerEmail")
    List<MedicalRecord> findByPractitionerEmail(String practitionerEmail);
    
    boolean existsByPatientEmail(String patientEmail);
    boolean existsByPatientSocialSecurityNumber(String ssn);
} 