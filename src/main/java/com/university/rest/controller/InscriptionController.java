package com.university.rest.controller;
import com.university.rest.dto.SuiviUpdateRequest;
import com.university.model.Inscription;
import com.university.model.Cours;
import com.university.rest.service.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.university.rest.dto.InscriptionRequest;
import com.university.rest.dto.ErrorResponse;
import com.university.rest.dto.SuccessResponse;
import java.util.List;

@RestController
@RequestMapping("/api/inscriptions")
@CrossOrigin(origins = "*")
public class InscriptionController {

    @Autowired
    private InscriptionService inscriptionService;

    @PostMapping
    public ResponseEntity<?> creerInscription(@RequestBody InscriptionRequest request) {
        try {
            Inscription inscription = inscriptionService.creerInscription(
                    request.getMatriculeEtudiant(),
                    request.getCoursId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(inscription);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInscription(@PathVariable Long id) {
        try {
            Inscription inscription = inscriptionService.getInscription(id);
            return ResponseEntity.ok(inscription);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Inscription>> listerInscriptions() {
        return ResponseEntity.ok(inscriptionService.listerInscriptions());
    }

    @GetMapping("/etudiant/{matricule}")
    public ResponseEntity<List<Inscription>> getInscriptionsEtudiant(
            @PathVariable String matricule) {
        return ResponseEntity.ok(
                inscriptionService.getInscriptionsByEtudiant(matricule)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> annulerInscription(@PathVariable Long id) {
        try {
            inscriptionService.annulerInscription(id);
            return ResponseEntity.ok(new SuccessResponse("Inscription annulée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}