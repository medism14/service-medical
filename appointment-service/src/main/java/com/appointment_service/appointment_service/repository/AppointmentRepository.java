package com.appointment_service.appointment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import com.appointment_service.appointment_service.models.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientEmail(String patientEmail);
    List<Appointment> findByPractitionerEmail(String practitionerEmail);
    
    @Query("SELECT a FROM Appointment a " +
           "WHERE a.practitionerEmail = :practitionerEmail " +
           "AND a.status != 'CANCELLED' " +
           "AND (" +
           "    (a.startTime < :endTime AND a.endTime > :startTime) OR " +
           "    (a.startTime = :startTime) OR " +
           "    (a.endTime = :endTime)" +
           ")")
    List<Appointment> findOverlappingAppointments(
        @Param("practitionerEmail") String practitionerEmail,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
} 