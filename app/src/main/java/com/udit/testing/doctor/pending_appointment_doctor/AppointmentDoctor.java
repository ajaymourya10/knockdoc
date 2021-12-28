package com.udit.testing.doctor.pending_appointment_doctor;

public class AppointmentDoctor {

    String appointment_date;
    String appointment_day;
    String count;
    String patient_aadhar;
    String patient_dob;
    String patient_gender;
    String patient_image;
    String patient_mobile;
    String patient_name;
    String status;
    String time_slot;
    String token;
    String doc_mob;

    public String getDoc_mob() {
        return doc_mob;
    }

    public void setDoc_mob(String doc_mob) {
        this.doc_mob = doc_mob;
    }

    public AppointmentDoctor(){}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AppointmentDoctor(String appointment_date, String appointment_day, String count,
                             String patient_aadhar, String patient_dob, String patient_gender,
                             String patient_image, String patient_mobile, String patient_name,
                             String status, String time_slot, String token) {
        this.appointment_date = appointment_date;
        this.appointment_day = appointment_day;
        this.count = count;
        this.patient_aadhar = patient_aadhar;
        this.patient_dob = patient_dob;
        this.patient_gender = patient_gender;
        this.patient_image = patient_image;
        this.patient_mobile = patient_mobile;
        this.patient_name = patient_name;
        this.status = status;
        this.time_slot = time_slot;
        this.token=token;

    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public String getAppointment_day() {
        return appointment_day;
    }

    public void setAppointment_day(String appointment_day) {
        this.appointment_day = appointment_day;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPatient_aadhar() {
        return patient_aadhar;
    }

    public void setPatient_aadhar(String patient_aadhar) {
        this.patient_aadhar = patient_aadhar;
    }

    public String getPatient_dob() {
        return patient_dob;
    }

    public void setPatient_dob(String patient_dob) {
        this.patient_dob = patient_dob;
    }

    public String getPatient_gender() {
        return patient_gender;
    }

    public void setPatient_gender(String patient_gender) {
        this.patient_gender = patient_gender;
    }

    public String getPatient_image() {
        return patient_image;
    }

    public void setPatient_image(String patient_image) {
        this.patient_image = patient_image;
    }

    public String getPatient_mobile() {
        return patient_mobile;
    }

    public void setPatient_mobile(String patient_mobile) {
        this.patient_mobile = patient_mobile;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }
}
