package phuongnq.asm3.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import phuongnq.asm3.entity.EmailMedicalRecord;

public interface EmailMedicalRecordRepository extends JpaRepository<EmailMedicalRecord, Integer> {
}
