package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Shop;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopDoesntExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class ShopServiceTests {

    @Autowired
    private ShopService shopService;

    @Test
    public void serviceCanSaveAndReturnAShop(){
        Shop shop = new Shop();
        shopService.save(shop);
        Shop result = shopService.findShopById(shop.getId());
        assertEquals(shop.getId(), result.getId());
    }

    @Test
    public void serviceThrowsShopDoesNotExistIfTheShopIsNotPresentWhenSearchingById(){
        Long id = 0L;
        Exception exception = assertThrows(ShopDoesntExistException.class, ()-> shopService.findShopById(id));
        assertNotNull(exception);

        String expected = "Shop with id " + id + " does not exist";

        assertEquals(expected, exception.getMessage());
    }
}
