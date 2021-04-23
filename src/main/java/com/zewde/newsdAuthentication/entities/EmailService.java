package com.zewde.newsdAuthentication.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final static Logger logger = LoggerFactory.getLogger(EmailService.class);

  @Autowired
  private JavaMailSender mailSender;


//  @Autowired
//  private SimpleMailMessage simpleMailMessage;

  private final static String SENDER = "noreply@newsdme.com";

  public void sendEmail(String to, String subject, String text){
    SimpleMailMessage message = new SimpleMailMessage();

    message.setFrom(SENDER);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);

    System.out.println("************************");
    logger.info("Mail created");
   try {
     mailSender.send(message);
     logger.info("Email send to: {}",to);
     logger.info("try to get text");
     System.out.println(message.getText());

   }catch(MailException ex){
     logger.warn("Not able to send Email");
   }

  }


//  public void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
//    MimeMessage message = mailSender.createMimeMessage();
//
//    MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
//
//    helper.setFrom(SENDER);
//    helper.setTo(to);
//    helper.setSubject(subject);
//    helper.setText(htmlBody, true);
//
//    mailSender.send(message);
//
//  }
}
