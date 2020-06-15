package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.LocationBuilder;
import com.unq.dapp0.c1.comprandoencasa.model.ShopBuilder;
import com.unq.dapp0.c1.comprandoencasa.model.Manager;
import com.unq.dapp0.c1.comprandoencasa.model.ManagerBuilder;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;
import com.unq.dapp0.c1.comprandoencasa.model.ProductBuilder;

import com.unq.dapp0.c1.comprandoencasa.services.exceptions.LocationDoesNotExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.ProductDoesntExistException;
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
    private ManagerService managerService;

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

        List<Product> result = productService.searchBy(keyword, categories, searchLocation.getId(), 0, 3, "priceAsc");
        assertEquals(3, result.size());
        assertEquals(result.get(0).getId(), product1.getId());
        assertEquals(result.get(1).getId(), product2.getId());
        assertEquals(result.get(2).getId(), product4.getId());
    }

    @Test
    public void serviceThrowsLocationDoesNotExistExceptionIfTheLocationIdUsedForSearchByIsInvalid(){
        List<ProductType> categories = new ArrayList<>();
        Long locationId = 0L;
        Exception exception = assertThrows(LocationDoesNotExistException.class, ()-> productService.searchBy("", categories, locationId, 0, 3, "priceAsc"));

        assertNotNull(exception);

        String expected = "Location with the id " + locationId + " does not exist";

        assertEquals(expected, exception.getMessage());
    }
}
