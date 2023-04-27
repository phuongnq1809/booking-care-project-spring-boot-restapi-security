package phuongnq.asm3.service;

public interface EmailService {
    String sendMailWithAttachment(String recipient,
                                  String msgBody,
                                  String subject,
                                  String attachment);
}
