package com.medical_record_service.medical_record_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.medical_record_service.medical_record_service.repository.MedicalRecordRepository;
import com.medical_record_service.medical_record_service.models.MedicalRecord;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public ResponseEntity<Void> createMedicalRecords() {
        try {
            Object[][] recordsData = {
                    { "jean.dupont@example.com", "180055512345678", new String[] { "Pénicilline", "Arachides" },
                            "Asthme chronique" },
                    { "marie.curie@example.com", "275086612345678", new String[] { "Lactose", "Pollen" },
                            "Diabète type 2" },
                    { "pierre.martin@example.com", "190036712345678", new String[] { "Fruits de mer", "Aspirine" },
                            "Hypertension" },
                    { "sophie.bernard@example.com", "265097812345678", new String[] { "Gluten", "Œufs" },
                            "Migraine chronique" },
                    { "lucas.petit@example.com", "195023412345678", new String[] { "Soja", "Noix" }, "Arthrite" }
            };

            String[] practitioners = {
                    "dr.dupont@med.com", "dr.martin@med.com", "dr.dubois@med.com",
                    "dr.laurent@med.com", "dr.robert@med.com", "dr.simon@med.com"
            };

            for (Object[] data : recordsData) {
                if (!medicalRecordRepository.existsByPatientEmail((String) data[0]) &&
                        !medicalRecordRepository.existsByPatientSocialSecurityNumber((String) data[1])) {

                    MedicalRecord record = new MedicalRecord();
                    record.setPatientEmail((String) data[0]);
                    record.setPatientSocialSecurityNumber((String) data[1]);
                    record.setAllergies(List.of((String[]) data[2]));

                    List<MedicalRecord.MedicalEntry> history = new ArrayList<>();

                    // Entrée initiale
                    LocalDateTime baseDate = LocalDateTime.now().minusYears(2);
                    MedicalRecord.MedicalEntry initialEntry = new MedicalRecord.MedicalEntry();
                    initialEntry.setDate(baseDate);
                    initialEntry.setPractitionerEmail(practitioners[0]);
                    initialEntry.setType("CREATION");
                    initialEntry.setDescription("Création du dossier médical - Condition chronique: " + data[3]);
                    history.add(initialEntry);

                    // Ajout d'entrées médicales variées
                    addMedicalEntry(history, baseDate.plusMonths(3), practitioners[1], "CONSULTATION",
                            "Examen de routine - Tension artérielle: 12/8, Poids: 70kg");

                    addMedicalEntry(history, baseDate.plusMonths(6), practitioners[2], "PRESCRIPTION",
                            "Renouvellement traitement chronique - 3 mois");

                    addMedicalEntry(history, baseDate.plusMonths(8), practitioners[3], "ANALYSE",
                            "Bilan sanguin complet - Résultats dans les normes");

                    addMedicalEntry(history, baseDate.plusYears(1), practitioners[4], "HOSPITALISATION",
                            "Intervention mineure - Observation 24h");

                    addMedicalEntry(history, baseDate.plusMonths(15), practitioners[5], "VACCINATION",
                            "Mise à jour vaccins - Grippe saisonnière");

                    record.setMedicalHistory(history);
                    record.setLastUpdatedBy(practitioners[5]);
                    record.setStatus("ACTIVE");

                    medicalRecordRepository.save(record);
                }
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void addMedicalEntry(List<MedicalRecord.MedicalEntry> history, LocalDateTime date,
            String practitionerEmail, String type, String description) {
        MedicalRecord.MedicalEntry entry = new MedicalRecord.MedicalEntry();
        entry.setDate(date);
        entry.setPractitionerEmail(practitionerEmail);
        entry.setType(type);
        entry.setDescription(description);
        history.add(entry);
    }

    public ResponseEntity<MedicalRecord> getMedicalRecordById(Long id) {
        try {
            Optional<MedicalRecord> record = medicalRecordRepository.findById(id);
            return record.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<MedicalRecord> getMedicalRecordByPatientEmail(String patientEmail) {
        try {
            Optional<MedicalRecord> record = medicalRecordRepository.findByPatientEmail(patientEmail);
            return record.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<MedicalRecord>> getMedicalRecordsByPractitioner(String practitionerEmail) {
        try {
            List<MedicalRecord> records = medicalRecordRepository.findByPractitionerEmail(practitionerEmail);
            if (records.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<MedicalRecord> updateMedicalRecord(Long id, MedicalRecord recordDetails) {
        try {
            Optional<MedicalRecord> recordData = medicalRecordRepository.findById(id);
            if (recordData.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            MedicalRecord record = recordData.get();

            // Mise à jour des champs de base
            if (recordDetails.getMedicalHistory() != null) {
                record.setMedicalHistory(recordDetails.getMedicalHistory());
            }
            if (recordDetails.getAllergies() != null) {
                record.setAllergies(recordDetails.getAllergies());
            }
            if (recordDetails.getStatus() != null) {
                record.setStatus(recordDetails.getStatus());
            }

            // Mise à jour des métadonnées
            record.setLastUpdated(LocalDateTime.now());
            record.setLastUpdatedBy(recordDetails.getLastUpdatedBy());

            return new ResponseEntity<>(medicalRecordRepository.save(record), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> deleteMedicalRecord(Long id) {
        try {
            if (!medicalRecordRepository.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            medicalRecordRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
        try {
            List<MedicalRecord> records = medicalRecordRepository.findAll();
            if (records.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}