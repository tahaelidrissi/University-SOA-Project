package com.university.soap.impl;

import com.university.model.Enseignant;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnseignantServiceImplTest {

    private final EnseignantServiceImpl service = new EnseignantServiceImpl();

    @Test
    void ajouterEnseignant_estDisponibleParDefaut() {
        Enseignant e = service.ajouterEnseignant("Bennani", "Youssef", "Génie logiciel", "y@b.ma");

        assertThat(e.getId()).isNotNull();
        assertThat(e.isDisponible()).isTrue();
        assertThat(service.verifierDisponibilite(e.getId())).isTrue();
        assertThat(service.getEnseignant(e.getId()).getSpecialite()).isEqualTo("Génie logiciel");
    }

    @Test
    void modifierEnseignant_peutLeRendreIndisponible() {
        Enseignant e = service.ajouterEnseignant("A", "B", "Réseaux", "a@b.c");

        service.modifierEnseignant(e.getId(), "A", "B", "Réseaux", false, "a@b.c");

        assertThat(service.verifierDisponibilite(e.getId())).isFalse();
    }

    @Test
    void supprimerEnseignant_puisAccesImpossible() {
        Enseignant e = service.ajouterEnseignant("A", "B", "Maths", "a@b.c");

        assertThat(service.supprimerEnseignant(e.getId())).isTrue();
        assertThat(service.listerEnseignants()).extracting(Enseignant::getId).doesNotContain(e.getId());
        assertThatThrownBy(() -> service.getEnseignant(e.getId())).hasMessageContaining("non trouvé");
        assertThatThrownBy(() -> service.verifierDisponibilite(e.getId())).hasMessageContaining("non trouvé");
    }
}
