package com.university.bpmn;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

public class MajSuiviTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String matricule = (String) execution.getVariable("matriculeEtudiant");
        Long coursId = (Long) execution.getVariable("coursId");
        String titreCours = (String) execution.getVariable("titreCours");
        Integer credits = (Integer) execution.getVariable("creditsCours");

        System.out.println("=== Mise à jour du suivi académique ===");

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8080/api/suivi/" + matricule;

            Map<String, Object> request = new HashMap<>();
            request.put("coursId", coursId);
            request.put("titreCours", titreCours);
            request.put("note", -1.0); // -1 indique "en cours"
            request.put("credits", credits);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            restTemplate.put(url, entity);
            System.out.println("✓ Suivi académique mis à jour pour " + matricule);

        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du suivi: " + e.getMessage());
        }
    }
}
