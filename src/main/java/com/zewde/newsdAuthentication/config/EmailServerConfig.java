package com.zewde.newsdAuthentication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailServerConfig {

  @Value("${spring.mail.host}")
  private String mailServerHost;

  @Value("${spring.mail.port}")
  private int mailServerPort;

  @Value("${spring.mail.username}")
  private String mailServerUsername;

  @Value("${spring.mail.password}")
  private String mailServerPassword;

  @Value("${frontend.url}")
  private String frontendUrl;

  @Bean
  public JavaMailSender getMailSender() {

    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setHost(mailServerHost);
    mailSender.setPort(mailServerPort);

    mailSender.setUsername(mailServerUsername);
    mailSender.setPassword(mailServerPassword);

    Properties mailProperties = mailSender.getJavaMailProperties();
    mailProperties.put("mail.transport.protocol", "smtp");
    mailProperties.put("mail.smtp.auth", "true");
    mailProperties.put("mail.smtp.starttls.enable", "true");
    mailProperties.put("mail.debug", "true");

    return mailSender;
  }

  @Bean
  public SimpleMailMessage mailTemplate(){
    SimpleMailMessage message = new SimpleMailMessage();

    message.setText("Hi from Newsdme \n please click the link below to confirm your registration.\n\n\n\n" +
        frontendUrl+"/confirm/2?token=%s\n");
    return message;
  }

}
