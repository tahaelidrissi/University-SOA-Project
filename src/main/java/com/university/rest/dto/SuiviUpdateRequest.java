package com.university.rest.dto;

public class SuiviUpdateRequest {
    private Long coursId;
    private String titreCours;
    private double note;
    private int credits;

    public SuiviUpdateRequest() {}

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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}