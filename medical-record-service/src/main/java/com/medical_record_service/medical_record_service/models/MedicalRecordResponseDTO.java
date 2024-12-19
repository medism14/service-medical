package com.medical_record_service.medical_record_service.models;

import java.time.LocalDateTime;
import java.util.List;

public class MedicalRecordResponseDTO {
    private Long id;
    private String patientEmail;
    private String patientSocialSecurityNumber;
    private PatientDTO patient;
    private String practitionerEmail;
    private PractitionerDTO practitioner;
    private List<String> allergies;
    private List<MedicalRecord.MedicalEntry> medicalHistory;
    private String status;
    private LocalDateTime lastUpdated;
    private String lastUpdatedBy;

    // Constructeur par défaut
    public MedicalRecordResponseDTO() {
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientSocialSecurityNumber() {
        return patientSocialSecurityNumber;
    }

    public void setPatientSocialSecurityNumber(String patientSocialSecurityNumber) {
        this.patientSocialSecurityNumber = patientSocialSecurityNumber;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    public String getPractitionerEmail() {
        return practitionerEmail;
    }

    public void setPractitionerEmail(String practitionerEmail) {
        this.practitionerEmail = practitionerEmail;
    }

    public PractitionerDTO getPractitioner() {
        return practitioner;
    }

    public void setPractitioner(PractitionerDTO practitioner) {
        this.practitioner = practitioner;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public List<MedicalRecord.MedicalEntry> getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(List<MedicalRecord.MedicalEntry> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
} 