package phuongnq.asm3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phuongnq.asm3.dao.DoctorRepository;
import phuongnq.asm3.entity.Doctor;
import phuongnq.asm3.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private DoctorRepository doctorRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository theDoctorRepository) {
        this.doctorRepository = theDoctorRepository;
    }

    @Override
    public Doctor save(Doctor theDoctor) {
        return doctorRepository.save(theDoctor);
    }

    @Override
    public Doctor findById(int doctorId) {
        Optional<Doctor> result = doctorRepository.findById(doctorId);

        Doctor theDoctor = null;

        if (result.isPresent()) {
            theDoctor = result.get();
        } else {
            // we didn't find the doctor
            throw new EntityNotFoundException("Did not find doctor id - " + doctorId);
        }

        return theDoctor;
    }

    @Override
    public Doctor findByUserId(int userId) {
        return doctorRepository.findByUserId(userId);
    }

    @Override
    public List<Doctor> searchSpecialization(String name) {
        return doctorRepository.searchSpecialization(name);
    }
}
