package com.example.mentalhealthcarepms.repository;

import com.example.mentalhealthcarepms.entity.Appointment;
import com.example.mentalhealthcarepms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByPatientId(int patientId);
    List<Appointment> findByDoctorId(int doctorId);
}
