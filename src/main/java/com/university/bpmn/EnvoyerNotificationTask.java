package com.university.bpmn;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

public class EnvoyerNotificationTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String emailEtudiant = (String) execution.getVariable("emailEtudiant");
        String nomEtudiant = (String) execution.getVariable("nomEtudiant");
        String titreCours = (String) execution.getVariable("titreCours");

        System.out.println("=== Envoi de la notification de confirmation ===");

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8080/api/notifications/email";

            // Préparer le message
            String sujet = "Confirmation d'inscription - " + titreCours;
            String message = String.format(
                    "Bonjour %s,\n\n" +
                            "Votre inscription au cours '%s' a été confirmée avec succès.\n\n" +
                            "Cordialement,\n" +
                            "L'équipe universitaire",
                    nomEtudiant, titreCours
            );

            Map<String, Object> request = new HashMap<>();
            request.put("destinataire", emailEtudiant);
            request.put("sujet", sujet);
            request.put("message", message);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            restTemplate.postForEntity(url, entity, Map.class);
            System.out.println("✓ Notification envoyée à " + emailEtudiant);

        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de la notification: " + e.getMessage());
        }
    }
}
