package com.university.soap.impl;

import com.university.model.Etudiant;
import com.university.soap.EtudiantService;
import javax.jws.WebService;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@WebService(
        serviceName = "EtudiantService",        // Nom du service SOAP
        portName = "EtudiantPort",              // Nom du port
        endpointInterface = "com.university.soap.EtudiantService"  // L'interface SEI
)
public class EtudiantServiceImpl implements EtudiantService {

    // Stockage en mémoire (en production, utiliser une base de données)
    private static final Map<String, Etudiant> etudiants = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Etudiant creerEtudiant(String nom, String prenom, String matricule,
                                  String filiere, int annee, String email) {
        // Vérifier si le matricule existe déjà
        if (etudiants.containsKey(matricule)) {
            throw new RuntimeException("Un étudiant avec ce matricule existe déjà");
        }

        // Créer l'étudiant
        Etudiant etudiant = new Etudiant(
                idCounter.getAndIncrement(),
                nom,
                prenom,
                matricule,
                filiere,
                annee,
                email
        );

        // Sauvegarder
        etudiants.put(matricule, etudiant);

        System.out.println("Étudiant créé: " + matricule + " - " + nom + " " + prenom);
        return etudiant;
    }

    @Override
    public Etudiant getEtudiant(String matricule) {
        Etudiant etudiant = etudiants.get(matricule);
        if (etudiant == null) {
            throw new RuntimeException("Étudiant non trouvé avec le matricule: " + matricule);
        }
        return etudiant;
    }

    @Override
    public Etudiant modifierEtudiant(String matricule, String nom, String prenom,
                                     String filiere, int annee, String email) {
        Etudiant etudiant = etudiants.get(matricule);
        if (etudiant == null) {
            throw new RuntimeException("Étudiant non trouvé avec le matricule: " + matricule);
        }

        // Mettre à jour les informations
        etudiant.setNom(nom);
        etudiant.setPrenom(prenom);
        etudiant.setFiliere(filiere);
        etudiant.setAnnee(annee);
        etudiant.setEmail(email);

        etudiants.put(matricule, etudiant);

        System.out.println("Étudiant modifié: " + matricule);
        return etudiant;
    }

    @Override
    public boolean supprimerEtudiant(String matricule) {
        if (!etudiants.containsKey(matricule)) {
            throw new RuntimeException("Étudiant non trouvé avec le matricule: " + matricule);
        }

        etudiants.remove(matricule);
        System.out.println("Étudiant supprimé: " + matricule);
        return true;
    }

    @Override
    public List<Etudiant> listerEtudiants() {
        return new ArrayList<>(etudiants.values());
    }

    @Override
    public boolean verifierExistence(String matricule) {
        return etudiants.containsKey(matricule);
    }
}
