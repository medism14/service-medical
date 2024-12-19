package com.patient_service.patient_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.patient_service.patient_service.repository.PatientRepository;
import com.patient_service.patient_service.models.Patient;

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
            String[][] patientsData = {
                {"Jean", "Dupont", "jean.dupont@example.com", "0123456781", "1 rue de Paris"},
                {"Marie", "Curie", "marie.curie@example.com", "0123456782", "2 avenue des Champs"},
                {"Pierre", "Martin", "pierre.martin@example.com", "0123456783", "3 boulevard Saint-Germain"},
                {"Sophie", "Durand", "sophie.durand@example.com", "0123456784", "4 place de la République"},
                {"Luc", "Bernard", "luc.bernard@example.com", "0123456785", "5 chemin de la Liberté"},
                {"Claire", "Lefevre", "claire.lefevre@example.com", "0123456786", "6 impasse des Fleurs"},
                {"Paul", "Moreau", "paul.moreau@example.com", "0123456787", "7 route de la Mer"},
                {"Emma", "Simon", "emma.simon@example.com", "0123456788", "8 allée des Écoles"},
                {"Julien", "Garnier", "julien.garnier@example.com", "0123456789", "9 rue des Artistes"},
                {"Alice", "Rousseau", "alice.rousseau@example.com", "0123456790", "10 avenue de la Paix"}
            };

            for (String[] data : patientsData) {
                Patient patient = new Patient();
                patient.setFirstName(data[0]);
                patient.setLastName(data[1]);
                patient.setEmail(data[2]);
                patient.setPhoneNumber(data[3]);
                patient.setAddress(data[4]);

                if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }

                patientRepository.save(patient);
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
}
