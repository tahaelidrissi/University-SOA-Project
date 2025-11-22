package com.university.bpmn;

import com.university.soap.CoursService;
import com.university.model.Cours;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class VerifierCoursTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long coursId = (Long) execution.getVariable("coursId");

        System.out.println("=== Vérification du cours: " + coursId + " ===");

        try {
            URL wsdlURL = new URL("http://localhost:8080/services/CoursService?wsdl");
            QName qname = new QName("http://soap.university.com/", "CoursService");
            Service service = Service.create(wsdlURL, qname);
            CoursService coursService = service.getPort(CoursService.class);

            // Vérifier la disponibilité du cours
            boolean disponible = coursService.verifierDisponibiliteCours(coursId);

            if (disponible) {
                Cours cours = coursService.getCours(coursId);
                execution.setVariable("coursDisponible", true);
                execution.setVariable("titreCours", cours.getTitre());
                execution.setVariable("enseignantId", cours.getEnseignantId());
                execution.setVariable("creditsCours", cours.getCredits());
                System.out.println("✓ Cours disponible: " + cours.getTitre());
            } else {
                execution.setVariable("coursDisponible", false);
                System.out.println("✗ Cours non disponible ou complet");
            }

        } catch (Exception e) {
            execution.setVariable("coursDisponible", false);
            System.err.println("Erreur lors de la vérification du cours: " + e.getMessage());
        }
    }
}
