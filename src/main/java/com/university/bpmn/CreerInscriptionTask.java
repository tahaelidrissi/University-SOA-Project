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

        System.out.println("=== Création de l'inscription ===");

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8080/api/inscriptions";

            // Préparer la requête
            Map<String, Object> request = new HashMap<>();
            request.put("matriculeEtudiant", matricule);
            request.put("coursId", coursId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            // Appel au service REST
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                Map<String, Object> inscription = response.getBody();
                execution.setVariable("inscriptionId", inscription.get("id"));
                execution.setVariable("inscriptionCreee", true);
                System.out.println("✓ Inscription créée avec succès: ID " + inscription.get("id"));
            } else {
                execution.setVariable("inscriptionCreee", false);
                System.out.println("✗ Échec de la création de l'inscription");
            }

        } catch (Exception e) {
            execution.setVariable("inscriptionCreee", false);
            System.err.println("Erreur lors de la création de l'inscription: " + e.getMessage());
        }
    }
}
