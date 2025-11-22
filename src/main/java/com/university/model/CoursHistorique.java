package com.university.model;

public class CoursHistorique {
    private Long coursId;
    private String titreCours;
    private double note;
    private String resultat; // REUSSI, ECHOUE, EN_COURS
    private int credits;

    public CoursHistorique() {}

    public CoursHistorique(Long coursId, String titreCours, double note, String resultat, int credits) {
        this.coursId = coursId;
        this.titreCours = titreCours;
        this.note = note;
        this.resultat = resultat;
        this.credits = credits;
    }

    // Getters et Setters
    public Long getCoursId() {
        return coursId;
    }

    public void setCoursId(Long coursId) {
        this.coursId = coursId;
    }

    public String getTitreCours() {
        return titreCours;
    }

    public void setTitreCours(String titreCours) {
        this.titreCours = titreCours;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
