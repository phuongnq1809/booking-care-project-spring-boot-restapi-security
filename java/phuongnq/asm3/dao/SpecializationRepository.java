package phuongnq.asm3.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import phuongnq.asm3.entity.Specialization;

import java.util.List;

public interface SpecializationRepository extends JpaRepository<Specialization, Integer> {

    @Query(value = "select * from specializations order by number_booked desc limit 3", nativeQuery = true)
    List<Specialization> getFeaturedSpecializations();
}
