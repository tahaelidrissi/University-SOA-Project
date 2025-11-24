package com.university.rest.controller;

import com.university.rest.dto.InscriptionRequest;
import com.university.rest.dto.ErrorResponse;
import com.university.rest.dto.SuccessResponse;
import com.university.model.Inscription;
import com.university.rest.service.InscriptionService;

// Imports Camunda
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inscriptions")
@CrossOrigin(origins = "*")
public class InscriptionController {

    private final InscriptionService inscriptionService;
    private final RuntimeService runtimeService;

    // Injection par constructeur (Best Practice)
    public InscriptionController(InscriptionService inscriptionService, RuntimeService runtimeService) {
        this.inscriptionService = inscriptionService;
        this.runtimeService = runtimeService;
    }

    // --- PORTE 1 : Publique (L'étudiant/Postman clique ici) ---
    // Cette méthode DÉMARRE le processus d'orchestration BPMN
    @PostMapping
    public ResponseEntity<?> demarrerProcessusInscription(@RequestBody InscriptionRequest request) {
        try {
            // 1. On prépare les données pour Camunda
            Map<String, Object> variables = new HashMap<>();
            variables.put("matriculeEtudiant", request.getMatriculeEtudiant());
            variables.put("coursId", request.getCoursId());

            // 2. On lance le processus (correspond à l'ID dans le fichier .bpmn)
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                    "processusInscription",
                    variables
            );

            // 3. On répond que la demande est acceptée (traitement asynchrone)
            Map<String, Object> response = new HashMap<>();
            response.put("status", "EN_TRAITEMENT");
            response.put("message", "Demande reçue. Le processus de validation a démarré.");
            response.put("processInstanceId", processInstance.getId());

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors du lancement du processus", e.getMessage()));
        }
    }

    // --- PORTE 2 : Technique (Le robot BPMN appelle ici) ---
    // Cette méthode fait l'enregistrement RÉEL en base de données
    @PostMapping("/save")
    public ResponseEntity<?> sauvegarderInscriptionReelle(@RequestBody InscriptionRequest request) {
        try {
            // Appel direct au service métier
            Inscription inscription = inscriptionService.creerInscription(
                    request.getMatriculeEtudiant(),
                    request.getCoursId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(inscription);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // --- Méthodes de lecture (inchangées) ---

    @GetMapping("/{id}")
    public ResponseEntity<?> getInscription(@PathVariable Long id) {
        try {
            Inscription inscription = inscriptionService.getInscription(id);
            return ResponseEntity.ok(inscription);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Inscription>> listerInscriptions() {
        return ResponseEntity.ok(inscriptionService.listerInscriptions());
    }

    @GetMapping("/etudiant/{matricule}")
    public ResponseEntity<List<Inscription>> getInscriptionsEtudiant(@PathVariable String matricule) {
        return ResponseEntity.ok(inscriptionService.getInscriptionsByEtudiant(matricule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> annulerInscription(@PathVariable Long id) {
        try {
            inscriptionService.annulerInscription(id);
            return ResponseEntity.ok(new SuccessResponse("Inscription annulée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}