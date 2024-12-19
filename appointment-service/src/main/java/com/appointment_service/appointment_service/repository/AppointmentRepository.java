package com.appointment_service.appointment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

import com.appointment_service.appointment_service.models.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientEmail(String patientEmail);
    List<Appointment> findByPractitionerEmail(String practitionerEmail);
    
    @Query("SELECT a FROM Appointment a WHERE a.practitionerEmail = :practitionerEmail " +
           "AND ((a.startTime BETWEEN :start AND :end) OR (a.endTime BETWEEN :start AND :end))")
    List<Appointment> findOverlappingAppointments(
        @Param("practitionerEmail") String practitionerEmail,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
} 