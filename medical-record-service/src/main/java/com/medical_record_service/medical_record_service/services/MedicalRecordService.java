package com.medical_record_service.medical_record_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.medical_record_service.medical_record_service.repository.MedicalRecordRepository;
import com.medical_record_service.medical_record_service.models.*;
import com.medical_record_service.medical_record_service.clients.UserServiceClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final UserServiceClient userServiceClient;
    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

    @Autowired
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, UserServiceClient userServiceClient) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.userServiceClient = userServiceClient;
    }

    public ResponseEntity<Void> createMedicalRecords() {
        try {
            List<PatientDTO> patients = userServiceClient.getAllPatients();
            List<PractitionerDTO> practitioners = userServiceClient.getAllPractitioners();

            if (patients.isEmpty() || practitioners.isEmpty()) {
                logger.error("No patients or practitioners available");
                return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            }

            Random random = new Random();
            String[] conditions = {"Asthme chronique", "Diabète type 2", "Hypertension", "Migraine chronique", "Arthrite"};
            String[] allergies = {"Pénicilline", "Lactose", "Pollen", "Arachides", "Gluten", "Fruits de mer"};

            for (PatientDTO patient : patients) {
                if (!medicalRecordRepository.existsByPatientEmail(patient.getEmail())) {
                    MedicalRecord record = new MedicalRecord();
                    
                    // Informations de base
                    record.setPatientEmail(patient.getEmail());
                    record.setPatientSocialSecurityNumber(patient.getSocialSecurityNumber());
                    PractitionerDTO randomPractitioner = practitioners.get(random.nextInt(practitioners.size()));
                    record.setPractitionerEmail(randomPractitioner.getEmail());
                    
                    // Dates et statut
                    LocalDateTime now = LocalDateTime.now();
                    record.setCreatedAt(now);
                    record.setLastUpdated(now);
                    record.setLastUpdatedBy(randomPractitioner.getEmail());
                    record.setStatus("ACTIVE");

                    // Allergies
                    List<String> patientAllergies = new ArrayList<>();
                    int numAllergies = random.nextInt(3) + 1;
                    for (int i = 0; i < numAllergies; i++) {
                        patientAllergies.add(allergies[random.nextInt(allergies.length)]);
                    }
                    record.setAllergies(patientAllergies);

                    // Historique médical
                    List<MedicalRecord.MedicalEntry> history = new ArrayList<>();
                    LocalDateTime baseDate = now.minusYears(1);

                    int numEntries = random.nextInt(3) + 3;
                    for (int i = 0; i < numEntries; i++) {
                        PractitionerDTO entryPractitioner = practitioners.get(random.nextInt(practitioners.size()));
                        MedicalRecord.MedicalEntry entry = new MedicalRecord.MedicalEntry();
                        entry.setDate(baseDate.plusMonths(i * 2L));
                        entry.setPractitionerEmail(entryPractitioner.getEmail());
                        entry.setType(getRandomEntryType());
                        entry.setDescription(generateDescription(entry.getType(), conditions[random.nextInt(conditions.length)]));
                        history.add(entry);
                    }
                    record.setMedicalHistory(history);

                    medicalRecordRepository.save(record);
                }
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating medical records: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getRandomEntryType() {
        String[] types = {"CONSULTATION", "PRESCRIPTION", "ANALYSE", "VACCINATION", "HOSPITALISATION"};
        return types[new Random().nextInt(types.length)];
    }

    private String generateDescription(String type, String condition) {
        switch (type) {
            case "CONSULTATION":
                return "Consultation de suivi - " + condition;
            case "PRESCRIPTION":
                return "Renouvellement traitement - " + condition;
            case "ANALYSE":
                return "Analyses sanguines - Suivi " + condition;
            case "VACCINATION":
                return "Vaccination préventive";
            case "HOSPITALISATION":
                return "Hospitalisation pour " + condition;
            default:
                return "Visite médicale standard";
        }
    }

    public ResponseEntity<List<MedicalRecordResponseDTO>> getAllMedicalRecords() {
        try {
            List<MedicalRecord> records = medicalRecordRepository.findAll();
            if (records.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<MedicalRecordResponseDTO> responseDTOs = records.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving medical records: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<MedicalRecordResponseDTO> getMedicalRecordById(Long id) {
        try {
            Optional<MedicalRecord> record = medicalRecordRepository.findById(id);
            if (record.isPresent()) {
                MedicalRecordResponseDTO responseDTO = convertToResponseDTO(record.get());
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error retrieving medical record: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<MedicalRecordResponseDTO>> getMedicalRecordsByPatient(String email) {
        try {
            List<MedicalRecord> records = medicalRecordRepository.findByPatientEmail(email);
            if (records.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<MedicalRecordResponseDTO> responseDTOs = records.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving medical records for patient: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<MedicalRecordResponseDTO> updateMedicalRecord(Long id, MedicalRecord recordDetails) {
        try {
            Optional<MedicalRecord> existingRecord = medicalRecordRepository.findById(id);
            if (existingRecord.isPresent()) {
                MedicalRecord record = existingRecord.get();
                record.setAllergies(recordDetails.getAllergies());
                record.setMedicalHistory(recordDetails.getMedicalHistory());
                record.setStatus(recordDetails.getStatus());
                record.setLastUpdated(LocalDateTime.now());
                record.setLastUpdatedBy(recordDetails.getLastUpdatedBy());
                record.setPractitionerEmail(recordDetails.getPractitionerEmail());

                MedicalRecord updatedRecord = medicalRecordRepository.save(record);
                MedicalRecordResponseDTO responseDTO = convertToResponseDTO(updatedRecord);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error updating medical record: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> deleteMedicalRecord(Long id) {
        try {
            if (medicalRecordRepository.existsById(id)) {
                medicalRecordRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error deleting medical record: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private MedicalRecordResponseDTO convertToResponseDTO(MedicalRecord record) {
        MedicalRecordResponseDTO dto = new MedicalRecordResponseDTO();
        
        // Informations de base
        dto.setId(record.getId());
        dto.setPatientEmail(record.getPatientEmail());
        dto.setPatientSocialSecurityNumber(record.getPatientSocialSecurityNumber());
        dto.setPractitionerEmail(record.getPractitionerEmail());
        
        // Enrichir avec les informations du patient et du praticien
        try {
            PatientDTO patient = userServiceClient.getPatientByEmail(record.getPatientEmail());
            dto.setPatient(patient);
        } catch (Exception e) {
            logger.warn("Could not fetch patient details for email: {}", record.getPatientEmail());
        }

        try {
            PractitionerDTO practitioner = userServiceClient.getPractitionerByEmail(record.getPractitionerEmail());
            dto.setPractitioner(practitioner);
        } catch (Exception e) {
            logger.warn("Could not fetch practitioner details for email: {}", record.getPractitionerEmail());
        }

        // Autres informations
        dto.setAllergies(record.getAllergies());
        dto.setMedicalHistory(record.getMedicalHistory());
        dto.setStatus(record.getStatus());
        dto.setLastUpdated(record.getLastUpdated());
        dto.setLastUpdatedBy(record.getLastUpdatedBy());

        return dto;
    }
}