package com.university.rest.service;

import com.university.model.Notification;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final Map<Long, Notification> notifications = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong(1);

    public Notification envoyerEmail(String destinataire, String sujet, String message) {
        Long id = idCounter.getAndIncrement();
        Notification notification = new Notification(id, destinataire, "EMAIL", sujet, message);

        // Simuler l'envoi d'email
        System.out.println("=== EMAIL ENVOYÉ ===");
        System.out.println("À: " + destinataire);
        System.out.println("Sujet: " + sujet);
        System.out.println("Message: " + message);
        System.out.println("==================");

        notification.setEnvoye(true);
        notifications.put(id, notification);

        return notification;
    }

    public Notification envoyerSMS(String destinataire, String message) {
        Long id = idCounter.getAndIncrement();
        Notification notification = new Notification(id, destinataire, "SMS", null, message);

        // Simuler l'envoi de SMS
        System.out.println("=== SMS ENVOYÉ ===");
        System.out.println("Au: " + destinataire);
        System.out.println("Message: " + message);
        System.out.println("=================");

        notification.setEnvoye(true);
        notifications.put(id, notification);

        return notification;
    }

    public List<Notification> getHistorique(String userId) {
        return notifications.values().stream()
                .filter(n -> n.getDestinataire().contains(userId))
                .collect(Collectors.toList());
    }
}