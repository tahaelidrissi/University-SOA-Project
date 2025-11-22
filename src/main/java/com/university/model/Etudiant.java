
// ========== Modèle Etudiant ==========
package com.university.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Etudiant implements Serializable {
    private Long id;
    private String nom;
    private String prenom;
    private String matricule;
    private String filiere;
    private int annee;
    private String email;

    // Constructeurs
    public Etudiant() {}

    public Etudiant(Long id, String nom, String prenom, String matricule,
                    String filiere, int annee, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.filiere = filiere;
        this.annee = annee;
        this.email = email;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getFiliere() { return filiere; }
    public void setFiliere(String filiere) { this.filiere = filiere; }

    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
