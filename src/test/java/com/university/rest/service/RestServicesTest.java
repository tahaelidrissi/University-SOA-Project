package com.university.rest.service;

import com.university.model.Inscription;
import com.university.model.Notification;
import com.university.model.SuiviAcademique;
import com.university.rest.dto.AttestationDTO;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class RestServicesTest {

    // Le stockage est statique : chaque test utilise un matricule unique.
    private static String matricule() {
        return "T-" + UUID.randomUUID();
    }

    @Nested
    class Inscriptions {

        private final InscriptionService service = new InscriptionService();

        @Test
        void creerInscription_estActive() {
            String m = matricule();
            Inscription i = service.creerInscription(m, 1L);

            assertThat(i.getStatut()).isEqualTo("ACTIVE");
            assertThat(service.getInscription(i.getId())).isSameAs(i);
            assertThat(service.getInscriptionsByEtudiant(m)).containsExactly(i);
        }

        @Test
        void doubleInscriptionActive_estRefusee() {
            String m = matricule();
            service.creerInscription(m, 7L);

            assertThatThrownBy(() -> service.creerInscription(m, 7L))
                    .hasMessageContaining("déjà inscrit");
        }

        @Test
        void apresAnnulation_onPeutSeReinscrire() {
            String m = matricule();
            Inscription i = service.creerInscription(m, 8L);

            service.annulerInscription(i.getId());

            assertThat(service.getInscription(i.getId()).getStatut()).isEqualTo("ANNULEE");
            assertThat(service.creerInscription(m, 8L).getStatut()).isEqualTo("ACTIVE");
        }

        @Test
        void inscriptionInexistante_leveUneErreur() {
            assertThatThrownBy(() -> service.getInscription(-1L)).hasMessageContaining("non trouvée");
            assertThatThrownBy(() -> service.annulerInscription(-1L)).hasMessageContaining("non trouvée");
        }
    }

    @Nested
    class Suivi {

        private final SuiviAcademiqueService service = new SuiviAcademiqueService();

        @Test
        void getSuivi_leCreeAutomatiquement() {
            String m = matricule();
            SuiviAcademique s = service.getSuiviAcademique(m);

            assertThat(s.getMatriculeEtudiant()).isEqualTo(m);
            assertThat(s.getCoursHistorique()).isEmpty();
        }

        @Test
        void creerSuivi_deuxFois_estRefuse() {
            String m = matricule();
            service.creerSuiviAcademique(m);

            assertThatThrownBy(() -> service.creerSuiviAcademique(m)).hasMessageContaining("existe déjà");
        }

        @Test
        void resultatSelonLaNote() {
            String m = matricule();
            service.ajouterCoursAuSuivi(m, 1L, "SOA", 15, 4);
            service.ajouterCoursAuSuivi(m, 2L, "Réseaux", 8, 3);
            service.ajouterCoursAuSuivi(m, 3L, "BPMN", -1, 2);

            assertThat(service.getSuiviAcademique(m).getCoursHistorique())
                    .extracting(c -> c.getResultat())
                    .containsExactly("REUSSI", "ECHOUE", "EN_COURS");
        }

        @Test
        void moyenneEtCredits_neComptentQueLesCoursReussis() {
            String m = matricule();
            service.ajouterCoursAuSuivi(m, 1L, "SOA", 16, 4);
            service.ajouterCoursAuSuivi(m, 2L, "Java", 12, 2);
            service.ajouterCoursAuSuivi(m, 3L, "Réseaux", 5, 3);

            SuiviAcademique s = service.getSuiviAcademique(m);
            assertThat(s.getMoyenneGenerale()).isCloseTo(14.0, within(1e-9));
            assertThat(s.getTotalCredits()).isEqualTo(6);

            Map<String, Object> notes = service.getNotesEtudiant(m);
            assertThat(notes).containsEntry("matricule", m).containsEntry("totalCredits", 6);
            assertThat((List<?>) notes.get("notes")).hasSize(3);
        }

        @Test
        void attestations_uniquementPourLesCoursReussis() {
            String m = matricule();
            service.ajouterCoursAuSuivi(m, 1L, "SOA", 14, 4);
            service.ajouterCoursAuSuivi(m, 2L, "Réseaux", 6, 3);

            List<AttestationDTO> attestations = service.genererAttestations(m);

            assertThat(attestations).extracting(AttestationDTO::getTitreCours).containsExactly("SOA");
        }
    }

    @Nested
    class Notifications {

        private final NotificationService service = new NotificationService();

        @Test
        void email_estEnvoyeEtHistorise() {
            String dest = UUID.randomUUID() + "@example.com";
            Notification n = service.envoyerEmail(dest, "Sujet", "Corps");

            assertThat(n.isEnvoye()).isTrue();
            assertThat(n.getType()).isEqualTo("EMAIL");
            assertThat(service.getHistorique(dest)).containsExactly(n);
        }

        @Test
        void sms_estEnvoyeSansSujet() {
            String dest = "+2126" + Math.abs(UUID.randomUUID().getMostSignificantBits() % 100000000L);
            Notification n = service.envoyerSMS(dest, "Code 1234");

            assertThat(n.getType()).isEqualTo("SMS");
            assertThat(n.getSujet()).isNull();
            assertThat(service.getHistorique(dest)).contains(n);
        }
    }
}
