package com.appointment_service.appointment_service.controllers;

import com.appointment_service.appointment_service.models.AppointmentResponseDTO;
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
            @ApiResponse(responseCode = "204", description = "Aucun rendez-vous trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @Operation(summary = "Récupère un rendez-vous par ID", description = "Retourne un rendez-vous unique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendez-vous trouvé"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(
            @Parameter(description = "ID du rendez-vous", required = true) @PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @Operation(summary = "Crée un nouveau rendez-vous", description = "Crée un nouveau rendez-vous dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendez-vous créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @PostMapping("")
    public ResponseEntity<Void> createAppointments() {
        return appointmentService.createAppointments();
    }

    @Operation(summary = "Supprime un rendez-vous", description = "Supprime un rendez-vous du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rendez-vous supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @Parameter(description = "ID du rendez-vous", required = true) @PathVariable Long id) {
        return appointmentService.deleteAppointment(id);
    }

    @Operation(summary = "Liste les rendez-vous d'un patient", description = "Retourne tous les rendez-vous d'un patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous trouvée"),
            @ApiResponse(responseCode = "204", description = "Aucun rendez-vous trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/patient/{email}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByPatient(
            @Parameter(description = "Email du patient", required = true) @PathVariable String email) {
        return appointmentService.getAppointmentsByPatient(email);
    }

    @Operation(summary = "Liste les rendez-vous d'un praticien", description = "Retourne tous les rendez-vous d'un praticien")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous trouvée"),
            @ApiResponse(responseCode = "204", description = "Aucun rendez-vous trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/practitioner/{email}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByPractitioner(
            @Parameter(description = "Email du praticien", required = true) @PathVariable String email) {
        return appointmentService.getAppointmentsByPractitioner(email);
    }
}