package com.unq.dapp0.c1.comprandoencasa.repositories;

import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.LocationBuilder;
import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.ProductBuilder;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;
import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.ShopBuilder;
import com.unq.dapp0.c1.comprandoencasa.model.User;
import com.unq.dapp0.c1.comprandoencasa.model.UserBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void givenACorrectSetupthenAnEntityManagerWillBeAvailable() {
        assertNotNull(entityManager);
    }

    @Test
    public void repositoryCanSaveAndReturnAGivenProduct(){
        Product product = new Product();
        productRepository.save(product);
        Optional<Product> result = productRepository.findById(product.getId());
        assertTrue(result.isPresent());
        assertEquals(product.getId(), result.get().getId());
    }

    @Test
    public void repositoryReturnsEmptyOptionalIfTheProductWithTheGivenIdDoesNotExist(){
        Optional<Product> result = productRepository.findById(0L);
        assertFalse(result.isPresent());
    }

    @Test
    public void repositorySearchByReturnsAListOfProductsBasedOnAKeywordCategoriesLatitudeLongitudeAndAPageable(){
        Location nearLocation1 = LocationBuilder.anyLocation()
                .withCoordinates(-34.709572, -58.280855).build(); //Estacion Bernal
        Location nearLocation2 = LocationBuilder.anyLocation()
                .withCoordinates(-34.724440, -58.260724).build(); //Estacion Quilmes
        Location farLocation1 = LocationBuilder.anyLocation()
                .withCoordinates(-34.921477, -57.954431).build(); //Plaza Moreno, La Plata
        Location farLocation2 = LocationBuilder.anyLocation()
                .withCoordinates(-35.577200, -58.013928).build(); //Chascomus

        User manager = UserBuilder.anyUser().build();

        Shop shop1 = ShopBuilder.anyShop().withLocation(nearLocation1).build();
        Shop shop2 = ShopBuilder.anyShop().withLocation(nearLocation2).build();
        Shop shop3 = ShopBuilder.anyShop().withLocation(farLocation1).build();
        Shop shop4 = ShopBuilder.anyShop().withLocation(farLocation2).build();

        List<ProductType> validTypes1 = new ArrayList<>();
        validTypes1.add(ProductType.Bazaar);
        validTypes1.add(ProductType.BooksMoviesAndGames);
        List<ProductType> validTypes2 = new ArrayList<>();
        validTypes2.add(ProductType.FoodsAndDrinks);
        validTypes2.add(ProductType.Clothing);
        List<ProductType> invalidTypes = new ArrayList<>();
        invalidTypes.add(ProductType.BooksMoviesAndGames);
        invalidTypes.add(ProductType.Clothing);

        Product product1 = ProductBuilder.aProduct().withName("lata foo")
                .withTypes(validTypes1)
                .withPrice(BigDecimal.valueOf(2))
                .withShop(shop1).build();
        Product product2 = ProductBuilder.aProduct().withName("flataoo")
                .withTypes(validTypes2)
                .withPrice(BigDecimal.valueOf(3))
                .withShop(shop1).build();
        Product product3 = ProductBuilder.aProduct().withName("foo")
                .withTypes(invalidTypes)
                .withShop(shop1).build();
        Product product4 = ProductBuilder.aProduct().withName("foo lata")
                .withTypes(validTypes1)
                .withPrice(BigDecimal.valueOf(4))
                .withShop(shop2).build();
        Product product5 = ProductBuilder.aProduct().withName("fool")
                .withTypes(validTypes2)
                .withShop(shop2).build();
        Product product6 = ProductBuilder.aProduct().withName("foolata")
                .withTypes(validTypes1)
                .withShop(shop3).build();
        Product product7 = ProductBuilder.aProduct().withName("lata")
                .withTypes(validTypes2)
                .withShop(shop4).build();

        userRepository.save(manager);

        locationRepository.save(nearLocation1);
        locationRepository.save(nearLocation2);
        locationRepository.save(farLocation1);
        locationRepository.save(farLocation2);

        shopRepository.save(shop1);
        shopRepository.save(shop2);
        shopRepository.save(shop3);
        shopRepository.save(shop4);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
        productRepository.save(product5);
        productRepository.save(product6);
        productRepository.save(product7);

        shop1.setUser(manager);
        shop2.setUser(manager);
        shop3.setUser(manager);
        shop4.setUser(manager);

        shopRepository.save(shop1);
        shopRepository.save(shop2);
        shopRepository.save(shop3);
        shopRepository.save(shop4);

        String keyword = "lata";
        List<ProductType> categories = new ArrayList<>();
        categories.add(ProductType.Bazaar);
        categories.add(ProductType.FoodsAndDrinks);

        Location searchLocation = LocationBuilder.anyLocation()
                .withCoordinates(-34.706519, -58.278026).build(); //UNQ

        Pageable page = PageRequest.of(0, 3, Sort.by("price").ascending());

        List<Product> result = productRepository.searchBy(keyword, categories, searchLocation.getLatitude(), searchLocation.getLongitude(), page);
        assertEquals(3, result.size());
        assertEquals(result.get(0).getId(), product1.getId());
        assertEquals(result.get(1).getId(), product2.getId());
        assertEquals(result.get(2).getId(), product4.getId());

    }
}
