package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import com.unq.dapp0.c1.comprandoencasa.services.ShopService;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class ShopController {

    @Autowired
    private ShopService shopService;

    @CrossOrigin
    @GetMapping("/api/shop")
    public ShopDTO getShop(@RequestParam(value = "shopId") String shopId) {
        try{
            Shop shop = this.shopService.findShopById(Long.valueOf(shopId));
            return new ShopDTO(shop);
        } catch (ShopDoesntExistException exception){
            throw new ShopNotFoundException(shopId);
        }
    }
}
