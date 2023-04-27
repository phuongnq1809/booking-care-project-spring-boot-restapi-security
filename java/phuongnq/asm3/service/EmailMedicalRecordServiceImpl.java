package phuongnq.asm3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phuongnq.asm3.dao.EmailMedicalRecordRepository;
import phuongnq.asm3.entity.EmailMedicalRecord;

@Service
public class EmailMedicalRecordServiceImpl implements EmailMedicalRecordService {

    private EmailMedicalRecordRepository emailMedicalRecordRepository;

    @Autowired
    public EmailMedicalRecordServiceImpl(EmailMedicalRecordRepository theEmailMedicalRecordRepository) {
        emailMedicalRecordRepository = theEmailMedicalRecordRepository;
    }

    @Override
    public EmailMedicalRecord save(EmailMedicalRecord theEmailMedicalRecord) {
        return emailMedicalRecordRepository.save(theEmailMedicalRecord);
    }
}
