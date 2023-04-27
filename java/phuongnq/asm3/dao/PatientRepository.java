package phuongnq.asm3.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import phuongnq.asm3.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    @Query("select p from Patient p where p.user.id = ?1")
    Patient findByUserId(int userId);
}
