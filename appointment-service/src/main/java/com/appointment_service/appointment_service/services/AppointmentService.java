package com.appointment_service.appointment_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.appointment_service.appointment_service.repository.AppointmentRepository;
import com.appointment_service.appointment_service.models.Appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final GoogleCalendarService googleCalendarService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
            GoogleCalendarService googleCalendarService) {
        this.appointmentRepository = appointmentRepository;
        this.googleCalendarService = googleCalendarService;
    }

    public ResponseEntity<List<Appointment>> getAllAppointments() {
        try {
            List<Appointment> appointments = appointmentRepository.findAll();
            if (appointments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Appointment> getAppointmentById(Long id) {
        try {
            Optional<Appointment> appointment = appointmentRepository.findById(id);
            return appointment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Appointment> updateAppointment(Long id, Appointment appointmentDetails) {
        try {
            Optional<Appointment> appointmentData = appointmentRepository.findById(id);
            if (appointmentData.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Appointment existingAppointment = appointmentData.get();
            if (!existingAppointment.getStartTime().equals(appointmentDetails.getStartTime()) ||
                    !existingAppointment.getEndTime().equals(appointmentDetails.getEndTime())) {

                List<Appointment> overlappingAppointments = appointmentRepository.findOverlappingAppointments(
                        appointmentDetails.getPractitionerEmail(),
                        appointmentDetails.getStartTime(),
                        appointmentDetails.getEndTime());

                overlappingAppointments.removeIf(apt -> apt.getId().equals(id));

                if (!overlappingAppointments.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }

            existingAppointment.setStartTime(appointmentDetails.getStartTime());
            existingAppointment.setEndTime(appointmentDetails.getEndTime());
            existingAppointment.setStatus(appointmentDetails.getStatus());
            existingAppointment.setNotes(appointmentDetails.getNotes());
            existingAppointment.setLocation(appointmentDetails.getLocation());

            return new ResponseEntity<>(appointmentRepository.save(existingAppointment), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> deleteAppointment(Long id) {
        try {
            Optional<Appointment> appointment = appointmentRepository.findById(id);
            if (appointment.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            appointmentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Appointment>> getAppointmentsByPatient(String patientEmail) {
        try {
            List<Appointment> appointments = appointmentRepository.findByPatientEmail(patientEmail);
            if (appointments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Appointment>> getAppointmentsByPractitioner(String practitionerEmail) {
        try {
            List<Appointment> appointments = appointmentRepository.findByPractitionerEmail(practitionerEmail);
            if (appointments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> createAppointments() {
        try {
            Object[][] appointmentsData = {
                    { "jean.dupont@example.com", "jean.dupont@med.com",
                            LocalDateTime.now().plusDays(1).withHour(9).withMinute(0),
                            LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
                            "Consultation cardiologie", "Cabinet 1" },

                    { "marie.curie@example.com", "jean.dupont@med.com",
                            LocalDateTime.now().plusDays(1).withHour(14).withMinute(0),
                            LocalDateTime.now().plusDays(1).withHour(15).withMinute(0),
                            "Suivi routine", "Cabinet 2" },

                    { "jean.dupont@example.com", "marie.curie@med.com",
                            LocalDateTime.now().plusDays(2).withHour(11).withMinute(0),
                            LocalDateTime.now().plusDays(2).withHour(12).withMinute(0),
                            "Radiologie", "Cabinet 3" },

                    { "pierre.martin@example.com", "pierre.martin@med.com",
                            LocalDateTime.now().plusDays(3).withHour(10).withMinute(0),
                            LocalDateTime.now().plusDays(3).withHour(11).withMinute(0),
                            "Consultation pédiatrie", "Cabinet 4" },

                    { "marie.curie@example.com", "sophie.durand@med.com",
                            LocalDateTime.now().plusDays(4).withHour(15).withMinute(0),
                            LocalDateTime.now().plusDays(4).withHour(16).withMinute(0),
                            "Consultation dermatologie", "Cabinet 5" }
            };

            for (Object[] data : appointmentsData) {
                Appointment appointment = new Appointment();
                appointment.setPatientEmail((String) data[0]);
                appointment.setPractitionerEmail((String) data[1]);
                appointment.setStartTime((LocalDateTime) data[2]);
                appointment.setEndTime((LocalDateTime) data[3]);
                appointment.setNotes((String) data[4]);
                appointment.setLocation((String) data[5]);
                appointment.setStatus("SCHEDULED");

                List<Appointment> overlappingAppointments = appointmentRepository.findOverlappingAppointments(
                        appointment.getPractitionerEmail(),
                        appointment.getStartTime(),
                        appointment.getEndTime());

                if (overlappingAppointments.isEmpty()) {
                    String googleEventUrl = googleCalendarService.generateGoogleCalendarLink(
                            "Rendez-vous: " + appointment.getNotes(),
                            appointment.getStartTime().toString(),
                            appointment.getEndTime().toString(),
                            appointment.getNotes(),
                            "Lieu: " + appointment.getLocation());
                    appointment.setGoogleEventUrl(googleEventUrl);
                    
                    appointmentRepository.save(appointment);
                }
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}