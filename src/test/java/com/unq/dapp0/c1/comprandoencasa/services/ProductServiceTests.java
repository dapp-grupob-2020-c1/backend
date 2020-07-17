package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.objects.*;
import com.unq.dapp0.c1.comprandoencasa.model.LocationBuilder;
import com.unq.dapp0.c1.comprandoencasa.model.ProductBuilder;
import com.unq.dapp0.c1.comprandoencasa.model.ShopBuilder;
import com.unq.dapp0.c1.comprandoencasa.model.UserBuilder;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.LocationDoesNotExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.ProductDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.UserDoesntExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private UserService userService;

    @Test
    public void serviceCanSaveAndReturnAProduct(){
        Product product = new Product();
        productService.save(product);
        Product result = productService.findProductById(product.getId());
        assertEquals(product.getId(), result.getId());
    }

    @Test
    public void serviceThrowsProductDoesNotExistIfTheProductIsNotPresentWhenSearchingById(){
        Long id = 0L;
        Exception exception = assertThrows(ProductDoesntExistException.class, ()-> productService.findProductById(id));
        assertNotNull(exception);

        String expected = "Product with id " + id + " does not exist";

        assertEquals(expected, exception.getMessage());
    }

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

        userService.save(manager);

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

        shop1.setUser(manager);
        shop2.setUser(manager);
        shop3.setUser(manager);
        shop4.setUser(manager);

        shopService.save(shop1);
        shopService.save(shop2);
        shopService.save(shop3);
        shopService.save(shop4);

        String keyword = "lata";
        List<ProductType> categories = new ArrayList<>();
        categories.add(ProductType.Bazaar);
        categories.add(ProductType.FoodsAndDrinks);

        Location searchLocation = userService.addLocationTo(manager.getId(), "any", -34.706519, -58.278026); //UNQ

        userService.createShoppingList(manager.getId(), searchLocation.getId());

        List<Product> result = productService.searchBy(manager.getId(), keyword, categories, 0, 3, "priceAsc");
        assertEquals(3, result.size());
        assertEquals(result.get(0).getId(), product1.getId());
        assertEquals(result.get(1).getId(), product2.getId());
        assertEquals(result.get(2).getId(), product4.getId());
    }

    @Test
    public void serviceThrowsUserDoesNotExistExceptionIfTheUserIdUsedForSearchByIsInvalid(){
        List<ProductType> categories = new ArrayList<>();
        Long userId = 0L;
        Exception exception = assertThrows(UserDoesntExistException.class, ()-> productService.searchBy(userId,"", categories, 0, 3, "priceAsc"));

        assertNotNull(exception);

        String expected = "User with id " + userId + " does not exist";

        assertEquals(expected, exception.getMessage());
    }
}
