package com.university.bpmn;

import com.university.soap.CoursService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class IncrementerInscritsTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long coursId = (Long) execution.getVariable("coursId");

        System.out.println("=== Incrémentation du nombre d'inscrits ===");

        try {
            URL wsdlURL = new URL("http://localhost:8080/services/CoursService?wsdl");
            QName qname = new QName("http://soap.university.com/", "CoursService");
            Service service = Service.create(wsdlURL, qname);
            CoursService coursService = service.getPort(CoursService.class);

            coursService.incrementerNombreInscrits(coursId);
            System.out.println("✓ Nombre d'inscrits incrémenté pour le cours " + coursId);

        } catch (Exception e) {
            System.err.println("Erreur lors de l'incrémentation: " + e.getMessage());
        }
    }
}
