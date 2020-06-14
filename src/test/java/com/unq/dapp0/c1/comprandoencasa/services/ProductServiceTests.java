package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class ProductServiceTests {

    @Autowired
    ProductService productService;

    @Autowired
    LocationService locationService;

    @Autowired
    ShopService shopService;

    @Autowired
    ManagerService managerService;

    @Test
    public void serviceCanReturnAListOfProductsBeingSearched(){
        Location nearLocation1 = LocationBuilder.anyLocation()
                .withCoordinates(-34.709572, -58.280855).build(); //Estacion Bernal
        Location nearLocation2 = LocationBuilder.anyLocation()
                .withCoordinates(-34.724440, -58.260724).build(); //Estacion Quilmes
        Location farLocation1 = LocationBuilder.anyLocation()
                .withCoordinates(-34.921477, -57.954431).build(); //Plaza Moreno, La Plata
        Location farLocation2 = LocationBuilder.anyLocation()
                .withCoordinates(-35.577200, -58.013928).build(); //Chascomus

        Manager manager = ManagerBuilder.anyManager().build();

        Shop shop1 = ShopBuilder.anyShop().withLocation(nearLocation1)
                .withManager(manager).build();
        Shop shop2 = ShopBuilder.anyShop().withLocation(nearLocation2)
                .withManager(manager).build();
        Shop shop3 = ShopBuilder.anyShop().withLocation(farLocation1)
                .withManager(manager).build();
        Shop shop4 = ShopBuilder.anyShop().withLocation(farLocation2)
                .withManager(manager).build();

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
                .withShop(shop1).build();
        Product product2 = ProductBuilder.aProduct().withName("flataoo")
                .withTypes(validTypes2)
                .withShop(shop1).build();
        Product product3 = ProductBuilder.aProduct().withName("foo")
                .withTypes(invalidTypes)
                .withShop(shop1).build();
        Product product4 = ProductBuilder.aProduct().withName("foo lata")
                .withTypes(validTypes1)
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

        managerService.save(manager);

        locationService.save(nearLocation1);
        locationService.save(nearLocation2);
        locationService.save(farLocation1);
        locationService.save(farLocation2);

        shopService.save(shop1);
        shopService.save(shop2);
        shopService.save(shop3);
        shopService.save(shop4);

        productService.save(product1);
        productService.save(product2);
        productService.save(product3);
        productService.save(product4);
        productService.save(product5);
        productService.save(product6);
        productService.save(product7);

        String keyword = "lata";
        List<ProductType> categories = new ArrayList<>();
        categories.add(ProductType.Bazaar);
        categories.add(ProductType.FoodsAndDrinks);

        Location searchLocation = LocationBuilder.anyLocation()
                .withCoordinates(-34.706519, -58.278026).build(); //UNQ

        locationService.save(searchLocation);

        List<Product> result = productService.searchBy(keyword, categories, searchLocation.getID(), 0, 3);
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(product -> product.getId().equals(product1.getId())));
        assertTrue(result.stream().anyMatch(product -> product.getId().equals(product2.getId())));
        assertTrue(result.stream().anyMatch(product -> product.getId().equals(product4.getId())));
    }
}
