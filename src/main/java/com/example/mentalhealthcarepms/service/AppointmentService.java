package com.example.mentalhealthcarepms.service;

import com.example.mentalhealthcarepms.entity.Appointment;

import java.util.List;

public interface AppointmentService {

    public Appointment addAppointment(Appointment appointment);
    public Appointment getAppointmentById(int id);
    public List<Appointment> getAllAppointment();
    public List<Appointment> getAppointmentByPatientId(int patientId);
    public List<Appointment> getAppointmentByDoctorId(int doctorId);

}
