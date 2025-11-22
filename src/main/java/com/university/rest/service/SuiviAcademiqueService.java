package com.university.rest.service;
import com.university.rest.dto.AttestationDTO;
import com.university.model.SuiviAcademique;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import com.university.model.CoursHistorique;

@Service
public class SuiviAcademiqueService {

    private static final Map<String, SuiviAcademique> suivis = new ConcurrentHashMap<>();

    public SuiviAcademique creerSuiviAcademique(String matricule) {
        if (suivis.containsKey(matricule)) {
            throw new RuntimeException("Le suivi académique existe déjà pour cet étudiant");
        }

        SuiviAcademique suivi = new SuiviAcademique(matricule);
        suivis.put(matricule, suivi);

        System.out.println("Suivi académique créé pour: " + matricule);
        return suivi;
    }

    public SuiviAcademique getSuiviAcademique(String matricule) {
        SuiviAcademique suivi = suivis.get(matricule);
        if (suivi == null) {
            // Créer automatiquement si n'existe pas
            suivi = creerSuiviAcademique(matricule);
        }
        return suivi;
    }

    public SuiviAcademique ajouterCoursAuSuivi(String matricule, Long coursId,
                                               String titreCours, double note, int credits) {
        SuiviAcademique suivi = getSuiviAcademique(matricule);

        // Déterminer le résultat
        String resultat;
        if (note >= 10.0) {
            resultat = "REUSSI";
        } else if (note >= 0) {
            resultat = "ECHOUE";
        } else {
            resultat = "EN_COURS";
        }

        // Créer l'historique du cours
        CoursHistorique coursHist = new CoursHistorique(
                coursId, titreCours, note, resultat, credits
        );

        suivi.getCoursHistorique().add(coursHist);

        // Recalculer la moyenne et les crédits
        recalculerStatistiques(suivi);

        System.out.println("Cours ajouté au suivi de " + matricule + ": " + titreCours);
        return suivi;
    }

    private void recalculerStatistiques(SuiviAcademique suivi) {
        List<CoursHistorique> coursReussis = suivi.getCoursHistorique().stream()
                .filter(c -> c.getResultat().equals("REUSSI"))
                .collect(Collectors.toList());

        if (!coursReussis.isEmpty()) {
            double sommeNotes = coursReussis.stream()
                    .mapToDouble(CoursHistorique::getNote)
                    .sum();
            suivi.setMoyenneGenerale(sommeNotes / coursReussis.size());

            int totalCredits = coursReussis.stream()
                    .mapToInt(CoursHistorique::getCredits)
                    .sum();
            suivi.setTotalCredits(totalCredits);
        }
    }

    public Map<String, Object> getNotesEtudiant(String matricule) {
        SuiviAcademique suivi = getSuiviAcademique(matricule);

        Map<String, Object> result = new HashMap<>();
        result.put("matricule", matricule);
        result.put("moyenneGenerale", suivi.getMoyenneGenerale());
        result.put("totalCredits", suivi.getTotalCredits());
        result.put("notes", suivi.getCoursHistorique().stream()
                .map(c -> {
                    Map<String, Object> note = new HashMap<>();
                    note.put("cours", c.getTitreCours());
                    note.put("note", c.getNote());
                    note.put("resultat", c.getResultat());
                    note.put("credits", c.getCredits());
                    return note;
                })
                .collect(Collectors.toList())
        );

        return result;
    }

    public List<AttestationDTO> genererAttestations(String matricule) {
        SuiviAcademique suivi = getSuiviAcademique(matricule);

        return suivi.getCoursHistorique().stream()
                .filter(c -> c.getResultat().equals("REUSSI"))
                .map(c -> new AttestationDTO(
                        matricule,
                        c.getTitreCours(),
                        c.getNote(),
                        c.getCredits(),
                        "Attestation de réussite délivrée"
                ))
                .collect(Collectors.toList());
    }
}
