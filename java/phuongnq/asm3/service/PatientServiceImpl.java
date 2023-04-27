package phuongnq.asm3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phuongnq.asm3.dao.PatientRepository;
import phuongnq.asm3.entity.Patient;
import phuongnq.asm3.exception.EntityNotFoundException;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository thePatientRepository) {
        patientRepository = thePatientRepository;
    }

    @Override
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient findByUserId(int userId) {
        return patientRepository.findByUserId(userId);
    }

    @Override
    public Patient findById(int patientId) {
        Optional<Patient> result = patientRepository.findById(patientId);

        Patient thePatient = null;

        if (result.isPresent()) {
            thePatient = result.get();
        } else {
            // we didn't find the patient
            throw new EntityNotFoundException("Did not find patient id - " + patientId);
        }

        return thePatient;
    }
}
