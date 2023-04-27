package phuongnq.asm3.service;

import phuongnq.asm3.entity.Specialization;

import java.util.List;

public interface SpecializationService {
    Specialization findById(int specializationId);

    void save(Specialization specialization);

    List<Specialization> getFeaturedSpecializations();
}
