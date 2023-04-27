package phuongnq.asm3.service;

import phuongnq.asm3.entity.Booking;

import java.util.List;

public interface BookingService {
    Booking save(Booking theBooking);

    Booking findById(int bookingId);

    List<Booking> getBookingsByPatient(int patientId);

    List<Booking> getBookingsByDoctor(int doctorId);
}
