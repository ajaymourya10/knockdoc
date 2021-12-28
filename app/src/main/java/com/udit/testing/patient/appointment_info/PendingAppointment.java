package com.udit.testing.patient.appointment_info;

public class PendingAppointment {

    private String count;
    private String degree;
    private String doctor_dp;
    private String rating;
    private String appointment_date;
    private String appointment_day;
    private String doctor_fee;
    private String time_slot;
    private String doctor_mobile;
    private String doctor_name;
    private String doctor_type;
    private String patient_dp;
    private String patient_name;
    private String status;
    private String patient_dob;
    private String gender;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPatient_mob() {
        return patient_mob;
    }

    public void setPatient_mob(String patient_mob) {
        this.patient_mob = patient_mob;
    }

    private String patient_mob;

    public PendingAppointment() {
    }

    public PendingAppointment(String count, String degree, String doctor_dp, String doctor_fee, String doctor_mobile, String doctor_name,
                              String doctor_type, String patient_dp, String patient_name, String status) {
        this.count = count;
        this.degree = degree;
        this.doctor_dp = doctor_dp;
        this.doctor_fee = doctor_fee;
        this.doctor_mobile = doctor_mobile;
        this.doctor_name = doctor_name;
        this.doctor_type = doctor_type;
        this.patient_dp = patient_dp;
        this.patient_name = patient_name;
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPatient_dob() {
        return patient_dob;
    }

    public void setPatient_dob(String patient_dob) {
        this.patient_dob = patient_dob;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDoctor_dp() {
        return doctor_dp;
    }

    public void setDoctor_dp(String doctor_dp) {
        this.doctor_dp = doctor_dp;
    }

    public String getDoctor_fee() {
        return doctor_fee;
    }

    public void setDoctor_fee(String doctor_fee) {
        this.doctor_fee = doctor_fee;
    }

    public String getDoctor_mobile() {
        return doctor_mobile;
    }

    public void setDoctor_mobile(String doctor_mobile) {
        this.doctor_mobile = doctor_mobile;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDoctor_type() {
        return doctor_type;
    }

    public void setDoctor_type(String doctor_type) {
        this.doctor_type = doctor_type;
    }

    public String getPatient_dp() {
        return patient_dp;
    }

    public void setPatient_dp(String patient_dp) {
        this.patient_dp = patient_dp;
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
}
