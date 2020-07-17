package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.objects.DeliveryAtShop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShopDelivery;
import com.unq.dapp0.c1.comprandoencasa.repositories.DeliveryRepository;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.EmailServiceExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private MailService mailService;

    public void delete(ShopDelivery delivery) {
        this.deliveryRepository.delete(delivery);
    }

    @Transactional
    public void notifyMailDeliveries(){
        List<String> exceptions = new ArrayList<>();
        LocalDateTime startDate = LocalDateTime.now().plusHours(2).minusMinutes(15);
        LocalDateTime endDate = LocalDateTime.now().plusHours(2).plusMinutes(15);
        List<ShopDelivery> deliveryList = this.deliveryRepository.getAllActiveScheduledDeliveriesBetween(startDate, endDate);
        for (ShopDelivery delivery : deliveryList){
            try{
                sendMailReminder((DeliveryAtShop)delivery);
            } catch (MessagingException exception){
                exceptions.add(exception.getLocalizedMessage());
            }
        }
        if (!exceptions.isEmpty()){
            throw new EmailServiceExceptions(exceptions);
        }
    }

    private void sendMailReminder(DeliveryAtShop delivery) throws MessagingException {
        String to = delivery.getUser().getEmail();
        String subject = "Reminder: You have a scheduled turn for " + delivery.getShop().getName();
        String body = "Hello, dear customer! This is a reminder for your appointment at "
                + delivery.getShop().getName() +
                " to pick up your purchase. Your turn is in 2 hours, at " +
                delivery.getTurn().getTime().toLocalTime().toString() +
                ", don't be late! Best regards, Comprando en Casa.";
        mailService.sendASynchronousMail(to, subject, body);
    }
}
