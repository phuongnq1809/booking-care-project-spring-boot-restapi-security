package phuongnq.asm3.service;

import phuongnq.asm3.entity.Patient;

public interface PatientService {
    Patient save(Patient patient);

    Patient findByUserId(int userId);

    Patient findById(int patientId);
}
