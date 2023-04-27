package phuongnq.asm3.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import phuongnq.asm3.entity.*;
import phuongnq.asm3.service.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/admin-api")
public class AdminRestController {

    private UserService userService;
    private RoleService roleService;
    private DoctorService doctorService;
    private ClinicService clinicService;
    private SpecializationService specializationService;
    private PatientService patientService;
    private BookingService bookingService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminRestController(UserService theUserService,
                               RoleService theRoleService,
                               DoctorService theDoctorService,
                               ClinicService theClinicService,
                               SpecializationService theSpecializationService,
                               PatientService thePatientService,
                               BookingService theBookingService) {
        userService = theUserService;
        roleService = theRoleService;
        doctorService = theDoctorService;
        clinicService = theClinicService;
        specializationService = theSpecializationService;
        patientService = thePatientService;
        bookingService = theBookingService;
        passwordEncoder = new BCryptPasswordEncoder();
    }

    // them tai khoan bac si
    @PostMapping(value = "/doctors", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Doctor addDoctor(@RequestBody DoctorCreationRequest doctorCreationRequest) {

        // get User and Doctor infos from request's body
        User theUser = doctorCreationRequest.getUser();
        Doctor theDoctor = doctorCreationRequest.getDoctor();

        // Kiem tra xem user da ton tai trong he thong hay chua
        // Neu co tra ve thong bao loi
        User userFindByEmail = userService.findByEmail(theUser.getEmail());
        if (userFindByEmail != null) {
            throw new RuntimeException("Email: " + theUser.getEmail() + " da ton tai trong he thong, vui long chon email khac!");
        }

        // add new User
        theUser.setId(0);
        theUser.setUsername(theUser.getEmail());

        Role role = roleService.findById(theUser.getRole().getId());
        theUser.setRole(role);

        String plainPass = theUser.getPassword();
        String encodedPass = passwordEncoder.encode(plainPass);
        theUser.setPassword(encodedPass);

        theUser.setEnabled(1);
        String currentDateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime());
        theUser.setCreatedAt(currentDateTime);
        User dbUser = userService.save(theUser);

        // set role authority for user
        roleService.setUserAuthorities(theUser.getUsername(), theUser.getRole().getRoleName());

        // add new Doctor tuong ung
        theDoctor.setId(0);
        theDoctor.setUser(dbUser);
        theDoctor.setEnabled(1);
        theDoctor.setCreatedAt(currentDateTime);

        // get Clinic & Specialization from database
        Clinic clinic = clinicService.findById(theDoctor.getClinic().getId());
        Specialization specialization = specializationService.findById(theDoctor.getSpecialization().getId());

        theDoctor.setClinic(clinic);
        theDoctor.setSpecialization(specialization);

        Doctor dbDoctor = doctorService.save(theDoctor);

        return dbDoctor;
    }

    // khoa/huy khoa tai khoan cua benh nhan (user)
    @PutMapping("/lock-unlock-user")
    public Patient lockOrUnlockUser(@RequestBody User theUser) {

        // get User from database
        User userToLockOrUnlock = userService.findById(theUser.getId());

        // get Patient tuong ung voi user
        Patient patientToLockOrUnlock = patientService.findByUserId(userToLockOrUnlock.getId());

        String currentDateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime());

        // neu mo khoa tai khoan: xoa bo li do khoa truoc do (neu co)
        if (theUser.getEnabled() == 1) {
            userToLockOrUnlock.setDescription("");
            userToLockOrUnlock.setDeletedAt("");

            patientToLockOrUnlock.setDescription("");
            patientToLockOrUnlock.setDeletedAt("");

            // nguoc lai: them li do khoa tai khoan
        } else {
            userToLockOrUnlock.setDescription(theUser.getDescription());
            userToLockOrUnlock.setDeletedAt(currentDateTime);

            patientToLockOrUnlock.setDescription(theUser.getDescription());
            patientToLockOrUnlock.setDeletedAt(currentDateTime);
        }

        userToLockOrUnlock.setEnabled(theUser.getEnabled());
        patientToLockOrUnlock.setEnabled(theUser.getEnabled());

        patientToLockOrUnlock.setUser(userToLockOrUnlock);
        userService.save(userToLockOrUnlock);

        return patientService.save(patientToLockOrUnlock);
    }

    // khoa/huy khoa tai khoan cua bac si
    @PutMapping("/lock-unlock-doctor")
    public Doctor lockOrUnlockDoctor(@RequestBody User theUser) {

        // get User from database
        User userToLockOrUnlock = userService.findById(theUser.getId());

        // get Doctor tuong ung
        Doctor doctorToLockOrUnlock = doctorService.findByUserId(userToLockOrUnlock.getId());

        String currentDateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime());

        // neu mo khoa tai khoan: xoa bo li do khoa truoc do (neu co)
        if (theUser.getEnabled() == 1) {
            userToLockOrUnlock.setDescription("");
            userToLockOrUnlock.setDeletedAt("");

            doctorToLockOrUnlock.setDescription("");
            doctorToLockOrUnlock.setDeletedAt("");

            // nguoc lai: them li do khoa tai khoan
        } else {
            userToLockOrUnlock.setDescription(theUser.getDescription());
            userToLockOrUnlock.setDeletedAt(currentDateTime);

            doctorToLockOrUnlock.setDescription(theUser.getDescription());
            doctorToLockOrUnlock.setDeletedAt(currentDateTime);
        }

        userToLockOrUnlock.setEnabled(theUser.getEnabled());
        doctorToLockOrUnlock.setEnabled(theUser.getEnabled());

        doctorToLockOrUnlock.setUser(userToLockOrUnlock);
        userService.save(userToLockOrUnlock);

        return doctorService.save(doctorToLockOrUnlock);
    }

    // xem thong tin chi tiet lich kham cua tung benh nhan
    @GetMapping("/bookings-patient/{patientId}")
    public List<Booking> getBookingsByPatient(@PathVariable int patientId) {

        Patient thePatient = patientService.findById(patientId);

        return bookingService.getBookingsByPatient(thePatient.getId());

    }

    // xem thong tin chi tiet lich kham cua tung bac si
    @GetMapping("/bookings-doctor/{doctorId}")
    public List<Booking> getBookingsByDoctor(@PathVariable int doctorId) {

        Doctor theDoctor = doctorService.findById(doctorId);

        return bookingService.getBookingsByDoctor(theDoctor.getId());
    }

}
