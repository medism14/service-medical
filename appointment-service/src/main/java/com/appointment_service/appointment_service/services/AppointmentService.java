package com.appointment_service.appointment_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.appointment_service.appointment_service.repository.AppointmentRepository;
import com.appointment_service.appointment_service.models.Appointment;
import com.appointment_service.appointment_service.models.PatientDTO;
import com.appointment_service.appointment_service.models.PractitionerDTO;
import com.appointment_service.appointment_service.clients.UserServiceClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final GoogleCalendarService googleCalendarService;
    private final UserServiceClient userServiceClient;
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
            GoogleCalendarService googleCalendarService,
            UserServiceClient userServiceClient,
            RestTemplate restTemplate) {
        this.appointmentRepository = appointmentRepository;
        this.googleCalendarService = googleCalendarService;
        this.userServiceClient = userServiceClient;
    }

    private Appointment enrichAppointment(Appointment appointment) {
        PatientDTO patient = userServiceClient.getPatientByEmail(appointment.getPatientEmail());
        PractitionerDTO practitioner = userServiceClient.getPractitionerByEmail(appointment.getPractitionerEmail());
        appointment.setPatient(patient);
        appointment.setPractitioner(practitioner);
        return appointment;
    }

    public ResponseEntity<List<Appointment>> getAllAppointments() {
        try {
            List<Appointment> appointments = appointmentRepository.findAll();
            if (appointments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            appointments.forEach(this::enrichAppointment);
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting all appointments: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Appointment> getAppointmentById(Long id) {
        try {
            Optional<Appointment> appointmentData = appointmentRepository.findById(id);
            if (appointmentData.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Appointment appointment = enrichAppointment(appointmentData.get());
            return new ResponseEntity<>(appointment, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting appointment: {}", e.getMessage());
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

            Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
            return new ResponseEntity<>(enrichAppointment(updatedAppointment), HttpStatus.OK);
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
            appointments.forEach(this::enrichAppointment);
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting appointments by patient: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Appointment>> getAppointmentsByPractitioner(String practitionerEmail) {
        try {
            List<Appointment> appointments = appointmentRepository.findByPractitionerEmail(practitionerEmail);
            if (appointments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            appointments.forEach(this::enrichAppointment);
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting appointments by practitioner: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> createAppointments() {
        try {
            List<PatientDTO> patients = userServiceClient.getAllPatients();
            List<PractitionerDTO> practitioners = userServiceClient.getAllPractitioners();

            if (patients.isEmpty() || practitioners.isEmpty()) {
                logger.error("No patients or practitioners available");
                return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            }

            int appointmentsCreated = 0;
            int maxAttempts = 10; // Pour éviter une boucle infinie
            Random random = new Random();

            while (appointmentsCreated < 5 && maxAttempts > 0) {
                PatientDTO randomPatient = patients.get(random.nextInt(patients.size()));
                PractitionerDTO randomPractitioner = practitioners.get(random.nextInt(practitioners.size()));

                LocalDateTime startTime = LocalDateTime.now()
                    .plusDays(random.nextInt(14))
                    .withHour(9 + random.nextInt(8))
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);
                LocalDateTime endTime = startTime.plusHours(1);

                Appointment appointment = new Appointment();
                appointment.setPatient(randomPatient); // Utiliser setPatient au lieu de setPatientEmail
                appointment.setPractitioner(randomPractitioner); // Utiliser setPractitioner au lieu de setPractitionerEmail
                appointment.setStartTime(startTime);
                appointment.setEndTime(endTime);
                appointment.setStatus("SCHEDULED");
                appointment.setNotes("Consultation " + randomPractitioner.getSpeciality());
                appointment.setLocation("Cabinet " + (random.nextInt(5) + 1));

                List<Appointment> overlappingAppointments = appointmentRepository.findOverlappingAppointments(
                    appointment.getPractitionerEmail(),
                    appointment.getStartTime(),
                    appointment.getEndTime()
                );

                if (overlappingAppointments.isEmpty()) {
                    String googleEventUrl = googleCalendarService.generateGoogleCalendarLink(
                        "Rendez-vous: " + appointment.getNotes(),
                        appointment.getStartTime().toString(),
                        appointment.getEndTime().toString(),
                        appointment.getNotes(),
                        "Lieu: " + appointment.getLocation()
                    );
                    appointment.setGoogleEventUrl(googleEventUrl);
                    appointmentRepository.save(appointment);
                    appointmentsCreated++;
                }
                maxAttempts--;
            }

            if (appointmentsCreated > 0) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            logger.error("Error creating appointments: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}