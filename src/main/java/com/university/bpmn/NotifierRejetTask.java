package com.university.bpmn;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

public class NotifierRejetTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String emailEtudiant = (String) execution.getVariable("emailEtudiant");
        String motifRejet = determinerMotif(execution);

        System.out.println("=== Envoi de la notification de rejet ===");

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8080/api/notifications/email";

            String sujet = "Inscription refusée";
            String message = String.format(
                    "Bonjour,\n\n" +
                            "Nous regrettons de vous informer que votre demande d'inscription a été refusée.\n" +
                            "Motif: %s\n\n" +
                            "Cordialement,\n" +
                            "L'équipe universitaire",
                    motifRejet
            );

            Map<String, Object> request = new HashMap<>();
            request.put("destinataire", emailEtudiant != null ? emailEtudiant : "inconnu@university.com");
            request.put("sujet", sujet);
            request.put("message", message);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            restTemplate.postForEntity(url, entity, Map.class);
            System.out.println("✓ Notification de rejet envoyée");

        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de la notification de rejet: " + e.getMessage());
        }
    }

    private String determinerMotif(DelegateExecution execution) {
        Boolean etudiantValide = (Boolean) execution.getVariable("etudiantValide");
        Boolean coursDisponible = (Boolean) execution.getVariable("coursDisponible");
        Boolean enseignantDisponible = (Boolean) execution.getVariable("enseignantDisponible");

        if (etudiantValide != null && !etudiantValide) {
            return "Étudiant non trouvé ou invalide";
        }
        if (coursDisponible != null && !coursDisponible) {
            return "Cours non disponible ou complet";
        }
        if (enseignantDisponible != null && !enseignantDisponible) {
            return "Enseignant non disponible";
        }
        return "Motif non spécifié";
    }
}
