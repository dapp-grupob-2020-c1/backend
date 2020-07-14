package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.objects.DeliveryAtShop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShopDelivery;
import com.unq.dapp0.c1.comprandoencasa.repositories.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public void notifyMailDeliveries() {
        LocalDateTime startDate = LocalDateTime.now().minusMinutes(15);
        LocalDateTime endDate = LocalDateTime.now().plusMinutes(15);
        List<ShopDelivery> deliveryList = this.deliveryRepository.getAllActiveScheduledDeliveriesBetween(startDate, endDate);
        for (ShopDelivery delivery : deliveryList){
            sendMailReminder((DeliveryAtShop)delivery);
        }
    }

    private void sendMailReminder(DeliveryAtShop delivery) {
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
