package com.practitioner_service.practitioner_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.practitioner_service.practitioner_service.repository.PractitionerRepository;
import com.practitioner_service.practitioner_service.models.Practitioner;

import java.util.List;
import java.util.Optional;

@Service
public class PractitionerService {

    private final PractitionerRepository practitionerRepository;

    @Autowired
    public PractitionerService(PractitionerRepository practitionerRepository) {
        this.practitionerRepository = practitionerRepository;
    }

    public ResponseEntity<List<Practitioner>> getAllPractitioners() {
        try {
            List<Practitioner> practitioners = practitionerRepository.findAll();
            if (practitioners.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(practitioners, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Practitioner> getPractitionerById(Long id) {
        try {
            Optional<Practitioner> practitioner = practitionerRepository.findById(id);
            return practitioner.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> createPractitioners() {
        try {
            String[][] practitionersData = {
                {"Dr. Jean", "Dupont", "jean.dupont@med.com", "0123456781", "Cardiologie", "CARD123", "1 rue de Paris"},
                {"Dr. Marie", "Curie", "marie.curie@med.com", "0123456782", "Radiologie", "RAD456", "2 avenue des Champs"},
                {"Dr. Pierre", "Martin", "pierre.martin@med.com", "0123456783", "Pédiatrie", "PED789", "3 boulevard Saint-Germain"},
                {"Dr. Sophie", "Durand", "sophie.durand@med.com", "0123456784", "Dermatologie", "DERM321", "4 place de la République"},
                {"Dr. Luc", "Bernard", "luc.bernard@med.com", "0123456785", "Neurologie", "NEUR654", "5 chemin de la Liberté"}
            };

            for (String[] data : practitionersData) {
                Practitioner practitioner = new Practitioner();
                practitioner.setFirstName(data[0]);
                practitioner.setLastName(data[1]);
                practitioner.setEmail(data[2]);
                practitioner.setPhoneNumber(data[3]);
                practitioner.setSpeciality(data[4]);
                practitioner.setLicenseNumber(data[5]);
                practitioner.setAddress(data[6]);

                if (practitionerRepository.findByEmail(practitioner.getEmail()).isPresent()) {
                    continue;
                }

                practitionerRepository.save(practitioner);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Practitioner> updatePractitioner(Long id, Practitioner practitionerDetails) {
        try {
            Optional<Practitioner> practitionerData = practitionerRepository.findById(id);
            if (practitionerData.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Practitioner existingPractitioner = practitionerData.get();
            existingPractitioner.setFirstName(practitionerDetails.getFirstName());
            existingPractitioner.setLastName(practitionerDetails.getLastName());
            existingPractitioner.setEmail(practitionerDetails.getEmail());
            existingPractitioner.setPhoneNumber(practitionerDetails.getPhoneNumber());
            existingPractitioner.setSpeciality(practitionerDetails.getSpeciality());
            existingPractitioner.setLicenseNumber(practitionerDetails.getLicenseNumber());
            existingPractitioner.setAddress(practitionerDetails.getAddress());

            return new ResponseEntity<>(practitionerRepository.save(existingPractitioner), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> deletePractitioner(Long id) {
        try {
            if (!practitionerRepository.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            practitionerRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Practitioner> getPractitionerByEmail(String email) {
        try {
            Optional<Practitioner> practitioner = practitionerRepository.findByEmail(email);
            return practitioner.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 