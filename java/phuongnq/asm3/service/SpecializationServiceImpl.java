package phuongnq.asm3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phuongnq.asm3.dao.SpecializationRepository;
import phuongnq.asm3.entity.Specialization;
import phuongnq.asm3.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class SpecializationServiceImpl implements SpecializationService {

    private SpecializationRepository specializationRepository;

    @Autowired
    public SpecializationServiceImpl(SpecializationRepository theSpecializationRepository) {
        specializationRepository = theSpecializationRepository;
    }

    @Override
    public Specialization findById(int specializationId) {
        Optional<Specialization> result = specializationRepository.findById(specializationId);

        Specialization theSpecialization = null;

        if (result.isPresent()) {
            theSpecialization = result.get();
        } else {
            // we didn't find the specialization
            throw new EntityNotFoundException("Did not find specialization id - " + specializationId);
        }

        return theSpecialization;
    }

    @Override
    public void save(Specialization specialization) {
        specializationRepository.save(specialization);
    }

    @Override
    public List<Specialization> getFeaturedSpecializations() {
        return specializationRepository.getFeaturedSpecializations();
    }
}
