package com.university.bpmn;

import com.university.soap.EtudiantService;
import com.university.model.Etudiant;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class VerifierEtudiantTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Récupérer le matricule de l'étudiant depuis les variables du processus
        String matricule = (String) execution.getVariable("matriculeEtudiant");

        System.out.println("=== Vérification de l'étudiant: " + matricule + " ===");

        try {
            // Appel au service SOAP EtudiantService
            URL wsdlURL = new URL("http://localhost:8080/services/EtudiantService?wsdl");
            QName qname = new QName("http://soap.university.com/", "EtudiantService");
            Service service = Service.create(wsdlURL, qname);
            EtudiantService etudiantService = service.getPort(EtudiantService.class);

            // Vérifier si l'étudiant existe
            boolean existe = etudiantService.verifierExistence(matricule);

            if (existe) {
                Etudiant etudiant = etudiantService.getEtudiant(matricule);
                execution.setVariable("etudiantValide", true);
                execution.setVariable("nomEtudiant", etudiant.getNom() + " " + etudiant.getPrenom());
                execution.setVariable("emailEtudiant", etudiant.getEmail());
                System.out.println("✓ Étudiant trouvé: " + etudiant.getNom());
            } else {
                execution.setVariable("etudiantValide", false);
                System.out.println("✗ Étudiant non trouvé");
            }

        } catch (Exception e) {
            execution.setVariable("etudiantValide", false);
            System.err.println("Erreur lors de la vérification de l'étudiant: " + e.getMessage());
        }
    }
}