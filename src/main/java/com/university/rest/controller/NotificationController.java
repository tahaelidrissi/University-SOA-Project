package com.university.rest.controller;
import com.university.rest.dto.NotificationRequest;

import com.university.model.Notification;
import com.university.rest.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.university.rest.dto.ErrorResponse;
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/email")
    public ResponseEntity<?> envoyerEmail(@RequestBody NotificationRequest request) {
        try {
            Notification notification = notificationService.envoyerEmail(
                    request.getDestinataire(),
                    request.getSujet(),
                    request.getMessage()
            );
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/sms")
    public ResponseEntity<?> envoyerSMS(@RequestBody NotificationRequest request) {
        try {
            Notification notification = notificationService.envoyerSMS(
                    request.getDestinataire(),
                    request.getMessage()
            );
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/historique/{userId}")
    public ResponseEntity<List<Notification>> getHistorique(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.getHistorique(userId));
    }
}
