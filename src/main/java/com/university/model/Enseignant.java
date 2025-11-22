// ========== Modèle Enseignant ==========
package com.university.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Enseignant implements Serializable {
    private Long id;
    private String nom;
    private String prenom;
    private String specialite;
    private boolean disponible;
    private String email;

    public Enseignant() {}

    public Enseignant(Long id, String nom, String prenom, String specialite,
                      boolean disponible, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
        this.disponible = disponible;
        this.email = email;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}