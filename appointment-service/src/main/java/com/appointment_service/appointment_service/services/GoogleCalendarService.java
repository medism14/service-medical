package com.appointment_service.appointment_service.services;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class GoogleCalendarService {

    @Retry(name = "googleCalendarRetry", fallbackMethod = "generateGoogleCalendarLinkFallback")
    public String generateGoogleCalendarLink(String eventTitle, String startDateTime, String endDateTime,
            String description, String location) {
        String startUtc = convertToUTC(startDateTime);
        String endUtc = convertToUTC(endDateTime);

        String calendarLink = "https://www.google.com/calendar/render?action=TEMPLATE" +
                "&text=" + URLEncoder.encode(eventTitle, StandardCharsets.UTF_8) +
                "&dates=" + startUtc + "/" + endUtc +
                "&details=" + URLEncoder.encode(description, StandardCharsets.UTF_8) +
                "&location=" + URLEncoder.encode(location, StandardCharsets.UTF_8) +
                "&sf=true";

        return calendarLink;
    }

    // Méthode de fallback en cas d'échec
    public String generateGoogleCalendarLinkFallback(String eventTitle, String startDateTime, String endDateTime,
            String description, String location, Throwable e) {
        return "Erreur lors de la création de l'événement Google Calendar. Veuillez réessayer plus tard.";
    }

    // Méthode fictive pour la conversion de la date en UTC
    private String convertToUTC(String localDateTime) {
        return localDateTime.replace(" ", "T") + "Z"; // Exemple simple de conversion
    }
}
