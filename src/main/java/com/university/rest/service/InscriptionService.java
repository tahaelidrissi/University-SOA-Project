package com.university.rest.service;

import com.university.model.Inscription;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class InscriptionService {

    private static final Map<Long, Inscription> inscriptions = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong(1);

    public Inscription creerInscription(String matriculeEtudiant, Long coursId) {
        // Vérifier si l'étudiant n'est pas déjà inscrit à ce cours
        boolean dejaInscrit = inscriptions.values().stream()
                .anyMatch(i -> i.getMatriculeEtudiant().equals(matriculeEtudiant)
                        && i.getCoursId().equals(coursId)
                        && i.getStatut().equals("ACTIVE"));

        if (dejaInscrit) {
            throw new RuntimeException("Étudiant déjà inscrit à ce cours");
        }

        Long id = idCounter.getAndIncrement();
        Inscription inscription = new Inscription(id, matriculeEtudiant, coursId);
        inscriptions.put(id, inscription);

        System.out.println("Inscription créée: " + id);
        return inscription;
    }

    public Inscription getInscription(Long id) {
        Inscription inscription = inscriptions.get(id);
        if (inscription == null) {
            throw new RuntimeException("Inscription non trouvée");
        }
        return inscription;
    }

    public List<Inscription> listerInscriptions() {
        return new ArrayList<>(inscriptions.values());
    }

    public List<Inscription> getInscriptionsByEtudiant(String matricule) {
        return inscriptions.values().stream()
                .filter(i -> i.getMatriculeEtudiant().equals(matricule))
                .collect(Collectors.toList());
    }

    public void annulerInscription(Long id) {
        Inscription inscription = inscriptions.get(id);
        if (inscription == null) {
            throw new RuntimeException("Inscription non trouvée");
        }

        inscription.setStatut("ANNULEE");
        System.out.println("Inscription annulée: " + id);
    }
}