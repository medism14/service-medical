package com.appointment_service.appointment_service.controllers;

import com.appointment_service.appointment_service.models.Appointment;
import com.appointment_service.appointment_service.services.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@Tag(name = "Appointment Management", description = "Opérations liées à la gestion des rendez-vous")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Operation(summary = "Liste tous les rendez-vous", description = "Retourne la liste complète des rendez-vous")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Aucun rendez-vous trouvé")
    })
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @Operation(summary = "Récupère un rendez-vous par ID", description = "Retourne un rendez-vous unique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendez-vous trouvé"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(
            @Parameter(description = "ID du rendez-vous", required = true) @PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @Operation(summary = "Crée de nouveaux rendez-vous", description = "Crée des rendez-vous de test dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rendez-vous créés avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur lors de la création")
    })
    @PostMapping
    public ResponseEntity<Void> createAppointment() {
        return appointmentService.createAppointments();
    }

    @Operation(summary = "Met à jour un rendez-vous", description = "Met à jour les informations d'un rendez-vous existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendez-vous mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @Parameter(description = "ID du rendez-vous", required = true) @PathVariable Long id,
            @Parameter(description = "Nouvelles informations du rendez-vous", required = true) @RequestBody Appointment appointment) {
        return appointmentService.updateAppointment(id, appointment);
    }

    @Operation(summary = "Supprime un rendez-vous", description = "Supprime un rendez-vous du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rendez-vous supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @Parameter(description = "ID du rendez-vous", required = true) @PathVariable Long id) {
        return appointmentService.deleteAppointment(id);
    }

    @Operation(summary = "Liste les rendez-vous d'un patient", description = "Retourne tous les rendez-vous d'un patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous trouvée"),
            @ApiResponse(responseCode = "404", description = "Aucun rendez-vous trouvé")
    })
    @GetMapping("/patient/{email}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatient(
            @Parameter(description = "Email du patient", required = true) @PathVariable String email) {
        return appointmentService.getAppointmentsByPatient(email);
    }

    @Operation(summary = "Liste les rendez-vous d'un praticien", description = "Retourne tous les rendez-vous d'un praticien")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous trouvée"),
            @ApiResponse(responseCode = "404", description = "Aucun rendez-vous trouvé")
    })
    @GetMapping("/practitioner/{email}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPractitioner(
            @Parameter(description = "Email du praticien", required = true) @PathVariable String email) {
        return appointmentService.getAppointmentsByPractitioner(email);
    }
} 