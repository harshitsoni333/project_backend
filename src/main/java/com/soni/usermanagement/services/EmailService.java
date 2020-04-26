package com.soni.usermanagement.services;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

/**
 * EmailService
 */
@Service("emailService")
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SimpleMailMessage preConfiguredMessage;

    public void sendMail(String recipient, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendMailWithAttachment(String recipient, String subject, String body, String fileToAttach) 
    {
        MimeMessagePreparator preparator = new MimeMessagePreparator() 
        {
            public void prepare(MimeMessage mimeMessage) throws Exception 
            {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                mimeMessage.setFrom(new InternetAddress("blueknight30303@gmail.com"));
                mimeMessage.setSubject(subject);
                mimeMessage.setText(body);
                
                FileSystemResource file = new FileSystemResource(fileToAttach);
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.addAttachment("attachment.xlsx", file);
                helper.setText(body);
            }
        };
        
        try {
            mailSender.send(preparator);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }

    public void sendPreConfiguredMail(String message) {

        SimpleMailMessage mailMessage = new SimpleMailMessage(preConfiguredMessage);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}