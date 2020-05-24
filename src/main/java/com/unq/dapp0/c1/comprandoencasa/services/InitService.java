package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.Manager;
import com.unq.dapp0.c1.comprandoencasa.model.PaymentMethod;
import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.ShopCategory;

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

    @Value("${spring.datasource.driverClassName:NONE}")
    private String driverClassName;

    @Autowired
    private ProductService productService;

    @PostConstruct
    public void initialize() {
        if (driverClassName.equals("org.h2.Driver")) {

            Location shopLocation = new Location("AAA 123", 1234d, 1234d);
            LocalTime opening = LocalTime.of(9, 30, 0, 0);
            LocalTime closing = LocalTime.of(18, 0, 0, 0);
            Manager shopManager = new Manager("Manager", "123456", "manager@example.com", null);

            Shop shop = new Shop(
                    new ArrayList<ShopCategory>(),
                    shopLocation,
                    new ArrayList<DayOfWeek>(),
                    opening,
                    closing,
                    new ArrayList<PaymentMethod>(),
                    10,
                    shopManager,
                    new ArrayList<Product>()
            );

            Product product = new Product(
                    "Lata de Atún",
                    "Pepito",
                    "https://picsum.photos/200/300",
                    new BigDecimal("99.99"),
                    shop
            );

            this.productService.save(product);
        }
    }
}
