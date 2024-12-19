package com.medical_record_service.medical_record_service.models;

public class MedicalRecordResponseDTO {
    private Long id;
    private PatientDTO patient;
    private PractitionerDTO practitioner;
    private String diagnosis;
    private String treatment;
    private String prescription;
    private String notes;

    // Constructeurs
    public MedicalRecordResponseDTO() {
    }

    public MedicalRecordResponseDTO(Long id, PatientDTO patient, PractitionerDTO practitioner,
            String diagnosis, String treatment,
            String prescription, String notes) {
        this.id = id;
        this.patient = patient;
        this.practitioner = practitioner;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.prescription = prescription;
        this.notes = notes;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    public PractitionerDTO getPractitioner() {
        return practitioner;
    }

    public void setPractitioner(PractitionerDTO practitioner) {
        this.practitioner = practitioner;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}