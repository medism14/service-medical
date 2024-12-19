package com.medical_record_service.medical_record_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medical_record_service.medical_record_service.models.MedicalRecord;
import com.medical_record_service.medical_record_service.models.MedicalRecordResponseDTO;
import com.medical_record_service.medical_record_service.services.MedicalRecordService;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/medical-records")
@Tag(name = "Medical Record Management", description = "Opérations liées à la gestion des dossiers médicaux")
@CrossOrigin(origins = "*")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @Operation(summary = "Liste tous les dossiers médicaux", description = "Retourne la liste complète des dossiers médicaux")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des dossiers médicaux récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Aucun dossier médical trouvé")
    })
    @GetMapping
    public ResponseEntity<List<MedicalRecordResponseDTO>> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @Operation(summary = "Récupère un dossier médical par ID", description = "Retourne un dossier médical unique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dossier médical trouvé"),
            @ApiResponse(responseCode = "404", description = "Dossier médical non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO> getMedicalRecordById(
            @Parameter(description = "ID du dossier médical", required = true) @PathVariable Long id) {
        return medicalRecordService.getMedicalRecordById(id);
    }

    @Operation(summary = "Crée des dossiers médicaux", description = "Crée des dossiers médicaux de test dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dossiers médicaux créés avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur lors de la création")
    })
    @PostMapping
    public ResponseEntity<Void> createMedicalRecords() {
        return medicalRecordService.createMedicalRecords();
    }

    @Operation(summary = "Met à jour un dossier médical", description = "Met à jour les informations d'un dossier médical existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dossier médical mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Dossier médical non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO> updateMedicalRecord(
            @Parameter(description = "ID du dossier médical", required = true) @PathVariable Long id,
            @Parameter(description = "Nouvelles informations du dossier médical", required = true) 
            @RequestBody MedicalRecord recordDetails) {
        return medicalRecordService.updateMedicalRecord(id, recordDetails);
    }

    @Operation(summary = "Supprime un dossier médical", description = "Supprime un dossier médical du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dossier médical supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Dossier médical non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(
            @Parameter(description = "ID du dossier médical", required = true) @PathVariable Long id) {
        return medicalRecordService.deleteMedicalRecord(id);
    }

    @Operation(summary = "Liste les dossiers médicaux d'un patient", description = "Retourne tous les dossiers médicaux d'un patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des dossiers médicaux trouvée"),
            @ApiResponse(responseCode = "404", description = "Aucun dossier médical trouvé")
    })
    @GetMapping("/patient/{email}")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getMedicalRecordsByPatient(
            @Parameter(description = "Email du patient", required = true) @PathVariable String email) {
        return medicalRecordService.getMedicalRecordsByPatient(email);
    }
} 