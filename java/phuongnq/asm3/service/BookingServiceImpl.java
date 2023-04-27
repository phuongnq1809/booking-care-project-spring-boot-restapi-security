package phuongnq.asm3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phuongnq.asm3.dao.BookingRepository;
import phuongnq.asm3.entity.Booking;
import phuongnq.asm3.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository theBookingRepository) {
        bookingRepository = theBookingRepository;
    }

    @Override
    public Booking save(Booking theBooking) {
        return bookingRepository.save(theBooking);
    }

    @Override
    public Booking findById(int bookingId) {

        Optional<Booking> result = bookingRepository.findById(bookingId);

        Booking theBooking = null;

        if (result.isPresent()) {
            theBooking = result.get();
        } else {
            // we didn't find the booking
            throw new EntityNotFoundException("Did not find booking id - " + bookingId);
        }

        return theBooking;
    }

    @Override
    public List<Booking> getBookingsByPatient(int patientId) {
        return bookingRepository.getBookingsByPatient(patientId);
    }

    @Override
    public List<Booking> getBookingsByDoctor(int doctorId) {
        return bookingRepository.getBookingsByDoctor(doctorId);
    }
}
