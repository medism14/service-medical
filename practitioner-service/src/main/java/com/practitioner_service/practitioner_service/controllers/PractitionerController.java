package com.practitioner_service.practitioner_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.practitioner_service.practitioner_service.models.Practitioner;
import com.practitioner_service.practitioner_service.services.PractitionerService;

@RestController
@RequestMapping("/practitioners")
public class PractitionerController {

    private final PractitionerService practitionerService;

    @Autowired
    public PractitionerController(PractitionerService practitionerService) {
        this.practitionerService = practitionerService;
    }

    @GetMapping
    public ResponseEntity<List<Practitioner>> getAllPractitioners() {
        return practitionerService.getAllPractitioners();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Practitioner> getPractitionerById(@PathVariable Long id) {
        return practitionerService.getPractitionerById(id);
    }

    @PostMapping
    public ResponseEntity<Void> createPractitioner() {
        return practitionerService.createPractitioners();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Practitioner> updatePractitioner(@PathVariable Long id, @RequestBody Practitioner practitioner) {
        return practitionerService.updatePractitioner(id, practitioner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePractitioner(@PathVariable Long id) {
        return practitionerService.deletePractitioner(id);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Practitioner> getPractitionerByEmail(@PathVariable String email) {
        return practitionerService.getPractitionerByEmail(email);
    }
} 