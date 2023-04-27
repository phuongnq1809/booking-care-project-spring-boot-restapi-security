package phuongnq.asm3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phuongnq.asm3.dao.MedicalRecordRepository;
import phuongnq.asm3.entity.MedicalRecord;

import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public MedicalRecordServiceImpl(MedicalRecordRepository theMedicalRecordRepository) {
        medicalRecordRepository = theMedicalRecordRepository;
    }

    @Override
    public MedicalRecord save(MedicalRecord theMedicalRecord) {
        return medicalRecordRepository.save(theMedicalRecord);
    }

    @Override
    public List<MedicalRecord> findAllByDoctor(int doctorId) {
        return medicalRecordRepository.findAllByDoctor(doctorId);
    }

    @Override
    public MedicalRecord findAllByPatient(int patientId) {
        return medicalRecordRepository.findAllByPatient(patientId);
    }
}
