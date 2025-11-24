package com.university.bpmn;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

public class CreerInscriptionTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String matricule = (String) execution.getVariable("matriculeEtudiant");
        Long coursId = (Long) execution.getVariable("coursId");

        System.out.println("=== Création de l'inscription (Appel REST technique) ===");

        try {
            RestTemplate restTemplate = new RestTemplate();

            // CORRECTION IMPORTANTE : On appelle le endpoint "/save"
            // pour ne pas relancer le processus BPMN
            String url = "http://localhost:8080/api/inscriptions/save";

            // Préparer la requête
            Map<String, Object> request = new HashMap<>();
            request.put("matriculeEtudiant", matricule);
            request.put("coursId", coursId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            // Appel au service REST technique
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                Map<String, Object> inscription = response.getBody();
                // On récupère l'ID généré pour la suite du processus
                execution.setVariable("inscriptionId", inscription.get("id"));
                execution.setVariable("inscriptionCreee", true);
                System.out.println("✓ Inscription technique réussie: ID " + inscription.get("id"));
            } else {
                execution.setVariable("inscriptionCreee", false);
                System.out.println("✗ Échec de l'inscription technique");
            }

        } catch (Exception e) {
            execution.setVariable("inscriptionCreee", false);
            System.err.println("Erreur critique lors de la création de l'inscription: " + e.getMessage());
            // Optionnel : relancer l'exception si on veut créer un incident dans Camunda Cockpit
            // throw e;
        }
    }
}