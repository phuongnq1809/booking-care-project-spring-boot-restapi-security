/*
* Booking Repository
*/
package phuongnq.asm3.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import phuongnq.asm3.entity.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("select bk from Booking bk where bk.patient.id = ?1")
    List<Booking> getBookingsByPatient(int patientId);

    @Query("select bk from Booking bk where bk.doctor.id = ?1")
    List<Booking> getBookingsByDoctor(int doctorId);
}
