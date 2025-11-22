// ========== Modèle Notification ==========
package com.university.model;

import java.time.LocalDateTime;

public class Notification {
    private Long id;
    private String destinataire;
    private String type; // EMAIL, SMS
    private String sujet;
    private String message;
    private LocalDateTime dateEnvoi;
    private boolean envoye;

    public Notification() {}

    public Notification(Long id, String destinataire, String type,
                        String sujet, String message) {
        this.id = id;
        this.destinataire = destinataire;
        this.type = type;
        this.sujet = sujet;
        this.message = message;
        this.dateEnvoi = LocalDateTime.now();
        this.envoye = false;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDestinataire() { return destinataire; }
    public void setDestinataire(String destinataire) { this.destinataire = destinataire; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }

    public boolean isEnvoye() { return envoye; }
    public void setEnvoye(boolean envoye) { this.envoye = envoye; }
}
