package phuongnq.asm3.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import phuongnq.asm3.entity.*;
import phuongnq.asm3.exception.EntityNotFoundException;
import phuongnq.asm3.service.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users-api")
public class UserRestController {

    private UserService userService;
    private RoleService roleService;
    private PatientService patientService;
    private BookingService bookingService;
    private DoctorService doctorService;
    private ClinicService clinicService;
    private SpecializationService specializationService;
    private MedicalRecordService medicalRecordService;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserRestController(UserService theUserService,
                              RoleService theRoleService,
                              PatientService thePatientService,
                              BookingService theBookingService,
                              DoctorService theDoctorService,
                              ClinicService theClinicService,
                              SpecializationService theSpecializationService,
                              MedicalRecordService theMedicalRecordService
    ) {
        userService = theUserService;
        roleService = theRoleService;
        patientService = thePatientService;
        bookingService = theBookingService;
        doctorService = theDoctorService;
        clinicService = theClinicService;
        specializationService = theSpecializationService;
        medicalRecordService = theMedicalRecordService;
        passwordEncoder = new BCryptPasswordEncoder();
    }

    // User (Patient) dang ky tai khoan
    @PostMapping(value = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Patient addUser(@RequestBody User theUser) {

        // Kiem tra xem user da ton tai trong he thong hay chua
        // Neu co tra ve thong bao loi
        User userFindByEmail = userService.findByEmail(theUser.getEmail());
        if (userFindByEmail != null) {
            throw new RuntimeException("Email: " + theUser.getEmail() + " da ton tai trong he thong, vui long chon email khac!");
        }

        // get Role entity from id
        Role role = roleService.findById(theUser.getRole().getId());

        String currentDateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime());

        theUser.setId(0); // add new User
        theUser.setUsername(theUser.getEmail());

        String plainPass = theUser.getPassword();
        String encodedPass = passwordEncoder.encode(plainPass);

        theUser.setPassword(encodedPass);
        theUser.setRole(role);
        theUser.setEnabled(1);
        theUser.setCreatedAt(currentDateTime);

        User dbUser = userService.save(theUser);

        // set role authority for user
        roleService.setUserAuthorities(theUser.getUsername(), theUser.getRole().getRoleName());

        // add new Patient tuong ung voi thong tin user dang ky
        Patient patient = new Patient();
        patient.setId(0);
        patient.setUser(dbUser);
        patient.setMedicalHistory("");
        patient.setEnabled(1);
        patient.setCreatedAt(currentDateTime);

        return patientService.save(patient);
    }

    // Hien thi thong tin cua cac chuyen khoa noi bat
    @GetMapping("/featured-specializations")
    public List<Specialization> getFeaturedSpecializations() {
        return specializationService.getFeaturedSpecializations();
    }

    // Hien thi thong tin cua cac co so y te noi bat
    @GetMapping("/featured-clinics")
    public List<Clinic> getFeaturedClinics() {
        return clinicService.getFeaturedClinics();
    }

    // Hien thi thong tin ca nhan cua user va lich su kham benh
    @GetMapping("/users/{userId}")
    public Map<String, Object> getUser(@PathVariable int userId) {

        // get thong tin User
        User user = userService.findById(userId);

        // get Patient tuong ung voi User
        Patient patient = patientService.findByUserId(user.getId());

        // get Medical Record tuong ung voi Patient
        MedicalRecord medicalRecord = medicalRecordService.findAllByPatient(patient.getId());

        Map<String, Object> results = new HashMap<>();
        results.put("userInfo", user);
        results.put("medicalRecord", medicalRecord);

        return results;
    }

    // Cap nhat thong tin ca nhan user
    @PutMapping(value = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public User updateUser(@RequestBody User theUser) {

        // lay ve thong tin User muon cap nhat
        User userToUpdate = userService.findById(theUser.getId());

        // cap nhat cac truong thong tin (ko xoa thong tin cac field cu)
        userToUpdate.setEmail(theUser.getEmail());
        userToUpdate.setUsername(theUser.getEmail());

        String plainPass = theUser.getPassword();
        String encodedPass = passwordEncoder.encode(plainPass);
        userToUpdate.setPassword(encodedPass);

        userToUpdate.setFullName(theUser.getFullName());
        userToUpdate.setGender(theUser.getGender());
        userToUpdate.setPhoneNumber(theUser.getPhoneNumber());
        userToUpdate.setAddress(theUser.getAddress());
        userToUpdate.setDescription(theUser.getDescription());
        userToUpdate.setAvatar(theUser.getAvatar());

        String currentDateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime());
        userToUpdate.setUpdatedAt(currentDateTime);

        User dbUser = userService.save(userToUpdate);
        return dbUser;

    }

    // Tim kiem chung
    @GetMapping("/search-clinics")
    public List<Clinic> searchClinics(@RequestParam("address") String address,
                                      @RequestParam("price") String price,
                                      @RequestParam("name") String name
    ) {

        List<Clinic> results = clinicService.searchClinics(address, price, name);
        if (!results.isEmpty()) {
            return results;
        } else {
            throw new EntityNotFoundException("Khong tim thay ket qua, vui long thu lai!");
        }
    }

    // Tim kiem theo chuyen khoa cua bac si
    @GetMapping("/search-specialization")
    public List<Doctor> searchSpecialization(@RequestParam("name") String name) {

        List<Doctor> results = doctorService.searchSpecialization(name);

        if (!results.isEmpty()) {
            return results;
        } else {
            throw new EntityNotFoundException("Khong tim thay ket qua, vui long thu lai!");
        }
    }

    // Dat lich kham
    @PostMapping(value = "/bookings", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Booking addBooking(@RequestBody Booking theBooking) {

        // get thong tin cua Doctor va Patient dat lich kham
        Doctor doctor = doctorService.findById(theBooking.getDoctor().getId());
        Patient patient = patientService.findById(theBooking.getPatient().getId());

        // cong them so lan lua chon phong kham, chuyen khoa trong database
        Clinic clinic = doctor.getClinic();
        Specialization specialization = doctor.getSpecialization();

        clinic.setNumberBooked(clinic.getNumberBooked() + 1);
        specialization.setNumberBooked(specialization.getNumberBooked() + 1);

        clinicService.save(clinic);
        specializationService.save(specialization);

        // add a new Booking
        theBooking.setId(0);
        theBooking.setDoctor(doctor);
        theBooking.setPatient(patient);
        theBooking.setStatus(0); // trang thai: cho bac si xac nhan lich
        String currentDateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime());
        theBooking.setCreatedAt(currentDateTime);

        return bookingService.save(theBooking);
    }

}
