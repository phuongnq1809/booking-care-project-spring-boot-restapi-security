package phuongnq.asm3.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import phuongnq.asm3.entity.MedicalRecord;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {

    @Query("select mr from MedicalRecord mr where mr.doctor.id = ?1")
    List<MedicalRecord> findAllByDoctor(int doctorId);

    @Query("select mr from MedicalRecord mr where mr.patient.id = ?1")
    MedicalRecord findAllByPatient(int patientId);
}
