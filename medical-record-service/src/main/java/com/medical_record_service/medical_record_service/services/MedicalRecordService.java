package com.medical_record_service.medical_record_service.services;

import com.medical_record_service.medical_record_service.models.MedicalRecord;
import com.medical_record_service.medical_record_service.models.MedicalRecordResponseDTO;
import com.medical_record_service.medical_record_service.models.PatientDTO;
import com.medical_record_service.medical_record_service.models.PractitionerDTO;
import com.medical_record_service.medical_record_service.repository.MedicalRecordRepository;
import com.medical_record_service.medical_record_service.clients.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    private final Random random = new Random();

    private final String[] diagnoses = {
            "Hypertension artérielle", "Diabète de type 2", "Bronchite aiguë",
            "Migraine chronique", "Arthrose lombaire"
    };

    private final String[] treatments = {
            "Régime alimentaire et exercice", "Insulinothérapie", "Antibiotiques",
            "Antalgiques", "Kinésithérapie"
    };

    private final String[] prescriptions = {
            "Amlodipine 5mg", "Metformine 1000mg", "Amoxicilline 1g",
            "Paracétamol 1000mg", "Anti-inflammatoires"
    };

    public ResponseEntity<List<MedicalRecordResponseDTO>> getAllMedicalRecords() {
        List<MedicalRecord> records = medicalRecordRepository.findAll();
        if (records.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<MedicalRecordResponseDTO> dtos = new ArrayList<>();
        for (MedicalRecord record : records) {
            dtos.add(convertToDTO(record));
        }
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<Void> createMedicalRecords() {
        try {
            List<PatientDTO> patients = userServiceClient.getAllPatients();
            if (patients == null || patients.isEmpty()) {
                return ResponseEntity.status(503).build();
            }

            List<PractitionerDTO> practitioners = userServiceClient.getAllPractitioners();
            if (practitioners == null || practitioners.isEmpty()) {
                return ResponseEntity.status(503).build();
            }

            for (PatientDTO patient : patients) {
                try {
                    PractitionerDTO practitioner = practitioners.get(random.nextInt(practitioners.size()));

                    MedicalRecord record = new MedicalRecord(
                            patient.getEmail(),
                            practitioner.getEmail(),
                            diagnoses[random.nextInt(diagnoses.length)],
                            treatments[random.nextInt(treatments.length)],
                            prescriptions[random.nextInt(prescriptions.length)],
                            "Notes du " + java.time.LocalDate.now());

                    medicalRecordRepository.save(record);
                } catch (Exception e) {
                    System.err.println(
                            "Erreur lors de la création du dossier pour " + patient.getEmail() + ": " + e.getMessage());
                }
            }

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erreur lors de la création des dossiers médicaux: " + e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    public ResponseEntity<MedicalRecordResponseDTO> getMedicalRecordById(Long id) {
        Optional<MedicalRecord> recordOpt = medicalRecordRepository.findById(id);
        if (recordOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(recordOpt.get()));
    }

    public ResponseEntity<MedicalRecordResponseDTO> updateMedicalRecord(Long id, MedicalRecord recordDetails) {
        Optional<MedicalRecord> recordOpt = medicalRecordRepository.findById(id);
        if (recordOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MedicalRecord record = recordOpt.get();
        record.setPatientEmail(recordDetails.getPatientEmail());
        record.setPractitionerEmail(recordDetails.getPractitionerEmail());
        record.setDiagnosis(recordDetails.getDiagnosis());
        record.setTreatment(recordDetails.getTreatment());
        record.setPrescription(recordDetails.getPrescription());
        record.setNotes(recordDetails.getNotes());

        MedicalRecord updatedRecord = medicalRecordRepository.save(record);
        return ResponseEntity.ok(convertToDTO(updatedRecord));
    }

    public ResponseEntity<Void> deleteMedicalRecord(Long id) {
        if (!medicalRecordRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        medicalRecordRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<MedicalRecordResponseDTO>> getMedicalRecordsByPatient(String email) {
        List<MedicalRecord> records = medicalRecordRepository.findByPatientEmail(email);
        if (records.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<MedicalRecordResponseDTO> dtos = new ArrayList<>();
        for (MedicalRecord record : records) {
            dtos.add(convertToDTO(record));
        }
        return ResponseEntity.ok(dtos);
    }

    private MedicalRecordResponseDTO convertToDTO(MedicalRecord record) {
        return new MedicalRecordResponseDTO(
                record.getId(),
                userServiceClient.getPatientByEmail(record.getPatientEmail()),
                userServiceClient.getPractitionerByEmail(record.getPractitionerEmail()),
                record.getDiagnosis(),
                record.getTreatment(),
                record.getPrescription(),
                record.getNotes());
    }
}