package phuongnq.asm3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phuongnq.asm3.dao.ClinicRepository;
import phuongnq.asm3.entity.Clinic;
import phuongnq.asm3.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicServiceImpl implements ClinicService {

    private ClinicRepository clinicRepository;

    @Autowired
    public ClinicServiceImpl(ClinicRepository theClinicRepository) {
        clinicRepository = theClinicRepository;
    }

    @Override
    public Clinic findById(int clinicId) {
        Optional<Clinic> result = clinicRepository.findById(clinicId);

        Clinic theClinic = null;

        if (result.isPresent()) {
            theClinic = result.get();
        } else {
            // we didn't find the clinic
            throw new EntityNotFoundException("Did not find clinic id - " + clinicId);
        }

        return theClinic;
    }

    @Override
    public void save(Clinic clinic) {
        clinicRepository.save(clinic);
    }

    @Override
    public List<Clinic> getFeaturedClinics() {
        return clinicRepository.getFeaturedClinics();
    }

    @Override
    public List<Clinic> searchClinics(String address, String price, String name) {
        return clinicRepository.searchClinics(address, price, name);
    }
}
