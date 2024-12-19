package com.patient_service.patient_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.patient_service.patient_service.repository.PatientRepository;
import com.patient_service.patient_service.models.Patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public ResponseEntity<List<Patient>> getAllPatients() {
        try {
            List<Patient> patients = patientRepository.findAll();
            if (patients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Patient> getPatientById(Long id) {
        try {
            Optional<Patient> patient = patientRepository.findById(id);
            return patient.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> createPatients() {
        try {
            Object[][] patientsData = {
                    { "Jean", "Dupont", "jean.dupont@example.com", LocalDate.of(1980, 5, 15),
                            "0123456789", "123 Rue de Paris", "180055512345678" },
                    { "Marie", "Curie", "marie.curie@example.com", LocalDate.of(1975, 8, 22),
                            "0234567890", "456 Avenue des Sciences", "275086612345678" },
                    { "Pierre", "Martin", "pierre.martin@example.com", LocalDate.of(1990, 3, 10),
                            "0345678901", "789 Boulevard des Arts", "190036712345678" }
            };

            for (Object[] data : patientsData) {
                if (!patientRepository.existsByEmail((String) data[2]) &&
                    !patientRepository.existsBySocialSecurityNumber((String) data[6])) {
                    Patient patient = new Patient();
                    patient.setFirstName((String) data[0]);
                    patient.setLastName((String) data[1]);
                    patient.setEmail((String) data[2]);
                    patient.setDateOfBirth((LocalDate) data[3]);
                    patient.setPhoneNumber((String) data[4]);
                    patient.setAddress((String) data[5]);
                    patient.setSocialSecurityNumber((String) data[6]);
                    patientRepository.save(patient);
                }
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Patient> updatePatient(Long id, Patient patientDetails) {
        try {
            Optional<Patient> patientData = patientRepository.findById(id);
            if (patientData.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Patient existingPatient = patientData.get();
            existingPatient.setFirstName(patientDetails.getFirstName());
            existingPatient.setLastName(patientDetails.getLastName());
            existingPatient.setEmail(patientDetails.getEmail());
            existingPatient.setPhoneNumber(patientDetails.getPhoneNumber());
            existingPatient.setAddress(patientDetails.getAddress());
            // Suppression de la ligne concernant matricule
            // existingPatient.setMatricule(patientDetails.getMatricule());

            return new ResponseEntity<>(patientRepository.save(existingPatient), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> deletePatient(Long id) {
        try {
            if (!patientRepository.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            patientRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Patient> getPatientBySocialSecurityNumber(String ssn) {
        try {
            Optional<Patient> patient = patientRepository.findBySocialSecurityNumber(ssn);
            return patient.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
