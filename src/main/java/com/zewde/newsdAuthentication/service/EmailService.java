package com.zewde.newsdAuthentication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

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
     logger.info("Email send at : {}", new Date());
     logger.info("Email send to: {}", to);
   }catch(MailException ex){
     logger.warn("Not able to send Email");
   }

  }
}
