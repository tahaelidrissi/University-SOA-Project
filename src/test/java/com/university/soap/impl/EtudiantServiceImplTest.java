package com.university.soap.impl;

import com.university.model.Etudiant;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EtudiantServiceImplTest {

    private final EtudiantServiceImpl service = new EtudiantServiceImpl();

    // Le stockage est statique : chaque test utilise un matricule unique.
    private static String matricule() {
        return "T-" + UUID.randomUUID();
    }

    @Test
    void creerEtudiant_puisLeRecuperer() {
        String m = matricule();
        Etudiant cree = service.creerEtudiant("Alami", "Sara", m, "ASEDS", 2, "sara@example.com");

        assertThat(cree.getId()).isNotNull();
        assertThat(service.getEtudiant(m).getNom()).isEqualTo("Alami");
        assertThat(service.verifierExistence(m)).isTrue();
        assertThat(service.listerEtudiants()).extracting(Etudiant::getMatricule).contains(m);
    }

    @Test
    void creerEtudiant_matriculeEnDouble_estRefuse() {
        String m = matricule();
        service.creerEtudiant("A", "B", m, "F", 1, "a@b.c");

        assertThatThrownBy(() -> service.creerEtudiant("C", "D", m, "F", 1, "c@d.e"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("existe déjà");
    }

    @Test
    void modifierEtudiant_metAJourLesChamps() {
        String m = matricule();
        service.creerEtudiant("A", "B", m, "F", 1, "a@b.c");

        Etudiant modifie = service.modifierEtudiant(m, "Nouveau", "Prenom", "SESNUM", 3, "n@p.c");

        assertThat(modifie.getNom()).isEqualTo("Nouveau");
        assertThat(modifie.getFiliere()).isEqualTo("SESNUM");
        assertThat(modifie.getAnnee()).isEqualTo(3);
    }

    @Test
    void supprimerEtudiant_leRetireDuReferentiel() {
        String m = matricule();
        service.creerEtudiant("A", "B", m, "F", 1, "a@b.c");

        assertThat(service.supprimerEtudiant(m)).isTrue();
        assertThat(service.verifierExistence(m)).isFalse();
    }

    @Test
    void etudiantInexistant_leveUneErreur() {
        String m = matricule();
        assertThatThrownBy(() -> service.getEtudiant(m)).hasMessageContaining("non trouvé");
        assertThatThrownBy(() -> service.supprimerEtudiant(m)).hasMessageContaining("non trouvé");
        assertThatThrownBy(() -> service.modifierEtudiant(m, "a", "b", "c", 1, "d"))
                .hasMessageContaining("non trouvé");
    }
}
