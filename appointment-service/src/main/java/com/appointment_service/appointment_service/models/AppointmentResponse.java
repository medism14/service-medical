package com.appointment_service.appointment_service.models;

public class AppointmentResponse {
    private Appointment appointment;
    private PatientDTO patient;
    private PractitionerDTO practitioner;

    public AppointmentResponse(Appointment appointment, PatientDTO patient, PractitionerDTO practitioner) {
        this.appointment = appointment;
        this.patient = patient;
        this.practitioner = practitioner;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
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
} 