package com.appointment_service.appointment_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appointment_service.appointment_service.repository.AppointmentRepository;
import com.appointment_service.appointment_service.models.Appointment;
import com.appointment_service.appointment_service.models.PatientDTO;
import com.appointment_service.appointment_service.models.PractitionerDTO;
import com.appointment_service.appointment_service.clients.UserServiceClient;
import com.appointment_service.appointment_service.models.AppointmentResponseDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final GoogleCalendarService googleCalendarService;
    private final UserServiceClient userServiceClient;
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
            GoogleCalendarService googleCalendarService,
            UserServiceClient userServiceClient) {
        this.appointmentRepository = appointmentRepository;
        this.googleCalendarService = googleCalendarService;
        this.userServiceClient = userServiceClient;
    }

    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        try {
            List<Appointment> appointments = appointmentRepository.findAll();
            if (appointments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<AppointmentResponseDTO> responseDTOs = appointments.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting all appointments: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private AppointmentResponseDTO convertToResponseDTO(Appointment appointment) {
        PatientDTO patient = userServiceClient.getPatientByEmail(appointment.getPatientEmail());
        PractitionerDTO practitioner = userServiceClient.getPractitionerByEmail(appointment.getPractitionerEmail());
        return new AppointmentResponseDTO(appointment, patient, practitioner);
    }

    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(Long id) {
        try {
            Optional<Appointment> appointmentData = appointmentRepository.findById(id);
            if (appointmentData.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            AppointmentResponseDTO responseDTO = convertToResponseDTO(appointmentData.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting appointment: {}", e.getMessage());
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

    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByPatient(String patientEmail) {
        try {
            List<Appointment> appointments = appointmentRepository.findByPatientEmail(patientEmail);
            if (appointments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<AppointmentResponseDTO> responseDTOs = appointments.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting appointments by patient: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByPractitioner(String practitionerEmail) {
        try {
            List<Appointment> appointments = appointmentRepository.findByPractitionerEmail(practitionerEmail);
            if (appointments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<AppointmentResponseDTO> responseDTOs = appointments.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
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
                logger.error("No valid patients or practitioners available");
                return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            }

            int appointmentsCreated = 0;
            int maxAttempts = 10;
            Random random = new Random();

            while (appointmentsCreated < 5 && maxAttempts > 0) {
                try {
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
                    appointment.setPatientEmail(randomPatient.getEmail());
                    appointment.setPractitionerEmail(randomPractitioner.getEmail());
                    appointment.setStartTime(startTime);
                    appointment.setEndTime(endTime);
                    appointment.setStatus("SCHEDULED");
                    appointment.setReason("Consultation " +
                            (randomPractitioner.getSpeciality() != null ? randomPractitioner.getSpeciality()
                                    : "Générale"));

                    try {
                        String calendarLink = googleCalendarService.generateGoogleCalendarLink(
                                "Rendez-vous médical",
                                startTime.format(formatter),
                                endTime.format(formatter),
                                appointment.getReason(),
                                "Cabinet médical");
                        appointment.setUrlCalendar(calendarLink);
                    } catch (Exception e) {
                        logger.warn("Could not generate Google Calendar link: {}", e.getMessage());
                    }

                    appointmentRepository.save(appointment);
                    logger.info("Created appointment for patient: {} and practitioner: {}",
                            appointment.getPatientEmail(),
                            appointment.getPractitionerEmail());
                    appointmentsCreated++;
                } catch (Exception e) {
                    logger.error("Failed to create appointment attempt: {}", e.getMessage());
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