package com.unq.dapp0.c1.comprandoencasa.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class MailService {
    public static int noOfQuickServiceThreads = 20;
    private static final Logger logger = LogManager.getLogger(MailService.class);
    /**
     * this statement create a thread pool of twenty threads
     * here we are assigning send mail task using ScheduledExecutorService.submit();
     */
    private ScheduledExecutorService quickService = Executors.newScheduledThreadPool(noOfQuickServiceThreads); // Creates a thread pool that reuses fixed number of threads(as specified by noOfThreads in this case).

    @Autowired
    private JavaMailSender sender;




    public void sendASynchronousMail(String toEmail,String subject,String text) throws MailException,RuntimeException{
        logger.info("Sending email to " + toEmail + " with subject " + subject);
        SimpleMailMessage mail=new SimpleMailMessage();
        mail.setFrom("dapp2020grupoc@gmail.com");
        mail.setTo(toEmail);
        mail.setSubject(subject);
        mail.setText(text);
        quickService.submit(() -> {
            try{
                sender.send(mail);
            }catch(Exception e){
                logger.error("Exception occur while send a mail : ",e);
            }
        });
    }

}