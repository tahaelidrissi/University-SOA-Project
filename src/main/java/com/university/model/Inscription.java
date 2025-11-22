// ========== Modèle Inscription ==========
package com.university.model;

import java.time.LocalDateTime;

public class Inscription {
    private Long id;
    private String matriculeEtudiant;
    private Long coursId;
    private LocalDateTime dateInscription;
    private String statut; // ACTIVE, ANNULEE, VALIDEE

    public Inscription() {}

    public Inscription(Long id, String matriculeEtudiant, Long coursId) {
        this.id = id;
        this.matriculeEtudiant = matriculeEtudiant;
        this.coursId = coursId;
        this.dateInscription = LocalDateTime.now();
        this.statut = "ACTIVE";
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMatriculeEtudiant() { return matriculeEtudiant; }
    public void setMatriculeEtudiant(String matriculeEtudiant) {
        this.matriculeEtudiant = matriculeEtudiant;
    }

    public Long getCoursId() { return coursId; }
    public void setCoursId(Long coursId) { this.coursId = coursId; }

    public LocalDateTime getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}