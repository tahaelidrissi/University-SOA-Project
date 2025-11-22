package com.university.model;

import java.util.ArrayList;
import java.util.List;

public class SuiviAcademique {
    private String matriculeEtudiant;
    private List<CoursHistorique> coursHistorique;
    private double moyenneGenerale;
    private int totalCredits;

    public SuiviAcademique() {
        this.coursHistorique = new ArrayList<>();
    }

    public SuiviAcademique(String matriculeEtudiant) {
        this.matriculeEtudiant = matriculeEtudiant;
        this.coursHistorique = new ArrayList<>();
        this.moyenneGenerale = 0.0;
        this.totalCredits = 0;
    }

    // Getters et Setters
    public String getMatriculeEtudiant() { return matriculeEtudiant; }
    public void setMatriculeEtudiant(String matriculeEtudiant) {
        this.matriculeEtudiant = matriculeEtudiant;
    }

    public List<CoursHistorique> getCoursHistorique() { return coursHistorique; }
    public void setCoursHistorique(List<CoursHistorique> coursHistorique) {
        this.coursHistorique = coursHistorique;
    }

    public double getMoyenneGenerale() { return moyenneGenerale; }
    public void setMoyenneGenerale(double moyenneGenerale) {
        this.moyenneGenerale = moyenneGenerale;
    }

    public int getTotalCredits() { return totalCredits; }
    public void setTotalCredits(int totalCredits) { this.totalCredits = totalCredits; }
}
