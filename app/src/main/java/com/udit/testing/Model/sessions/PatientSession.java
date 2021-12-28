package com.udit.testing.Model.sessions;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class PatientSession {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    public static final String PATIENT_LOGIN_SESSION = "patientLoginSession";
    public static final String PATIENT_REMEMBER_ME_SESSION = "patientRememberMeSession";

    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_REMEMBER_ME = "IsLoggedIn";

    public static final String KEY_NAME = "name";
    public static final String KEY_CITY = "city";
    public static final String KEY_DOB = "dob";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE_NUMBER = "phoneNo";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_LOGIN_STATUS = "loginStatus";
    public static final String KEY_TOKEN="token";

    public static final String KEY_PATIENT_SESSION_PHONE_NUMBER = "phoneNo";
    public static final String KEY_PATIENT_SESSION_PASSWORD = "password";

    public PatientSession(Context _context, String sessionName) {
        context = _context;
        userSession = _context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
        editor = userSession.edit();
        editor.apply();
    }

    public void createLoginSession(String name, String phoneNo, String city,
                                   String password, String gender, String dob, String email, String loginStatus,String token) {
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PHONE_NUMBER, phoneNo);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_DOB, dob);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_LOGIN_STATUS, loginStatus);
        editor.putString(KEY_TOKEN,token);

        editor.commit();
    }


    public void createRememberMESession(String phoneNo,String password) {
        editor.putBoolean(IS_REMEMBER_ME, true);

        editor.putString(KEY_PATIENT_SESSION_PHONE_NUMBER, phoneNo);
        editor.putString(KEY_PATIENT_SESSION_PASSWORD, password);

        editor.commit();
    }

    public HashMap<String, String> getUserDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_NAME, userSession.getString(KEY_NAME, null));
        userData.put(KEY_PHONE_NUMBER, userSession.getString(KEY_PHONE_NUMBER, null));
        userData.put(KEY_PASSWORD, userSession.getString(KEY_PASSWORD, null));
        userData.put(KEY_EMAIL, userSession.getString(KEY_EMAIL, null));
        userData.put(KEY_DOB, userSession.getString(KEY_DOB, null));
        userData.put(KEY_GENDER, userSession.getString(KEY_GENDER, null));
        userData.put(KEY_CITY, userSession.getString(KEY_CITY, null));
        userData.put(KEY_LOGIN_STATUS, userSession.getString(KEY_LOGIN_STATUS, null));
        userData.put(KEY_TOKEN,userSession.getString(KEY_TOKEN,null));

        return userData;
    }

    public HashMap<String, String> getRememberMeDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();


        userData.put(KEY_PHONE_NUMBER, userSession.getString(KEY_PATIENT_SESSION_PHONE_NUMBER, null));
        userData.put(KEY_PASSWORD, userSession.getString(KEY_PATIENT_SESSION_PASSWORD, null));

        return userData;
    }

    public boolean checkLogin(){
        return userSession.getBoolean(IS_LOGIN, false);
    }

    public boolean checkRememberMe(){
        return userSession.getBoolean(IS_LOGIN, false);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }

}















