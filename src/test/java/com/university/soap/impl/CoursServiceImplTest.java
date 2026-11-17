package com.university.soap.impl;

import com.university.model.Cours;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CoursServiceImplTest {

    private final CoursServiceImpl service = new CoursServiceImpl();

    @Test
    void creerCours_estDisponibleEtVide() {
        Cours c = service.creerCours("SOA", "Architecture orientée services", 1L, 4, "Lundi 9h", 30);

        assertThat(c.getId()).isNotNull();
        assertThat(c.getNombreInscrits()).isZero();
        assertThat(service.verifierDisponibiliteCours(c.getId())).isTrue();
        assertThat(service.listerCours()).extracting(Cours::getId).contains(c.getId());
    }

    @Test
    void coursComplet_nEstPlusDisponible_etRefuseLesInscriptions() {
        Cours c = service.creerCours("Petit cours", "", 1L, 2, "Mardi", 2);

        assertThat(service.incrementerNombreInscrits(c.getId())).isTrue();
        assertThat(service.incrementerNombreInscrits(c.getId())).isTrue();

        assertThat(service.verifierDisponibiliteCours(c.getId())).isFalse();
        assertThatThrownBy(() -> service.incrementerNombreInscrits(c.getId()))
                .hasMessageContaining("complet");
    }

    @Test
    void decrementer_neDescendPasSousZero() {
        Cours c = service.creerCours("Cours", "", 1L, 2, "Mercredi", 5);

        service.incrementerNombreInscrits(c.getId());
        service.decrementerNombreInscrits(c.getId());
        service.decrementerNombreInscrits(c.getId());

        assertThat(service.getCours(c.getId()).getNombreInscrits()).isZero();
    }

    @Test
    void modifierCours_metAJourLesChamps() {
        Cours c = service.creerCours("Ancien", "", 1L, 2, "Jeudi", 10);

        Cours modifie = service.modifierCours(c.getId(), "Nouveau", "desc", 2L, 6, "Vendredi", 40);

        assertThat(modifie.getTitre()).isEqualTo("Nouveau");
        assertThat(modifie.getEnseignantId()).isEqualTo(2L);
        assertThat(modifie.getCapaciteMax()).isEqualTo(40);
    }

    @Test
    void coursInexistant_leveUneErreur() {
        Long id = -1L;
        assertThatThrownBy(() -> service.getCours(id)).hasMessageContaining("non trouvé");
        assertThatThrownBy(() -> service.verifierDisponibiliteCours(id)).hasMessageContaining("non trouvé");
        assertThatThrownBy(() -> service.supprimerCours(id)).hasMessageContaining("non trouvé");
    }
}
