package com.university.soap.impl;

import com.university.model.Cours;
import com.university.soap.CoursService;
import javax.jws.WebService;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@WebService(
        serviceName = "CoursService",
        portName = "CoursPort",
        targetNamespace = "http://soap.university.com/",
        endpointInterface = "com.university.soap.CoursService"
)
public class CoursServiceImpl implements CoursService {

    private static final Map<Long, Cours> cours = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Cours creerCours(String titre, String description, Long enseignantId,
                            int credits, String horaires, int capaciteMax) {
        Long id = idCounter.getAndIncrement();
        Cours nouveauCours = new Cours(id, titre, description, enseignantId,
                credits, horaires, capaciteMax);
        cours.put(id, nouveauCours);

        System.out.println("Cours créé: " + id + " - " + titre);
        return nouveauCours;
    }

    @Override
    public Cours getCours(Long id) {
        Cours c = cours.get(id);
        if (c == null) {
            throw new RuntimeException("Cours non trouvé avec l'ID: " + id);
        }
        return c;
    }

    @Override
    public Cours modifierCours(Long id, String titre, String description,
                               Long enseignantId, int credits, String horaires,
                               int capaciteMax) {
        Cours c = cours.get(id);
        if (c == null) {
            throw new RuntimeException("Cours non trouvé avec l'ID: " + id);
        }

        c.setTitre(titre);
        c.setDescription(description);
        c.setEnseignantId(enseignantId);
        c.setCredits(credits);
        c.setHoraires(horaires);
        c.setCapaciteMax(capaciteMax);

        System.out.println("Cours modifié: " + id);
        return c;
    }

    @Override
    public boolean supprimerCours(Long id) {
        if (!cours.containsKey(id)) {
            throw new RuntimeException("Cours non trouvé avec l'ID: " + id);
        }

        cours.remove(id);
        System.out.println("Cours supprimé: " + id);
        return true;
    }

    @Override
    public List<Cours> listerCours() {
        return new ArrayList<>(cours.values());
    }

    @Override
    public boolean verifierDisponibiliteCours(Long id) {
        Cours c = cours.get(id);
        if (c == null) {
            throw new RuntimeException("Cours non trouvé avec l'ID: " + id);
        }
        return c.isDisponible() && c.getNombreInscrits() < c.getCapaciteMax();
    }

    @Override
    public boolean incrementerNombreInscrits(Long id) {
        Cours c = cours.get(id);
        if (c == null) {
            throw new RuntimeException("Cours non trouvé avec l'ID: " + id);
        }

        if (c.getNombreInscrits() >= c.getCapaciteMax()) {
            throw new RuntimeException("Cours complet");
        }

        c.setNombreInscrits(c.getNombreInscrits() + 1);
        System.out.println("Nombre d'inscrits incrémenté pour le cours: " + id);
        return true;
    }

    @Override
    public boolean decrementerNombreInscrits(Long id) {
        Cours c = cours.get(id);
        if (c == null) {
            throw new RuntimeException("Cours non trouvé avec l'ID: " + id);
        }

        if (c.getNombreInscrits() > 0) {
            c.setNombreInscrits(c.getNombreInscrits() - 1);
            System.out.println("Nombre d'inscrits décrémenté pour le cours: " + id);
        }
        return true;
    }
}
