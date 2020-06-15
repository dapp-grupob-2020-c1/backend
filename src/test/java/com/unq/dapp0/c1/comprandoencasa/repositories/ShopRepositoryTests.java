package com.unq.dapp0.c1.comprandoencasa.repositories;

import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@ActiveProfiles("test")
public class ShopRepositoryTests {

    @Autowired
    private ShopRepository shopRepository;


    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void givenACorrectSetupthenAnEntityManagerWillBeAvailable() {
        assertNotNull(entityManager);
    }

    @Test
    public void repositoryCanSaveAndReturnAGivenShop(){
        Shop shop = new Shop();
        shopRepository.save(shop);
        Optional<Shop> result = shopRepository.findById(shop.getId());
        assertTrue(result.isPresent());
        assertEquals(shop.getId(), result.get().getId());
    }

    @Test
    public void repositoryReturnsEmptyOptionalIfTheShopWithTheGivenIdDoesNotExist(){
        Optional<Shop> result = shopRepository.findById(0L);
        assertFalse(result.isPresent());
    }

}
