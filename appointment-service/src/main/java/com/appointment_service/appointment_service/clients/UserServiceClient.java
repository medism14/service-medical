package com.appointment_service.appointment_service.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appointment_service.appointment_service.models.PatientDTO;
import com.appointment_service.appointment_service.models.PractitionerDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceClient.class);
    private final RestTemplate restTemplate;
    private static final String PATIENT_BASE_URL = "http://localhost:8001/patients";
    private static final String PRACTITIONER_BASE_URL = "http://localhost:8002/practitioners";

    @Autowired
    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "patientService", fallbackMethod = "getPatientFallback")
    public PatientDTO getPatientByEmail(String email) {
        logger.info("Fetching patient data for email: {}", email);
        try {
            ResponseEntity<PatientDTO> response = restTemplate.getForEntity(
                    PATIENT_BASE_URL + "/email/" + email,
                    PatientDTO.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            throw new RuntimeException("Patient service returned no data");
        } catch (Exception e) {
            logger.error("Error fetching patient data: {}", e.getMessage());
            throw e;
        }
    }

    @CircuitBreaker(name = "practitionerService", fallbackMethod = "getPractitionerFallback")
    public PractitionerDTO getPractitionerByEmail(String email) {
        logger.info("Fetching practitioner data for email: {}", email);
        try {
            ResponseEntity<PractitionerDTO> response = restTemplate.getForEntity(
                    PRACTITIONER_BASE_URL + "/email/" + email,
                    PractitionerDTO.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            throw new RuntimeException("Practitioner service returned no data");
        } catch (Exception e) {
            logger.error("Error fetching practitioner data: {}", e.getMessage());
            throw e;
        }
    }

    public PatientDTO getPatientFallback(String email, Exception ex) {
        logger.warn("Fallback for patient service. Email: {}, Error: {}", email, ex.getMessage());
        PatientDTO fallbackPatient = new PatientDTO();
        fallbackPatient.setEmail(email);
        fallbackPatient.setFirstName("Service Indisponible");
        fallbackPatient.setLastName("Service Indisponible");
        return fallbackPatient;
    }

    public PractitionerDTO getPractitionerFallback(String email, Exception ex) {
        logger.warn("Fallback for practitioner service. Email: {}, Error: {}", email, ex.getMessage());
        PractitionerDTO fallbackPractitioner = new PractitionerDTO();
        fallbackPractitioner.setEmail(email);
        fallbackPractitioner.setFirstName("Service Indisponible");
        fallbackPractitioner.setLastName("Service Indisponible");
        return fallbackPractitioner;
    }

    @CircuitBreaker(name = "patientService", fallbackMethod = "getAllPatientsFallback")
    public List<PatientDTO> getAllPatients() {
        logger.info("Fetching all patients");
        try {
            ResponseEntity<List<PatientDTO>> response = restTemplate.exchange(
                    PATIENT_BASE_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<PatientDTO>>() {
                    });
            return response.getBody() != null ? response.getBody() : new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error fetching all patients: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @CircuitBreaker(name = "practitionerService", fallbackMethod = "getAllPractitionersFallback")
    public List<PractitionerDTO> getAllPractitioners() {
        logger.info("Fetching all practitioners");
        try {
            ResponseEntity<List<PractitionerDTO>> response = restTemplate.exchange(
                    PRACTITIONER_BASE_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<PractitionerDTO>>() {
                    });
            return response.getBody() != null ? response.getBody() : new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error fetching all practitioners: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<PatientDTO> getAllPatientsFallback(Exception ex) {
        logger.warn("Fallback for getting all patients. Error: {}", ex.getMessage());
        return new ArrayList<>();
    }

    public List<PractitionerDTO> getAllPractitionersFallback(Exception ex) {
        logger.warn("Fallback for getting all practitioners. Error: {}", ex.getMessage());
        return new ArrayList<>();
    }
}