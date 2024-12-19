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

    @ElementCollection
    private List<MedicalEntry> medicalHistory = new ArrayList<>();

    @ElementCollection
    private List<Treatment> treatments = new ArrayList<>();

    @ElementCollection
    private List<Examination> examinations = new ArrayList<>();

    @ElementCollection
    private List<Prescription> prescriptions = new ArrayList<>();

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

    @Embeddable
    public static class Treatment {
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String name;
        private String dosage;
        private String frequency;
        private String practitionerEmail;
        private String notes;

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDateTime startDate) {
            this.startDate = startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDateTime endDate) {
            this.endDate = endDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDosage() {
            return dosage;
        }

        public void setDosage(String dosage) {
            this.dosage = dosage;
        }

        public String getFrequency() {
            return frequency;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public String getPractitionerEmail() {
            return practitionerEmail;
        }

        public void setPractitionerEmail(String practitionerEmail) {
            this.practitionerEmail = practitionerEmail;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    @Embeddable
    public static class Examination {
        private LocalDateTime date;
        private String type;
        private String result;
        private String practitionerEmail;
        private String notes;

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getPractitionerEmail() {
            return practitionerEmail;
        }

        public void setPractitionerEmail(String practitionerEmail) {
            this.practitionerEmail = practitionerEmail;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    @Embeddable
    public static class Prescription {
        private LocalDateTime date;
        private LocalDateTime expiryDate;
        private String medication;
        private String dosage;
        private String frequency;
        private String duration;
        private String practitionerEmail;
        private String notes;

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

        public LocalDateTime getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(LocalDateTime expiryDate) {
            this.expiryDate = expiryDate;
        }

        public String getMedication() {
            return medication;
        }

        public void setMedication(String medication) {
            this.medication = medication;
        }

        public String getDosage() {
            return dosage;
        }

        public void setDosage(String dosage) {
            this.dosage = dosage;
        }

        public String getFrequency() {
            return frequency;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getPractitionerEmail() {
            return practitionerEmail;
        }

        public void setPractitionerEmail(String practitionerEmail) {
            this.practitionerEmail = practitionerEmail;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
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

    public List<MedicalEntry> getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(List<MedicalEntry> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<Treatment> treatments) {
        this.treatments = treatments;
    }

    public List<Examination> getExaminations() {
        return examinations;
    }

    public void setExaminations(List<Examination> examinations) {
        this.examinations = examinations;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
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