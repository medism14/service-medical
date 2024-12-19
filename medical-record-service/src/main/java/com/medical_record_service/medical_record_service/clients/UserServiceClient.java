package com.medical_record_service.medical_record_service.clients;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.medical_record_service.medical_record_service.models.PatientDTO;
import com.medical_record_service.medical_record_service.models.PractitionerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceClient.class);
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Value("${service.patient.url:http://localhost:8001}")
    private String patientServiceUrl;
    
    @Value("${service.practitioner.url:http://localhost:8002}")
    private String practitionerServiceUrl;

    public List<PatientDTO> getAllPatients() {
        try {
            ResponseEntity<List<PatientDTO>> response = restTemplate.exchange(
                patientServiceUrl + "/patients",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PatientDTO>>() {}
            );
            return response.getBody() != null ? response.getBody() : new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error fetching patients: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<PractitionerDTO> getAllPractitioners() {
        try {
            ResponseEntity<List<PractitionerDTO>> response = restTemplate.exchange(
                practitionerServiceUrl + "/practitioners",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PractitionerDTO>>() {}
            );
            return response.getBody() != null ? response.getBody() : new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error fetching practitioners: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public PatientDTO getPatientByEmail(String email) {
        try {
            return restTemplate.getForObject(
                patientServiceUrl + "/patients/email/" + email,
                PatientDTO.class
            );
        } catch (Exception e) {
            logger.error("Error fetching patient: {}", e.getMessage());
            return null;
        }
    }

    public PractitionerDTO getPractitionerByEmail(String email) {
        try {
            return restTemplate.getForObject(
                practitionerServiceUrl + "/practitioners/email/" + email,
                PractitionerDTO.class
            );
        } catch (Exception e) {
            logger.error("Error fetching practitioner: {}", e.getMessage());
            return null;
        }
    }
}