//package com.movie.app.security.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Properties;
//
//@Configuration
//public class MailConfig {
//
//    @Value("${spring.mail.password}")
//    private String password;
//
//    @Value("${spring.mail.username}")
//    private String sender;
//
//    @Value("${spring.mail.host}")
//    private String host;
//
//    @Value("${spring.mail.port}")
//    private int port;
//
//    @Bean
//    public JavaMailSender javaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(host);
//        mailSender.setPort(port);
//        mailSender.setUsername(sender);
//        mailSender.setPassword(password);
//
//        Properties properties = mailSender.getJavaMailProperties();
//        properties.put("mail.transport.protocol", "smtp");
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true");
//        properties.put("mail.smtp.starttls.required", "true");
//        properties.put("mail.smtp.connectiontimeout", "5000");
//        properties.put("mail.smtp.timeout", "3000");
//        properties.put("mail.smtp.writetimeout", "5000");
//        properties.put("mail.debug", "true");
//
//        return mailSender;
//    }
//
//    @Bean
//    public String sender() {
//        return sender;
//    }
//}
