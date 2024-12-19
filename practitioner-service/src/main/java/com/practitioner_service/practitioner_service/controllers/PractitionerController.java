package com.practitioner_service.practitioner_service.controllers;

import com.practitioner_service.practitioner_service.models.Practitioner;
import com.practitioner_service.practitioner_service.services.PractitionerService;
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
@RequestMapping("/practitioners")
@Tag(name = "Practitioner Management", description = "Opérations liées à la gestion des praticiens")
public class PractitionerController {

    @Autowired
    private PractitionerService practitionerService;

    @Operation(summary = "Liste tous les praticiens", description = "Retourne la liste complète des praticiens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des praticiens récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Aucun praticien trouvé")
    })
    @GetMapping
    public ResponseEntity<List<Practitioner>> getAllPractitioners() {
        return practitionerService.getAllPractitioners();
    }

    @Operation(summary = "Récupère un praticien par son ID", description = "Retourne un praticien unique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Praticien trouvé"),
            @ApiResponse(responseCode = "404", description = "Praticien non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Practitioner> getPractitionerById(
            @Parameter(description = "ID du praticien", required = true) @PathVariable Long id) {
        return practitionerService.getPractitionerById(id);
    }

    @Operation(summary = "Crée un nouveau praticien", description = "Crée un nouveau praticien dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Praticien créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PostMapping
    public ResponseEntity<Void> createPractitioner() {
        return practitionerService.createPractitioners();
    }

    @Operation(summary = "Met à jour un praticien", description = "Met à jour les informations d'un praticien existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Praticien mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Praticien non trouvé"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Practitioner> updatePractitioner(
            @Parameter(description = "ID du praticien", required = true) @PathVariable Long id,
            @Parameter(description = "Nouvelles informations du praticien", required = true) @RequestBody Practitioner practitioner) {
        return practitionerService.updatePractitioner(id, practitioner);
    }

    @Operation(summary = "Supprime un praticien", description = "Supprime un praticien du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Praticien supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Praticien non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePractitioner(
            @Parameter(description = "ID du praticien", required = true) @PathVariable Long id) {
        return practitionerService.deletePractitioner(id);
    }

    @Operation(summary = "Récupère un praticien par email", description = "Retourne un praticien par son adresse email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Praticien trouvé"),
            @ApiResponse(responseCode = "404", description = "Praticien non trouvé")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<Practitioner> getPractitionerByEmail(
            @Parameter(description = "Email du praticien", required = true, example = "docteur.dupont@medical.com") @PathVariable String email) {
        return practitionerService.getPractitionerByEmail(email);
    }
}