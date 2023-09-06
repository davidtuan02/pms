package com.example.mentalhealthcarepms.dto;

public class UpdateAppointmentRequest {
    private int appointmentId;
    private int doctorId;
    private double price;
    private String presciption;
    private String status;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPresciption() {
        return presciption;
    }

    public void setPresciption(String presciption) {
        this.presciption = presciption;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
