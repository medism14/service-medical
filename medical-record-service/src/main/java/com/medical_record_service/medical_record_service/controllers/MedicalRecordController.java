package com.medical_record_service.medical_record_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medical_record_service.medical_record_service.models.MedicalRecord;
import com.medical_record_service.medical_record_service.services.MedicalRecordService;

import java.util.List;

@RestController
@RequestMapping("/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable Long id) {
        return medicalRecordService.getMedicalRecordById(id);
    }

    @GetMapping("/patient/email/{email}")
    public ResponseEntity<MedicalRecord> getMedicalRecordByPatientEmail(@PathVariable String email) {
        return medicalRecordService.getMedicalRecordByPatientEmail(email);
    }

    @GetMapping("/practitioner/email/{email}")
    public ResponseEntity<List<MedicalRecord>> getMedicalRecordsByPractitioner(@PathVariable String email) {
        return medicalRecordService.getMedicalRecordsByPractitioner(email);
    }

    @PostMapping
    public ResponseEntity<Void> createMedicalRecords() {
        return medicalRecordService.createMedicalRecords();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecord record) {
        return medicalRecordService.updateMedicalRecord(id, record);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        return medicalRecordService.deleteMedicalRecord(id);
    }
} 