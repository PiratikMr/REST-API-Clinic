CREATE TABLE users (
    user_id         BIGSERIAL   PRIMARY KEY,
    username        TEXT        NOT NULL UNIQUE,
    password        TEXT        NOT NULL,
    role            TEXT        NOT NULL
);

CREATE TABLE specialties (
    specialty_id    BIGSERIAL   PRIMARY KEY,
    name            TEXT        NOT NULL UNIQUE
);

CREATE TABLE doctors (
    doctor_id       BIGSERIAL   PRIMARY KEY,
    user_id         BIGINT      UNIQUE  REFERENCES users(user_id),
    last_name       TEXT        NOT NULL,
    first_name      TEXT        NOT NULL,
    middle_name     TEXT,
    specialty_id    INT         REFERENCES specialties(specialty_id)
);

CREATE TABLE places (
    place_id        BIGSERIAL   PRIMARY KEY,
    room            TEXT        NOT NULL,
    district        TEXT        NOT NULL
);

CREATE TABLE doctor_slots (
    slot_id         BIGSERIAL   PRIMARY KEY,
    doctor_id       BIGINT      REFERENCES doctors(doctor_id),
    place_id        BIGINT      REFERENCES places(place_id),
    start_time      TIMESTAMP   NOT NULL,
    end_time        TIMESTAMP   NOT NULL,

    UNIQUE (doctor_id, start_time)
);


CREATE TABLE patients (
    patient_id      BIGSERIAL   PRIMARY KEY,
    user_id         BIGINT      UNIQUE REFERENCES users(user_id),
    last_name       TEXT        NOT NULL,
    first_name      TEXT        NOT NULL,
    middle_name     TEXT,
    address         TEXT        NOT NULL
);

CREATE TABLE appointments (
    appointment_id  BIGSERIAL   PRIMARY KEY,
    patient_id      BIGINT      REFERENCES patients(patient_id),
    slot_id         BIGINT      NOT NULL REFERENCES doctor_slots(slot_id),
    complaints      TEXT,
    diagnosis       TEXT,
    status          TEXT        NOT NULL
);

CREATE UNIQUE INDEX unique_active_slot_idx
    ON appointments (slot_id)
    WHERE status = 'ACTIVE';


CREATE TABLE medications (
    medication_id   BIGSERIAL   PRIMARY KEY,
    name            TEXT        NOT NULL UNIQUE
);

CREATE TABLE procedures (
    procedure_id    BIGSERIAL   PRIMARY KEY,
    name            TEXT        NOT NULL UNIQUE
);

CREATE TABLE tests (
    test_id         BIGSERIAL   PRIMARY KEY,
    name            TEXT        NOT NULL UNIQUE
);

CREATE TABLE prescribed_medications (
    prescribed_med_id   BIGSERIAL   PRIMARY KEY,
    appointment_id      BIGINT      REFERENCES appointments(appointment_id),
    medication_id       BIGINT      REFERENCES medications(medication_id),
    details             TEXT
);

CREATE TABLE prescribed_procedures (
    prescribed_proc_id  BIGSERIAL   PRIMARY KEY,
    appointment_id      INT         REFERENCES appointments(appointment_id),
    procedure_id        INT         REFERENCES procedures(procedure_id),
    session_count       INT         CHECK (session_count > 0)
);

CREATE TABLE prescribed_tests (
    prescribed_test_id  BIGSERIAL   PRIMARY KEY,
    appointment_id      BIGINT      REFERENCES appointments(appointment_id),
    test_id             BIGINT      REFERENCES tests(test_id),
    result              TEXT
);