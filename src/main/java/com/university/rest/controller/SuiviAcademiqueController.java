package com.university.rest.controller;
import com.university.rest.dto.SuiviUpdateRequest;
import com.university.model.SuiviAcademique;
import com.university.rest.service.SuiviAcademiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import com.university.rest.dto.ErrorResponse;
import com.university.rest.dto.AttestationDTO;


@RestController
@RequestMapping("/api/suivi")
@CrossOrigin(origins = "*")
public class SuiviAcademiqueController {

    @Autowired
    private SuiviAcademiqueService suiviService;

    @GetMapping("/{matricule}")
    public ResponseEntity<?> getSuivi(@PathVariable String matricule) {
        try {
            SuiviAcademique suivi = suiviService.getSuiviAcademique(matricule);
            return ResponseEntity.ok(suivi);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{matricule}/cours")
    public ResponseEntity<?> getHistoriqueCours(@PathVariable String matricule) {
        try {
            SuiviAcademique suivi = suiviService.getSuiviAcademique(matricule);
            return ResponseEntity.ok(suivi.getCoursHistorique());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{matricule}/notes")
    public ResponseEntity<?> getNotes(@PathVariable String matricule) {
        try {
            Map<String, Object> notes = suiviService.getNotesEtudiant(matricule);
            return ResponseEntity.ok(notes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{matricule}/attestations")
    public ResponseEntity<?> getAttestations(@PathVariable String matricule) {
        try {
            List<AttestationDTO> attestations = suiviService.genererAttestations(matricule);
            return ResponseEntity.ok(attestations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{matricule}")
    public ResponseEntity<?> mettreAJourSuivi(
            @PathVariable String matricule,
            @RequestBody SuiviUpdateRequest request) {
        try {
            SuiviAcademique suivi = suiviService.ajouterCoursAuSuivi(
                    matricule,
                    request.getCoursId(),
                    request.getTitreCours(),
                    request.getNote(),
                    request.getCredits()
            );
            return ResponseEntity.ok(suivi);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/{matricule}/init")
    public ResponseEntity<?> initialiserSuivi(@PathVariable String matricule) {
        try {
            SuiviAcademique suivi = suiviService.creerSuiviAcademique(matricule);
            return ResponseEntity.status(HttpStatus.CREATED).body(suivi);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}
