package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.Manager;
import com.unq.dapp0.c1.comprandoencasa.model.PaymentMethod;
import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.ShopCategory;

import com.unq.dapp0.c1.comprandoencasa.repositories.ManagerRepositroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

@Service
@Transactional
public class InitService {

    @Autowired
    private ProductService productService;

    @Autowired
    private ManagerRepositroy managerRepositroy;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ShopService shopService;

    @PostConstruct
    public void initialize() {
        LocalTime opening = LocalTime.of(9, 30, 0, 0);
        LocalTime closing = LocalTime.of(18, 0, 0, 0);

        Manager aShopManager = new Manager("Manager", "123456", "manager@example.com", null);
        this.managerRepositroy.save(aShopManager);

        Location aShopLocation = new Location("AAA 123", 1234d, 1234d);
        this.locationService.save(aShopLocation);

        Shop aShop = new Shop(
                new ArrayList<ShopCategory>(),
                aShopLocation,
                new ArrayList<DayOfWeek>(),
                opening,
                closing,
                new ArrayList<PaymentMethod>(),
                10,
                aShopManager,
                new ArrayList<Product>()
        );
        this.shopService.save(aShop);

        Product aProduct = new Product(
                "Lata de Atún",
                "Pepito",
                "https://picsum.photos/200/300",
                new BigDecimal("99.99"),
                aShop
        );
        this.productService.save(aProduct);

        Product anotherProduct = new Product(
                "Lata de Sardinas",
                "Pepito",
                "https://picsum.photos/200/200",
                new BigDecimal("89.99"),
                aShop
        );
        this.productService.save(aProduct);
    }
}
