package com.medical_record_service.medical_record_service.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String patientEmail;

    @Column(nullable = false)
    private String patientSocialSecurityNumber;

    @Column(nullable = false)
    private String practitionerEmail;

    @ElementCollection
    private List<MedicalEntry> medicalHistory = new ArrayList<>();

    @ElementCollection
    private List<String> allergies = new ArrayList<>();

    @Column(nullable = false)
    private String status = "ACTIVE";

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @Column(nullable = false)
    private String lastUpdatedBy;

    // Classes imbriquées pour une meilleure structure des données
    @Embeddable
    public static class MedicalEntry {
        private LocalDateTime date;
        private String practitionerEmail;
        private String description;
        private String type; // CONSULTATION, DIAGNOSTIC, NOTE, etc.

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

        public String getPractitionerEmail() {
            return practitionerEmail;
        }

        public void setPractitionerEmail(String practitionerEmail) {
            this.practitionerEmail = practitionerEmail;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
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

    public String getPractitionerEmail() {
        return practitionerEmail;
    }

    public void setPractitionerEmail(String practitionerEmail) {
        this.practitionerEmail = practitionerEmail;
    }

    public List<MedicalEntry> getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(List<MedicalEntry> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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