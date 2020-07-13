package com.unq.dapp0.c1.comprandoencasa.repositories;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Product;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ProductType;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Discount;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DiscountByCategory;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DiscountByMultiple;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DiscountBySingle;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
public class DiscountRepositoryTests {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void givenACorrectSetupthenAnEntityManagerWillBeAvailable() {
        assertNotNull(entityManager);
    }

    @Test
    public void repositoryCanSaveAndRetrieveAnyDiscountType(){
        Shop shop = new Shop();
        shopRepository.save(shop);

        double percentage = 1D;
        LocalDate start = LocalDate.of(1, 1, 1);
        LocalDate end = LocalDate.of(2, 1, 1);
        ProductType productType = ProductType.Bazaar;

        Discount discountByCat = new DiscountByCategory(percentage, start, end, shop, productType);
        discountRepository.save(discountByCat);
        Optional<Discount> retrievedDiscountByCat = discountRepository.findById(discountByCat.getId());

        assertTrue(retrievedDiscountByCat.isPresent());
        assertEquals(discountByCat.getId(), retrievedDiscountByCat.get().getId());
        assertTrue(retrievedDiscountByCat.get() instanceof DiscountByCategory);
        assertEquals(productType, ((DiscountByCategory) retrievedDiscountByCat.get()).getProductType());

        Product product1 = new Product();
        productRepository.save(product1);

        Discount discountBySingle = new DiscountBySingle(percentage, start, end, shop, product1);
        discountRepository.save(discountBySingle);
        Optional<Discount> retrievedDiscountBySingle = discountRepository.findById(discountBySingle.getId());

        assertTrue(retrievedDiscountBySingle.isPresent());
        assertEquals(discountBySingle.getId(), retrievedDiscountBySingle.get().getId());
        assertTrue(retrievedDiscountBySingle.get() instanceof DiscountBySingle);
        assertEquals(product1.getId(), ((DiscountBySingle) retrievedDiscountBySingle.get()).getProduct().getId());

        Product product2 = new Product();
        productRepository.save(product2);
        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);

        Discount discountByMultiple = new DiscountByMultiple(percentage, start, end, shop, productList);
        discountRepository.save(discountByMultiple);
        Optional<Discount> retrievedDiscountByMultiple = discountRepository.findById(discountByMultiple.getId());

        assertTrue(retrievedDiscountByMultiple.isPresent());
        assertEquals(discountByMultiple.getId(), retrievedDiscountByMultiple.get().getId());
        assertTrue(retrievedDiscountByMultiple.get() instanceof DiscountByMultiple);
        Collection<Product> retrievedProducts = ((DiscountByMultiple) retrievedDiscountByMultiple.get()).getProducts();
        assertEquals(2, retrievedProducts.size());
        assertTrue(retrievedProducts.stream().anyMatch(product -> product.getId().equals(product1.getId())));
        assertTrue(retrievedProducts.stream().anyMatch(product -> product.getId().equals(product2.getId())));
    }
}
