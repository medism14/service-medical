package com.medical_record_service.medical_record_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.medical_record_service.medical_record_service.models.MedicalRecord;
import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatientEmail(String patientEmail);
    boolean existsByPatientEmail(String patientEmail);
    boolean existsByPatientSocialSecurityNumber(String ssn);
} 