package phuongnq.asm3.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import phuongnq.asm3.entity.Clinic;

import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, Integer> {

    @Query(value = "select * from clinics order by number_booked desc limit 3", nativeQuery = true)
    List<Clinic> getFeaturedClinics();

    @Query("select cl from Clinic cl where " +
            "cl.address like concat('%', :address, '%') " +
            "or cl.introduction like concat('%', :price, '%') " +
            "or cl.clinicName like concat('%', :name, '%')"
    )
    List<Clinic> searchClinics(String address, String price, String name);
}
