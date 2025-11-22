package com.university.rest.dto;

public class AttestationDTO {
    private String matriculeEtudiant;
    private String titreCours;
    private double note;
    private int credits;
    private String description;

    public AttestationDTO() {}

    public AttestationDTO(String matriculeEtudiant, String titreCours,
                          double note, int credits, String description) {
        this.matriculeEtudiant = matriculeEtudiant;
        this.titreCours = titreCours;
        this.note = note;
        this.credits = credits;
        this.description = description;
    }

    // Getters
    public String getMatriculeEtudiant() {
        return matriculeEtudiant;
    }

    public String getTitreCours() {
        return titreCours;
    }

    public double getNote() {
        return note;
    }

    public int getCredits() {
        return credits;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setMatriculeEtudiant(String matriculeEtudiant) {
        this.matriculeEtudiant = matriculeEtudiant;
    }

    public void setTitreCours(String titreCours) {
        this.titreCours = titreCours;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}