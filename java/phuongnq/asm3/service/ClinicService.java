package phuongnq.asm3.service;

import phuongnq.asm3.entity.Clinic;

import java.util.List;

public interface ClinicService {
    Clinic findById(int clinicId);

    void save(Clinic clinic);

    List<Clinic> getFeaturedClinics();

    List<Clinic> searchClinics(String address, String price, String name);
}
