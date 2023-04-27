package phuongnq.asm3.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;
import phuongnq.asm3.entity.*;
import phuongnq.asm3.service.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/doctor-api")
public class DoctorRestController {

    private BookingService bookingService;
    private MedicalRecordService medicalRecordService;
    private DoctorService doctorService;
    private PatientService patientService;
    private EmailService emailService;
    private EmailMedicalRecordService emailMedicalRecordService;

    @Autowired
    public DoctorRestController(BookingService theBookingService,
                                MedicalRecordService theMedicalRecordService,
                                DoctorService theDoctorService,
                                PatientService thePatientService,
                                EmailService theEmailService,
                                EmailMedicalRecordService theEmailMedicalRecordService) {
        bookingService = theBookingService;
        medicalRecordService = theMedicalRecordService;
        doctorService = theDoctorService;
        patientService = thePatientService;
        emailService = theEmailService;
        emailMedicalRecordService = theEmailMedicalRecordService;
    }
    
    // nhan/huy lich kham cua benh nhan
    @PutMapping("/accept-cancel-booking")
    public Booking acceptOrCancelBooking(@RequestBody Booking theBooking) {

        // get Booking from database
        Booking bookingToAcceptOrCancel = bookingService.findById(theBooking.getId());

        String currentDateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime());

        // neu bac si dong y nhan lich kham: xoa bo li do huy truoc do (neu co)
        if (theBooking.getStatus() == 1) {
            bookingToAcceptOrCancel.setDescription("");
            bookingToAcceptOrCancel.setUpdatedAt(currentDateTime);

            // nguoc lai: them li do huy nhan lich
        } else {
            bookingToAcceptOrCancel.setDescription(theBooking.getDescription());
            bookingToAcceptOrCancel.setUpdatedAt(currentDateTime);
        }

        bookingToAcceptOrCancel.setStatus(theBooking.getStatus());

        return bookingService.save(bookingToAcceptOrCancel);
    }

    // bac si kham benh va cap nhat ket qua kham
    @PostMapping("/medical-record")
    public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord theMedicalRecord) {

        // get Doctor & Patient
        Doctor doctor = doctorService.findById(theMedicalRecord.getDoctor().getId());
        Patient patient = patientService.findById(theMedicalRecord.getPatient().getId());

        // add a new MedicalRecord
        theMedicalRecord.setId(0);
        theMedicalRecord.setDoctor(doctor);
        theMedicalRecord.setPatient(patient);
        String currentDateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime());
        theMedicalRecord.setCreatedAt(currentDateTime);

        return medicalRecordService.save(theMedicalRecord);
    }

    // hien thi danh sach benh nhan
    @GetMapping("/medical-records/{doctorId}")
    public List<MedicalRecord> getMedicalRecords(@PathVariable int doctorId) {

        // get Doctor
        Doctor doctor = doctorService.findById(doctorId);

        return medicalRecordService.findAllByDoctor(doctor.getId());

    }

    // bac si gui thong tin kham chua benh ve email cua benh nhan (dinh kem file)
    @PostMapping("/send-email-medical-record")
    public EmailMedicalRecord sendEmailMedicalRecord(@RequestBody EmailMedicalRecord theEmailMedicalRecord) {

        // send email
        String status = emailService.sendMailWithAttachment(theEmailMedicalRecord.getRecipient(),
                theEmailMedicalRecord.getMsgBody(),
                theEmailMedicalRecord.getSubject(),
                theEmailMedicalRecord.getAttachment());

        if (status.equals("Mail sent Successfully!")) {

            // get Doctor & Patient in database
            Doctor doctor = doctorService.findById(theEmailMedicalRecord.getDoctor().getId());
            Patient patient = patientService.findById(theEmailMedicalRecord.getPatient().getId());

            theEmailMedicalRecord.setId(0);
            theEmailMedicalRecord.setDoctor(doctor);
            theEmailMedicalRecord.setPatient(patient);

            FileSystemResource file
                    = new FileSystemResource(
                    new File(theEmailMedicalRecord.getAttachment()));
            theEmailMedicalRecord.setAttachment(file.getFilename());

            theEmailMedicalRecord.setStatus("success");
            String currentDateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime());
            theEmailMedicalRecord.setCreatedAt(currentDateTime);

            return emailMedicalRecordService.save(theEmailMedicalRecord);

        } else {
            throw new RuntimeException("Xay ra loi trong qua trinh gui email, vui long thu lai sau. Many Thanks!!!");
        }

    }

}
