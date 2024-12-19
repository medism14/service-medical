package com.medical_record_service.medical_record_service.clients;

import com.medical_record_service.medical_record_service.models.PatientDTO;
import com.medical_record_service.medical_record_service.models.PractitionerDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceClient.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    private static final String PATIENT_SERVICE_URL = "http://localhost:8001";
    private static final String PRACTITIONER_SERVICE_URL = "http://localhost:8002";

    @Retry(name = "userService", fallbackMethod = "getAllPatientsFallback")
    @CircuitBreaker(name = "userService", fallbackMethod = "getAllPatientsFallback")
    public List<PatientDTO> getAllPatients() {
        try {
            ResponseEntity<List<PatientDTO>> response = restTemplate.exchange(
                PATIENT_SERVICE_URL + "/patients",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PatientDTO>>() {}
            );
            return response.getBody() != null ? response.getBody() : new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error fetching patients: {}", e.getMessage());
            throw e;
        }
    }

    @Retry(name = "userService", fallbackMethod = "getAllPractitionersFallback")
    @CircuitBreaker(name = "userService", fallbackMethod = "getAllPractitionersFallback")
    public List<PractitionerDTO> getAllPractitioners() {
        try {
            ResponseEntity<List<PractitionerDTO>> response = restTemplate.exchange(
                PRACTITIONER_SERVICE_URL + "/practitioners",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PractitionerDTO>>() {}
            );
            return response.getBody() != null ? response.getBody() : new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error fetching practitioners: {}", e.getMessage());
            throw e;
        }
    }

    @Retry(name = "userService", fallbackMethod = "getPatientByEmailFallback")
    @CircuitBreaker(name = "userService", fallbackMethod = "getPatientByEmailFallback")
    public PatientDTO getPatientByEmail(String email) {
        try {
            PatientDTO patient = restTemplate.getForObject(
                PATIENT_SERVICE_URL + "/patients/email/" + email,
                PatientDTO.class
            );
            return patient != null ? patient : createDefaultPatient(email);
        } catch (Exception e) {
            logger.error("Error fetching patient: {}", e.getMessage());
            throw e;
        }
    }

    @Retry(name = "userService", fallbackMethod = "getPractitionerByEmailFallback")
    @CircuitBreaker(name = "userService", fallbackMethod = "getPractitionerByEmailFallback")
    public PractitionerDTO getPractitionerByEmail(String email) {
        try {
            PractitionerDTO practitioner = restTemplate.getForObject(
                PRACTITIONER_SERVICE_URL + "/practitioners/email/" + email,
                PractitionerDTO.class
            );
            return practitioner != null ? practitioner : createDefaultPractitioner(email);
        } catch (Exception e) {
            logger.error("Error fetching practitioner: {}", e.getMessage());
            throw e;
        }
    }

    // Méthodes de fallback
    public List<PatientDTO> getAllPatientsFallback(Exception e) {
        logger.warn("Fallback for getAllPatients: {}", e.getMessage());
        return new ArrayList<>();
    }

    public List<PractitionerDTO> getAllPractitionersFallback(Exception e) {
        logger.warn("Fallback for getAllPractitioners: {}", e.getMessage());
        return new ArrayList<>();
    }

    public PatientDTO getPatientByEmailFallback(String email, Exception e) {
        logger.warn("Fallback for getPatientByEmail: {}", e.getMessage());
        return createDefaultPatient(email);
    }

    public PractitionerDTO getPractitionerByEmailFallback(String email, Exception e) {
        logger.warn("Fallback for getPractitionerByEmail: {}", e.getMessage());
        return createDefaultPractitioner(email);
    }

    private PatientDTO createDefaultPatient(String email) {
        PatientDTO patient = new PatientDTO();
        patient.setEmail(email);
        patient.setFirstName("Indisponible");
        patient.setLastName("Indisponible");
        return patient;
    }

    private PractitionerDTO createDefaultPractitioner(String email) {
        PractitionerDTO practitioner = new PractitionerDTO();
        practitioner.setEmail(email);
        practitioner.setFirstName("Indisponible");
        practitioner.setLastName("Indisponible");
        return practitioner;
    }
}