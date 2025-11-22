package com.university.bpmn;

import com.university.soap.EnseignantService;
import com.university.model.Enseignant;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class VerifierEnseignantTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long enseignantId = (Long) execution.getVariable("enseignantId");

        System.out.println("=== Vérification de l'enseignant: " + enseignantId + " ===");

        try {
            URL wsdlURL = new URL("http://localhost:8080/services/EnseignantService?wsdl");
            QName qname = new QName("http://soap.university.com/", "EnseignantService");
            Service service = Service.create(wsdlURL, qname);
            EnseignantService enseignantService = service.getPort(EnseignantService.class);

            boolean disponible = enseignantService.verifierDisponibilite(enseignantId);

            if (disponible) {
                Enseignant enseignant = enseignantService.getEnseignant(enseignantId);
                execution.setVariable("enseignantDisponible", true);
                execution.setVariable("nomEnseignant", enseignant.getNom() + " " + enseignant.getPrenom());
                System.out.println("✓ Enseignant disponible: " + enseignant.getNom());
            } else {
                execution.setVariable("enseignantDisponible", false);
                System.out.println("✗ Enseignant non disponible");
            }

        } catch (Exception e) {
            execution.setVariable("enseignantDisponible", false);
            System.err.println("Erreur lors de la vérification de l'enseignant: " + e.getMessage());
        }
    }
}