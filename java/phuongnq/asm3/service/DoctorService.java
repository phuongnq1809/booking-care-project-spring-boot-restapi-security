package phuongnq.asm3.service;

import phuongnq.asm3.entity.Doctor;

import java.util.List;

public interface DoctorService {
    Doctor save(Doctor theDoctor);

    Doctor findById(int doctorId);

    Doctor findByUserId(int userId);

    List<Doctor> searchSpecialization(String name);
}
