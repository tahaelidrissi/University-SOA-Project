package com.university.bpmn;

import com.university.model.Cours;
import com.university.model.Enseignant;
import com.university.model.Inscription;
import com.university.model.SuiviAcademique;
import com.university.rest.service.InscriptionService;
import com.university.rest.service.NotificationService;
import com.university.rest.service.SuiviAcademiqueService;
import com.university.soap.impl.CoursServiceImpl;
import com.university.soap.impl.EnseignantServiceImpl;
import com.university.soap.impl.EtudiantServiceImpl;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de bout en bout du processus BPMN « processusInscription ».
 * Les JavaDelegates appellent http://localhost:8080 (SOAP + REST) :
 * l'application est donc démarrée sur le port 8080 (qui doit être libre).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "server.port=8080")
class ProcessusInscriptionTest {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private InscriptionService inscriptionService;

    @Autowired
    private SuiviAcademiqueService suiviService;

    @Autowired
    private NotificationService notificationService;

    // Les implémentations SOAP partagent leur stockage statique avec les endpoints publiés.
    private final EtudiantServiceImpl etudiants = new EtudiantServiceImpl();
    private final EnseignantServiceImpl enseignants = new EnseignantServiceImpl();
    private final CoursServiceImpl cours = new CoursServiceImpl();

    private ResponseEntity<Map> demanderInscription(String matricule, Long coursId) {
        Map<String, Object> body = new HashMap<>();
        body.put("matriculeEtudiant", matricule);
        body.put("coursId", coursId);
        return rest.postForEntity("/api/inscriptions", body, Map.class);
    }

    private HistoricProcessInstance instance(ResponseEntity<Map> response) {
        String id = (String) response.getBody().get("processInstanceId");
        return historyService.createHistoricProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    @Test
    void inscriptionValide_parcourtToutLeProcessus() {
        String matricule = "IT-" + UUID.randomUUID();
        String email = matricule + "@example.com";
        etudiants.creerEtudiant("Alami", "Sara", matricule, "ASEDS", 2, email);
        Enseignant prof = enseignants.ajouterEnseignant("Bennani", "Youssef", "SOA", "prof@example.com");
        Cours c = cours.creerCours("Architecture SOA", "", prof.getId(), 4, "Lundi 9h", 30);

        ResponseEntity<Map> response = demanderInscription(matricule, c.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(instance(response).getEndActivityId()).isEqualTo("endEvent4");

        List<Inscription> inscriptions = inscriptionService.getInscriptionsByEtudiant(matricule);
        assertThat(inscriptions).hasSize(1);
        assertThat(inscriptions.get(0).getCoursId()).isEqualTo(c.getId());

        assertThat(cours.getCours(c.getId()).getNombreInscrits()).isEqualTo(1);

        SuiviAcademique suivi = suiviService.getSuiviAcademique(matricule);
        assertThat(suivi.getCoursHistorique()).extracting(h -> h.getResultat()).containsExactly("EN_COURS");

        assertThat(notificationService.getHistorique(email)).isNotEmpty();
    }

    @Test
    void etudiantInconnu_estRejete() {
        Enseignant prof = enseignants.ajouterEnseignant("A", "B", "SOA", "a@example.com");
        Cours c = cours.creerCours("Cours", "", prof.getId(), 2, "Mardi", 10);
        String matricule = "INCONNU-" + UUID.randomUUID();

        ResponseEntity<Map> response = demanderInscription(matricule, c.getId());

        assertThat(instance(response).getEndActivityId()).isEqualTo("endEvent1");
        assertThat(inscriptionService.getInscriptionsByEtudiant(matricule)).isEmpty();
    }

    @Test
    void coursComplet_estRejete() {
        String matricule = "IT-" + UUID.randomUUID();
        etudiants.creerEtudiant("A", "B", matricule, "F", 1, "x@example.com");
        Enseignant prof = enseignants.ajouterEnseignant("A", "B", "SOA", "a@example.com");
        Cours c = cours.creerCours("Complet", "", prof.getId(), 2, "Mardi", 1);
        cours.incrementerNombreInscrits(c.getId());

        ResponseEntity<Map> response = demanderInscription(matricule, c.getId());

        assertThat(instance(response).getEndActivityId()).isEqualTo("endEvent2");
        assertThat(inscriptionService.getInscriptionsByEtudiant(matricule)).isEmpty();
    }

    @Test
    void enseignantIndisponible_estRejete() {
        String matricule = "IT-" + UUID.randomUUID();
        etudiants.creerEtudiant("A", "B", matricule, "F", 1, "y@example.com");
        Enseignant prof = enseignants.ajouterEnseignant("A", "B", "SOA", "a@example.com");
        enseignants.modifierEnseignant(prof.getId(), "A", "B", "SOA", false, "a@example.com");
        Cours c = cours.creerCours("Cours", "", prof.getId(), 2, "Mardi", 10);

        ResponseEntity<Map> response = demanderInscription(matricule, c.getId());

        assertThat(instance(response).getEndActivityId()).isEqualTo("endEvent3");
        assertThat(inscriptionService.getInscriptionsByEtudiant(matricule)).isEmpty();
    }
}
