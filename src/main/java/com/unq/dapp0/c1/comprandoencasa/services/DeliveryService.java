package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.ShopDelivery;
import com.unq.dapp0.c1.comprandoencasa.repositories.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;

    public void delete(ShopDelivery delivery) {
        this.deliveryRepository.delete(delivery);
    }
}
