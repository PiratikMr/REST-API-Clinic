package com.example.clinic.service.core.security;

public final class SecurityConstants {

    public static final String ADMIN = "ADMIN";
    public static final String DOCTOR = "DOCTOR";
    public static final String PATIENT = "PATIENT";


    public static final String HAS_ADMIN = "hasRole('" + ADMIN + "')";
    public static final String HAS_PATIENT = "hasRole('" + PATIENT + "')";
    public static final String HAS_DOCTOR = "hasRole('" + DOCTOR + "')";


    private SecurityConstants() {}
}
