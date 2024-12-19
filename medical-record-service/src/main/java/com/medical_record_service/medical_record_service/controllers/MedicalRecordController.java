package com.medical_record_service.medical_record_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medical_record_service.medical_record_service.models.MedicalRecord;
import com.medical_record_service.medical_record_service.models.MedicalRecordResponseDTO;
import com.medical_record_service.medical_record_service.services.MedicalRecordService;

import java.util.List;

@RestController
@RequestMapping("/medical-records")
@CrossOrigin(origins = "*")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<Void> createMedicalRecords() {
        return medicalRecordService.createMedicalRecords();
    }

    @GetMapping
    public ResponseEntity<List<MedicalRecordResponseDTO>> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO> getMedicalRecordById(@PathVariable Long id) {
        return medicalRecordService.getMedicalRecordById(id);
    }

    @GetMapping("/patient/{email}")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getMedicalRecordsByPatient(@PathVariable String email) {
        return medicalRecordService.getMedicalRecordsByPatient(email);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO> updateMedicalRecord(
            @PathVariable Long id,
            @RequestBody MedicalRecord recordDetails) {
        return medicalRecordService.updateMedicalRecord(id, recordDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        return medicalRecordService.deleteMedicalRecord(id);
    }
} 