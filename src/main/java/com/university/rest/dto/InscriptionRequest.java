package com.university.rest.dto;

public class InscriptionRequest {
    private String matriculeEtudiant;
    private Long coursId;

    // Constructeurs
    public InscriptionRequest() {}

    public InscriptionRequest(String matriculeEtudiant, Long coursId) {
        this.matriculeEtudiant = matriculeEtudiant;
        this.coursId = coursId;
    }

    // Getters et Setters
    public String getMatriculeEtudiant() {
        return matriculeEtudiant;
    }

    public void setMatriculeEtudiant(String matriculeEtudiant) {
        this.matriculeEtudiant = matriculeEtudiant;
    }

    public Long getCoursId() {
        return coursId;
    }

    public void setCoursId(Long coursId) {
        this.coursId = coursId;
    }
}