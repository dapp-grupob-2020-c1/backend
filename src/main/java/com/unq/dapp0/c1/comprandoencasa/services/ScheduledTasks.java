package com.unq.dapp0.c1.comprandoencasa.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private UserService userService;

    private static final Logger log = LogManager.getLogger(ScheduledTasks.class);

    //@Scheduled(fixedRate = 5000) //Runs every 5 seconds
    @Scheduled(fixedRate = 1800000) //Runs every half hour
    public void notifyMailDeliveries() {
        log.info("Starting mail notifications for deliveries.");
        try{
            deliveryService.notifyMailDeliveries();
            log.info("Mail notifications completed");
        } catch (Exception e){
            log.error("There has been an error during mail notification, with the following message: "+ e.getLocalizedMessage());
        }
    }

    @Scheduled(fixedRate = 120000) //Runs every 2 minutes
    //@Scheduled(fixedRate = 86400000) //Runs every 24hs
    public void calculateSuggestedThresholds(){
        log.info("Starting suggested thresholds calculations");
        try {
            this.userService.calculateAllSuggestedThresholds();
        } catch (Exception e){
            log.error("There has been an error during thresholds calculations " + e.getLocalizedMessage());
        }

    }
}