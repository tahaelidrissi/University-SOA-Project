package com.university.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Cours implements Serializable {
    private Long id;
    private String titre;
    private String description;
    private Long enseignantId;
    private int credits;
    private String horaires;
    private int capaciteMax;
    private int nombreInscrits;
    private boolean disponible;

    public Cours() {}

    public Cours(Long id, String titre, String description, Long enseignantId,
                 int credits, String horaires, int capaciteMax) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.enseignantId = enseignantId;
        this.credits = credits;
        this.horaires = horaires;
        this.capaciteMax = capaciteMax;
        this.nombreInscrits = 0;
        this.disponible = true;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getEnseignantId() { return enseignantId; }
    public void setEnseignantId(Long enseignantId) { this.enseignantId = enseignantId; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getHoraires() { return horaires; }
    public void setHoraires(String horaires) { this.horaires = horaires; }

    public int getCapaciteMax() { return capaciteMax; }
    public void setCapaciteMax(int capaciteMax) { this.capaciteMax = capaciteMax; }

    public int getNombreInscrits() { return nombreInscrits; }
    public void setNombreInscrits(int nombreInscrits) { this.nombreInscrits = nombreInscrits; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}