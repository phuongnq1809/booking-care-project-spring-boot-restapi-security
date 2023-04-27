package phuongnq.asm3.service;

import phuongnq.asm3.entity.MedicalRecord;

import java.util.List;

public interface MedicalRecordService {
    MedicalRecord save(MedicalRecord theMedicalRecord);

    List<MedicalRecord> findAllByDoctor(int doctorId);

    MedicalRecord findAllByPatient(int patientId);
}
