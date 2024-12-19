package com.appointment_service.appointment_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class GoogleCalendarService {
    private static final Logger logger = LoggerFactory.getLogger(GoogleCalendarService.class);

    public String generateGoogleCalendarLink(String title, String startTime, String endTime, String description, String location) {
        try {
            String baseUrl = "https://calendar.google.com/calendar/render?action=TEMPLATE";
            
            // Encoder les paramètres pour l'URL
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
            String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);
            String encodedDescription = URLEncoder.encode(description, StandardCharsets.UTF_8);
            
            // Formater les dates pour Google Calendar (format YYYYMMDDTHHMMSS)
            String formattedStartTime = startTime.replaceAll("[- :]", "");
            String formattedEndTime = endTime.replaceAll("[- :]", "");
            String dates = formattedStartTime + "/" + formattedEndTime;
            
            // Construire l'URL complète
            return String.format("%s&text=%s&dates=%s&details=%s&location=%s",
                baseUrl, encodedTitle, dates, encodedDescription, encodedLocation);
                
        } catch (Exception e) {
            logger.error("Error generating Google Calendar link: {}", e.getMessage());
            return null;
        }
    }
}
