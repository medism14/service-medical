package com.medical_record_service.medical_record_service.repository;

import com.medical_record_service.medical_record_service.models.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatientEmail(String email);
    boolean existsByPatientEmail(String email);
} 