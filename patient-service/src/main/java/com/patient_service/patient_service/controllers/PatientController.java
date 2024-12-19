package com.patient_service.patient_service.controllers;

import com.patient_service.patient_service.models.Patient;
import com.patient_service.patient_service.services.PatientService;
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
@RequestMapping("/patients")
@Tag(name = "Patient Management", description = "Opérations liées à la gestion des patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Operation(summary = "Liste tous les patients", description = "Retourne la liste complète des patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des patients récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Aucun patient trouvé")
    })
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return patientService.getAllPatients();
    }

    @Operation(summary = "Récupère un patient par son ID", description = "Retourne un patient unique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient trouvé"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(
            @Parameter(description = "ID du patient", required = true) @PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @Operation(summary = "Crée un nouveau patient", description = "Crée un nouveau patient dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PostMapping
    public ResponseEntity<Void> createPatient() {
        return patientService.createPatients();
    }

    @Operation(summary = "Met à jour un patient", description = "Met à jour les informations d'un patient existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @Parameter(description = "ID du patient", required = true) @PathVariable Long id,
            @Parameter(description = "Nouvelles informations du patient", required = true) @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient);
    }

    @Operation(summary = "Supprime un patient", description = "Supprime un patient du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(
            @Parameter(description = "ID du patient", required = true) @PathVariable Long id) {
        return patientService.deletePatient(id);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Patient> getPatientByEmail(@PathVariable String email) {
        return patientService.getPatientByEmail(email);
    }
}
