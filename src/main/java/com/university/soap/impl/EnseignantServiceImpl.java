package com.university.soap.impl;

import com.university.model.Enseignant;
import com.university.soap.EnseignantService;
import javax.jws.WebService;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@WebService(
        serviceName = "EnseignantService",
        portName = "EnseignantPort",
        endpointInterface = "com.university.soap.EnseignantService"
)
public class EnseignantServiceImpl implements EnseignantService {

    private static final Map<Long, Enseignant> enseignants = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Enseignant ajouterEnseignant(String nom, String prenom,
                                        String specialite, String email) {
        Long id = idCounter.getAndIncrement();
        Enseignant enseignant = new Enseignant(id, nom, prenom, specialite, true, email);
        enseignants.put(id, enseignant);

        System.out.println("Enseignant ajouté: " + id + " - " + nom + " " + prenom);
        return enseignant;
    }

    @Override
    public Enseignant getEnseignant(Long id) {
        Enseignant enseignant = enseignants.get(id);
        if (enseignant == null) {
            throw new RuntimeException("Enseignant non trouvé avec l'ID: " + id);
        }
        return enseignant;
    }

    @Override
    public Enseignant modifierEnseignant(Long id, String nom, String prenom,
                                         String specialite, boolean disponible, String email) {
        Enseignant enseignant = enseignants.get(id);
        if (enseignant == null) {
            throw new RuntimeException("Enseignant non trouvé avec l'ID: " + id);
        }

        enseignant.setNom(nom);
        enseignant.setPrenom(prenom);
        enseignant.setSpecialite(specialite);
        enseignant.setDisponible(disponible);
        enseignant.setEmail(email);

        System.out.println("Enseignant modifié: " + id);
        return enseignant;
    }

    @Override
    public boolean supprimerEnseignant(Long id) {
        if (!enseignants.containsKey(id)) {
            throw new RuntimeException("Enseignant non trouvé avec l'ID: " + id);
        }

        enseignants.remove(id);
        System.out.println("Enseignant supprimé: " + id);
        return true;
    }

    @Override
    public List<Enseignant> listerEnseignants() {
        return new ArrayList<>(enseignants.values());
    }

    @Override
    public boolean verifierDisponibilite(Long id) {
        Enseignant enseignant = enseignants.get(id);
        if (enseignant == null) {
            throw new RuntimeException("Enseignant non trouvé avec l'ID: " + id);
        }
        return enseignant.isDisponible();
    }
}