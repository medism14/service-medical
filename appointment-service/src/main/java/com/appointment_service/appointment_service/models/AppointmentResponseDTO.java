package com.appointment_service.appointment_service.models;

import java.time.LocalDateTime;

public class AppointmentResponseDTO {
    private Long id;
    private PatientDTO patient;
    private PractitionerDTO practitioner;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String reason;
    private String status;
    private String urlCalendar;

    // Constructeur
    public AppointmentResponseDTO(Appointment appointment, PatientDTO patient, PractitionerDTO practitioner) {
        this.id = appointment.getId();
        this.patient = patient;
        this.practitioner = practitioner;
        this.startTime = appointment.getStartTime();
        this.endTime = appointment.getEndTime();
        this.reason = appointment.getReason();
        this.status = appointment.getStatus();
        this.urlCalendar = appointment.getUrlCalendar();
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrlCalendar() {
        return urlCalendar;
    }

    public void setUrlCalendar(String urlCalendar) {
        this.urlCalendar = urlCalendar;
    }
} 