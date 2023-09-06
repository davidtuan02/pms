package com.example.mentalhealthcarepms.controller;

import com.example.mentalhealthcarepms.dto.AppointmentResponse;
import com.example.mentalhealthcarepms.dto.ResponseObject;
import com.example.mentalhealthcarepms.dto.UpdateAppointmentRequest;
import com.example.mentalhealthcarepms.entity.Appointment;
import com.example.mentalhealthcarepms.entity.User;
import com.example.mentalhealthcarepms.exception.AppointmentNotFoundException;
import com.example.mentalhealthcarepms.service.AppointmentService;
import com.example.mentalhealthcarepms.service.UserService;
import com.example.mentalhealthcarepms.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @PostMapping("/patient/add")
    public ResponseEntity<?> addAppointment(@RequestBody Appointment appointment) {

        ResponseObject responseObject = new ResponseObject();

        if(appointment == null) {
            responseObject.setResponseCode(Constants.ResponseCode.FAIL.value());
            responseObject.setResponseMessage("Failed");
            return new ResponseEntity(responseObject, HttpStatus.BAD_REQUEST);
        }

        if(appointment.getPatientId() == 0) {
            responseObject.setResponseCode(Constants.ResponseCode.FAIL.value());
            responseObject.setResponseMessage("Failed");
            return new ResponseEntity(responseObject, HttpStatus.BAD_REQUEST);
        }

        appointment.setDate(LocalDate.now().toString());
        appointment.setStatus(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
        Appointment addedAppointment = appointmentService.addAppointment(appointment);

        if(addedAppointment != null) {
            responseObject.setResponseCode(Constants.ResponseCode.SUCCESS.value());
            responseObject.setResponseMessage("add patient appointment successfully");
            return new ResponseEntity(responseObject, HttpStatus.OK);
        }
        else {
            responseObject.setResponseCode(Constants.ResponseCode.FAIL.value());
            responseObject.setResponseMessage("Failed");
            return new ResponseEntity(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllAppointments() {

        List<Appointment> appointments = appointmentService.getAllAppointment();
        List<AppointmentResponse> responses = new ArrayList<>();

        for (Appointment appointment : appointments) {

            AppointmentResponse appointmentResponse = new AppointmentResponse();
            User patient = userService.getUserById(appointment.getPatientId());
            appointmentResponse.setPatientContact(patient.getContact());
            appointmentResponse.setPatientId(patient.getId());
            appointmentResponse.setPatientName(patient.getFirstname() + " " + patient.getLastname());

            if (appointment.getDoctorId() != 0) {
                User doctor = this.userService.getUserById(appointment.getDoctorId());
                appointmentResponse.setDoctorContact(doctor.getContact());
                appointmentResponse.setDoctorName(doctor.getFirstname() + " " + doctor.getLastname());
                appointmentResponse.setDoctorId(doctor.getId());
                appointmentResponse.setPrescription(appointment.getPresciption());

                if (appointment.getStatus().equals(Constants.AppointmentStatus.TREATMENT_DONE.value())) {
                    appointmentResponse.setPrice(String.valueOf(appointment.getPrice()));
                } else {
                    appointmentResponse.setPrice(Constants.AppointmentStatus.PENDING.value());
                }
            } else {
                appointmentResponse.setDoctorContact(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
                appointmentResponse.setDoctorName(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
                appointmentResponse.setDoctorId(0);
                appointmentResponse.setPrice(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
                appointmentResponse.setPrescription(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
            }

            appointmentResponse.setStatus(appointment.getStatus());
            appointmentResponse.setProblem(appointment.getProblem());
            appointmentResponse.setDate(appointment.getDate());
            appointmentResponse.setAppointmentDate(appointment.getDate());
            appointmentResponse.setId(appointment.getId());
        }

        return new ResponseEntity(responses, HttpStatus.OK);
    }



    @GetMapping("/id")
    public ResponseEntity<?> getAllAppointmentsById(@RequestParam("appointmentId") int appointmentId) {

        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        if(appointment ==  null) {
            throw new AppointmentNotFoundException();
        }

        AppointmentResponse appointmentResponse = new AppointmentResponse();

        User patient = userService.getUserById(appointment.getPatientId());
        appointmentResponse.setPatientContact(patient.getContact());
        appointmentResponse.setPatientId(patient.getId());
        appointmentResponse.setPatientName(patient.getFirstname() + " " + patient.getLastname());

        if (appointment.getDoctorId() != 0) {
            User doctor = this.userService.getUserById(appointment.getDoctorId());
            appointmentResponse.setDoctorContact(doctor.getContact());
            appointmentResponse.setDoctorName(doctor.getFirstname() + " " + doctor.getLastname());
            appointmentResponse.setDoctorId(doctor.getId());
            appointmentResponse.setPrescription(appointment.getPresciption());

            if (appointment.getStatus().equals(Constants.AppointmentStatus.TREATMENT_DONE.value())) {
                appointmentResponse.setPrice(String.valueOf(appointment.getPrice()));
            } else {
                appointmentResponse.setPrice(Constants.AppointmentStatus.PENDING.value());
            }
        } else {
            appointmentResponse.setDoctorContact(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
            appointmentResponse.setDoctorName(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
            appointmentResponse.setDoctorId(0);
            appointmentResponse.setPrice(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
            appointmentResponse.setPrescription(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
        }

        appointmentResponse.setStatus(appointment.getStatus());
        appointmentResponse.setProblem(appointment.getProblem());
        appointmentResponse.setDate(appointment.getDate());
        appointmentResponse.setAppointmentDate(appointment.getDate());
        appointmentResponse.setId(appointment.getId());

        return new ResponseEntity(appointmentResponse, HttpStatus.OK);
    }



    @GetMapping("/patient/id")
    public ResponseEntity<?> getAllAppointmentsByPatientId(@RequestParam("patientId") int patientId) {

        List<Appointment> appointments = appointmentService.getAppointmentByPatientId(patientId);
        List<AppointmentResponse> responses = new ArrayList<>();

        for (Appointment appointment : appointments) {
            AppointmentResponse appointmentResponse = new AppointmentResponse();
            User patient = userService.getUserById(appointment.getPatientId());
            appointmentResponse.setPatientContact(patient.getContact());
            appointmentResponse.setPatientId(patient.getId());
            appointmentResponse.setPatientName(patient.getFirstname() + " " + patient.getLastname());

            if (appointment.getDoctorId() != 0) {
                User doctor = this.userService.getUserById(appointment.getDoctorId());
                appointmentResponse.setDoctorContact(doctor.getContact());
                appointmentResponse.setDoctorName(doctor.getFirstname() + " " + doctor.getLastname());
                appointmentResponse.setDoctorId(doctor.getId());
                appointmentResponse.setPrescription(appointment.getPresciption());

                if (appointment.getStatus().equals(Constants.AppointmentStatus.TREATMENT_DONE.value())) {
                    appointmentResponse.setPrice(String.valueOf(appointment.getPrice()));
                } else {
                    appointmentResponse.setPrice(Constants.AppointmentStatus.PENDING.value());
                }
            } else {
                appointmentResponse.setDoctorContact(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
                appointmentResponse.setDoctorName(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
                appointmentResponse.setDoctorId(0);
                appointmentResponse.setPrice(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
                appointmentResponse.setPrescription(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
            }

            appointmentResponse.setStatus(appointment.getStatus());
            appointmentResponse.setProblem(appointment.getProblem());
            appointmentResponse.setDate(appointment.getDate());
            appointmentResponse.setAppointmentDate(appointment.getDate());
            appointmentResponse.setId(appointment.getId());
        }

        return new ResponseEntity(responses, HttpStatus.OK);
    }



    @GetMapping("/doctor/id")
    public ResponseEntity<?> getAllAppointmentsByDoctorId(@RequestParam("doctorId") int doctorId) {

        List<Appointment> appointments = appointmentService.getAppointmentByDoctorId(doctorId);
        List<AppointmentResponse> responses = new ArrayList<>();

        for (Appointment appointment : appointments) {
            AppointmentResponse appointmentResponse = new AppointmentResponse();
            User patient = userService.getUserById(appointment.getPatientId());
            appointmentResponse.setPatientContact(patient.getContact());
            appointmentResponse.setPatientId(patient.getId());
            appointmentResponse.setPatientName(patient.getFirstname() + " " + patient.getLastname());

            if (appointment.getDoctorId() != 0) {
                User doctor = this.userService.getUserById(appointment.getDoctorId());
                appointmentResponse.setDoctorContact(doctor.getContact());
                appointmentResponse.setDoctorName(doctor.getFirstname() + " " + doctor.getLastname());
                appointmentResponse.setDoctorId(doctor.getId());
                appointmentResponse.setPrescription(appointment.getPresciption());

                if (appointment.getStatus().equals(Constants.AppointmentStatus.TREATMENT_DONE.value())) {
                    appointmentResponse.setPrice(String.valueOf(appointment.getPrice()));
                } else {
                    appointmentResponse.setPrice(Constants.AppointmentStatus.PENDING.value());
                }
            } else {
                appointmentResponse.setDoctorContact(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
                appointmentResponse.setDoctorName(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
                appointmentResponse.setDoctorId(0);
                appointmentResponse.setPrice(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
                appointmentResponse.setPrescription(Constants.AppointmentStatus.NOT_ASSIGNED_TO_DOCTOR.value());
            }

            appointmentResponse.setStatus(appointment.getStatus());
            appointmentResponse.setProblem(appointment.getProblem());
            appointmentResponse.setDate(appointment.getDate());
            appointmentResponse.setAppointmentDate(appointment.getDate());
            appointmentResponse.setId(appointment.getId());
        }

        return new ResponseEntity(responses, HttpStatus.OK);
    }



    // admin/assign/doctor - assign appointment to doctor
    @PostMapping("/admin/assign/doctor")
    public ResponseEntity<?> assignAppointmentToDoctor(@RequestBody UpdateAppointmentRequest request) {

        ResponseObject responseObject = new ResponseObject();

        if(request == null ) {
            responseObject.setResponseCode(Constants.ResponseCode.FAIL.value());
            responseObject.setResponseMessage("Failed");
            return new ResponseEntity(responseObject, HttpStatus.BAD_REQUEST);
        }

        if(request.getDoctorId() == 0) {
            responseObject.setResponseCode(Constants.ResponseCode.FAIL.value());
            responseObject.setResponseMessage("Doctor not found");
            return new ResponseEntity(responseObject, HttpStatus.BAD_REQUEST);
        }

        User doctor = userService.getUserById(request.getDoctorId());

        if(doctor == null) {
            responseObject.setResponseCode(Constants.ResponseCode.FAIL.value());
            responseObject.setResponseMessage("Doctor not found");
            return new ResponseEntity(responseObject, HttpStatus.BAD_REQUEST);
        }

        if (request.getAppointmentId() == 0) {
            responseObject.setResponseCode(Constants.ResponseCode.FAIL.value());
            responseObject.setResponseMessage("Appointment not found");
            return new ResponseEntity(responseObject, HttpStatus.BAD_REQUEST);
        }

        Appointment appointment = appointmentService.getAppointmentById(request.getAppointmentId());

        if (appointment == null) {
            throw new AppointmentNotFoundException();
        }

        if(appointment.getStatus().equals(Constants.AppointmentStatus.CANCEL.value())) {
            responseObject.setResponseCode(Constants.ResponseCode.FAIL.value());
            responseObject.setResponseMessage("Appointment is canceled");
            return new ResponseEntity(responseObject, HttpStatus.BAD_REQUEST);
        }

        appointment.setDoctorId(request.getDoctorId());
        appointment.setStatus(Constants.AppointmentStatus.ASSIGNED_TO_DOCTOR.value());
        Appointment updatedAppointment =appointmentService.addAppointment(appointment);

        if(updatedAppointment != null) {
            responseObject.setResponseCode(Constants.ResponseCode.SUCCESS.value());
            responseObject.setResponseMessage("Appointment is assigned to doctor");
            return new ResponseEntity(responseObject, HttpStatus.OK);
        }
        else {
            responseObject.setResponseCode(Constants.ResponseCode.FAIL.value());
            responseObject.setResponseMessage("Failed");
            return new ResponseEntity(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // update - update status of appointment
    @PostMapping("/update")
    public ResponseEntity<?> updateAppointmentStatus(@RequestBody UpdateAppointmentRequest request) {

        ResponseObject responseObject = new ResponseObject();

        if(request == null) {
            responseObject.setResponseCode(Constants.ResponseCode.FAIL.value());
            responseObject.setResponseMessage("Failed");
            return new ResponseEntity(responseObject, HttpStatus.BAD_REQUEST);
        }

        if(request.getAppointmentId() == 0) {
            responseObject.setResponseCode(Constants.ResponseCode.FAIL.value());
            responseObject.setResponseMessage("Failed");
            return new ResponseEntity(responseObject, HttpStatus.BAD_REQUEST);
        }

        Appointment appointment = appointmentService.getAppointmentById(request.getAppointmentId());

        if(appointment == null) {
            throw new AppointmentNotFoundException();
        }

        appointment.setStatus(request.getStatus());
        Appointment updatedAppointment = appointmentService.addAppointment(appointment);

        if(updatedAppointment != null) {
            responseObject.setResponseCode(Constants.ResponseCode.SUCCESS.value());
            responseObject.setResponseMessage("Successfully");
            return new ResponseEntity(responseObject, HttpStatus.OK);
        }
        else {
            responseObject.setResponseCode(Constants.ResponseCode.FAIL.value());
            responseObject.setResponseMessage("Failed");
            return new ResponseEntity(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
