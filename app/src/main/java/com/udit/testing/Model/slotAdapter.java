package com.udit.testing.Model;

public class slotAdapter {
    String _name;
    String _mobileNo;
    String _gender;
    String rating;
    String doc_type;
    String slot;




    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    String _address;
    String _dob;
    String _day;
    String token;
    String _appointmentDate;
    String _ptPic;
    String _aadharNo;
    String _doctorMobileNo;
    String profile;

    public slotAdapter(String _name, String _mobileNo, String _gender, String rating, String doc_type, String _address, String _dob, String _day, String token, String _appointmentDate, String _ptPic, String _aadharNo, String _doctorMobileNo, String profile) {
        this._name = _name;
        this._mobileNo = _mobileNo;
        this._gender = _gender;
        this.rating = rating;
        this.doc_type = doc_type;
        this._address = _address;
        this._dob = _dob;
        this._day = _day;
        this.token = token;
        this._appointmentDate = _appointmentDate;
        this._ptPic = _ptPic;
        this._aadharNo = _aadharNo;
        this._doctorMobileNo = _doctorMobileNo;
        this.profile = profile;
    }

    public slotAdapter() {
    }


    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_mobileNo() {
        return _mobileNo;
    }

    public void set_mobileNo(String _mobileNo) {
        this._mobileNo = _mobileNo;
    }

    public String get_gender() {
        return _gender;
    }

    public void set_gender(String _gender) {
        this._gender = _gender;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public String get_dob() {
        return _dob;
    }

    public void set_dob(String _dob) {
        this._dob = _dob;
    }

    public String get_day() {
        return _day;
    }

    public void set_day(String _day) {
        this._day = _day;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String get_appointmentDate() {
        return _appointmentDate;
    }

    public void set_appointmentDate(String _appointmentDate) {
        this._appointmentDate = _appointmentDate;
    }

    public String get_ptPic() {
        return _ptPic;
    }

    public void set_ptPic(String _ptPic) {
        this._ptPic = _ptPic;
    }

    public String get_aadharNo() {
        return _aadharNo;
    }

    public void set_aadharNo(String _aadharNo) {
        this._aadharNo = _aadharNo;
    }

    public String get_doctorMobileNo() {
        return _doctorMobileNo;
    }

    public void set_doctorMobileNo(String _doctorMobileNo) {
        this._doctorMobileNo = _doctorMobileNo;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
