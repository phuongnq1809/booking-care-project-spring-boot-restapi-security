package phuongnq.asm3.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import phuongnq.asm3.entity.Doctor;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    @Query("select d from Doctor d where d.user.id = ?1")
    Doctor findByUserId(int userId);

    @Query("select dt from Doctor dt where " +
            "dt.specialization.specializationName like concat('%', :name, '%')"
    )
    List<Doctor> searchSpecialization(String name);
}
