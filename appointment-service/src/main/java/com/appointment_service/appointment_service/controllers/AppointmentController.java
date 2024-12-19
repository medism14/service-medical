package com.appointment_service.appointment_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.appointment_service.appointment_service.models.Appointment;
import com.appointment_service.appointment_service.services.AppointmentService;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @PostMapping
    public ResponseEntity<Void> createAppointments() {
        return appointmentService.createAppointments();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
        return appointmentService.updateAppointment(id, appointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        return appointmentService.deleteAppointment(id);
    }

    @GetMapping("/patient/email/{email}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatientEmail(@PathVariable String email) {
        return appointmentService.getAppointmentsByPatient(email);
    }

    @GetMapping("/practitioner/email/{email}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPractitionerEmail(@PathVariable String email) {
        return appointmentService.getAppointmentsByPractitioner(email);
    }
} 