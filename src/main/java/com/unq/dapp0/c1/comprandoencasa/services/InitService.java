package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Discount;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DiscountByCategory;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DiscountByMultiple;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DiscountBySingle;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.model.objects.PaymentMethod;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Product;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ProductType;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShopCategory;
import com.unq.dapp0.c1.comprandoencasa.model.objects.User;

import com.unq.dapp0.c1.comprandoencasa.repositories.DiscountRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.LocationRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.ProductRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.ShopRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InitService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void initialize() {

        //Initializing objects

        LocalTime opening = LocalTime.of(9, 30, 0, 0);
        LocalTime closing = LocalTime.of(18, 0, 0, 0);

        User aShopManager = new User("Manager", "123456", "manager@example.com");
        aShopManager.setPassword(passwordEncoder.encode(aShopManager.getPassword()));
        this.userRepository.save(aShopManager);

        Location aShopLocation = new Location("AAA 123", 1234d, 1234d);

        Shop aShop = new Shop(
                "La Marca",
                new ArrayList<ShopCategory>(),
                aShopLocation,
                new ArrayList<DayOfWeek>(),
                opening,
                closing,
                new ArrayList<PaymentMethod>(),
                10,
                "");

        Product aProduct = new Product(
                "Lata de Atún",
                "Pepito",
                "https://picsum.photos/200/300",
                new BigDecimal("99.99"),
                aShop,
                new ArrayList<>()
        );

        Product anotherProduct = new Product(
                "Lata de Sardinas",
                "Pepito",
                "https://picsum.photos/200/200",
                new BigDecimal("89.99"),
                aShop,
                new ArrayList<>()
        );

        Location unqLocation = new Location("Roque Sáenz Peña 352, Bernal, Provincia de Buenos Aires",-34.706339, -58.278542);
        Location piaveBernalLocation = new Location("9 de Julio 2, Bernal, Provincia de Buenos Aires",-34.7098631, -58.2807604);
        Location outletQuilmLocation = new Location("Av. Hipólito Yrigoyen 85, Quilmes, Provincia de Buenos Aires",-34.7153311, -58.2667184);
        Location bkBeraLocation = new Location("Calle 14 Nº 4675 entre calle 147 y, Diagonal Lisandro de la Torre, Berazategui", -34.7623283, -58.2099812);
        Location piaveHudsonLocation = new Location("Calle 60 5400, B1885 Guillermo Hudson, Provincia de Buenos Aires", -34.797296, -58.1601194);
        Location clubMilaPlataLocation = new Location("Diagonal 74 1451, B1900BZK La Plata, Provincia de Buenos Aires", -34.9150418, -57.9551546);
        Location queimadaPlataLocation = new Location("Av. 60 26, B1904 La Plata, Provincia de Buenos Aires", -34.9388181, -57.9615092);
        Location contiBestHeladosLocation = new Location("Av. Villanueva y Maipu Ingeniero Maschwitz Buenos Aires AR Avenida Villanueva Maipú, B1623 Ingeniero Maschwitz, Provincia de Buenos Aires", -34.3883201, -58.7376738);

        List<ShopCategory> unqCat = new ArrayList<>();
        unqCat.add(ShopCategory.BooksMoviesAndGames);
        unqCat.add(ShopCategory.OfficeSupplies);
        unqCat.add(ShopCategory.Services);

        List<ShopCategory> foodCat = new ArrayList<>();
        foodCat.add(ShopCategory.FoodsAndDrinks);

        List<ShopCategory> shoppingCat = new ArrayList<>();
        shoppingCat.add(ShopCategory.Clothing);
        shoppingCat.add(ShopCategory.Bazaar);

        List<PaymentMethod> regularPay = new ArrayList<>();
        regularPay.add(PaymentMethod.CASH);

        List<DayOfWeek> regularDays = new ArrayList<>();
        regularDays.add(DayOfWeek.MONDAY);
        regularDays.add(DayOfWeek.TUESDAY);
        regularDays.add(DayOfWeek.WEDNESDAY);
        regularDays.add(DayOfWeek.THURSDAY);
        regularDays.add(DayOfWeek.FRIDAY);

        Shop unqShop = new Shop(
                "UNQ",
                unqCat,
                unqLocation,
                regularDays,
                opening,
                closing,
                regularPay,
                20,
                "");

        Shop piaveBernalShop = new Shop(
                "Piave Bernal",
                foodCat,
                piaveBernalLocation,
                regularDays,
                opening,
                closing,
                regularPay,
                10,
                "");

        Shop outletQuilShop = new Shop(
                "Outlet Quilmes",
                shoppingCat,
                outletQuilmLocation,
                regularDays,
                opening,
                closing,
                regularPay,
                5,
                "");

        Shop bkBeraShop = new Shop(
                "Burger King Berazategui",
                foodCat,
                bkBeraLocation,
                regularDays,
                opening,
                closing,
                regularPay,
                20,
                "");

        Shop piaveHudsonShop = new Shop(
                "Piave Hudson",
                foodCat,
                piaveHudsonLocation,
                regularDays,
                opening,
                closing,
                regularPay,
                20,
                "");

        Shop clubMilaPlataShop = new Shop(
                "El Club de la Milanesa La Plata",
                foodCat,
                clubMilaPlataLocation,
                regularDays,
                opening,
                closing,
                regularPay,
                10,
                "");

        Shop queimadaPlataShop = new Shop(
                "La Queimada La Plata",
                foodCat,
                queimadaPlataLocation,
                regularDays,
                opening,
                closing,
                regularPay,
                10,
                "");

        Shop contiBestHeladosShop = new Shop(
                "Heladeria Conti",
                foodCat,
                contiBestHeladosLocation,
                regularDays,
                opening,
                closing,
                regularPay,
                20,
                "");

        List<ProductType> bookTypes = new ArrayList<>();
        bookTypes.add(ProductType.BooksMoviesAndGames);

        List<ProductType> officeTypes = new ArrayList<>();
        officeTypes.add(ProductType.OfficeSupplies);

        List<ProductType> foodTypes = new ArrayList<>();
        foodTypes.add(ProductType.FoodsAndDrinks);

        List<ProductType> clothesTypes = new ArrayList<>();
        clothesTypes.add(ProductType.Clothing);

        Product unqBook = new Product(
                "Manual Genérico",
                "UNQEdiciones",
                "https://thumbs.dreamstime.com/z/manual-gen%C3%A9rico-1299018.jpg",
                new BigDecimal("100"),
                unqShop,
                bookTypes
        );

        Product unqGear = new Product(
                "Kit de Supervivencia del Estudiante",
                "UNQSurvival",
                "https://i.pinimg.com/originals/d9/68/df/d968dfb0143659841a6e7f114f79a606.jpg",
                new BigDecimal("150"),
                unqShop,
                officeTypes
        );

        Product piaveBerTorta = new Product(
                "Torta de Chocolate",
                "Casero",
                "https://ichef.bbci.co.uk/food/ic/food_16x9_1600/recipes/easy_chocolate_cake_31070_16x9.jpg",
                new BigDecimal("200"),
                piaveBernalShop,
                foodTypes
        );

        Product piaveBerSang = new Product(
                "Sandwich de Queso",
                "Casero",
                "https://www.cocinacaserayfacil.net/wp-content/uploads/2019/10/Sandwich-de-queso-fundido-1000x563.jpg",
                new BigDecimal("50"),
                piaveBernalShop,
                foodTypes
        );

        Product outRemera = new Product(
                "Remera del Pity",
                "El Pity",
                "https://http2.mlstatic.com/remeras-pity-alvarez-la-remera-de-fausto--D_NQ_NP_917495-MLA26356961121_112017-F.webp",
                new BigDecimal("500"),
                outletQuilShop,
                clothesTypes
        );

        Product bkBeraHamb = new Product(
                "Hamburguesa Doble",
                "Burger King Cats",
                "https://i.pinimg.com/originals/5c/cb/66/5ccb6603e89fb8b4b52bd7b9ace51ad0.jpg",
                new BigDecimal("100"),
                bkBeraShop,
                foodTypes
        );

        Product piaveHudTorta = new Product(
                "Torta de Chocolate",
                "Casero",
                "https://ichef.bbci.co.uk/food/ic/food_16x9_1600/recipes/easy_chocolate_cake_31070_16x9.jpg",
                new BigDecimal("200"),
                piaveHudsonShop,
                foodTypes
        );

        Product clubMilaSuper = new Product(
                "Super Milanesa a Caballo",
                "El Club de la Milanesa",
                "https://milrecetas.net/wp-content/uploads/2016/02/Milanesas-a-caballo-1-950x633.jpg",
                new BigDecimal("200"),
                clubMilaPlataShop,
                foodTypes
        );

        Product clubMilaHiper = new Product(
                "Hiper Sanguche de Milanesa",
                "El Club de la Milanesa",
                "https://images.clarin.com/2019/08/20/parte-del-enorme-sandwich-al___HrRLSt-EU_1200x0__1.jpg",
                new BigDecimal("1000"),
                clubMilaPlataShop,
                foodTypes
        );

        Product queimadaGeneric = new Product(
                "Plato de comida Genérico",
                "No se que sirven",
                "https://policart.com.ar/wp-content/uploads/2019/02/PLATO2-510x517.jpg",
                new BigDecimal("1"),
                queimadaPlataShop,
                foodTypes
        );

        Product bestHelado = new Product(
                "Cucurucho Conti",
                "Best Helados Ever",
                "https://nonperfect.files.wordpress.com/2014/07/c2.jpg",
                new BigDecimal("500"),
                contiBestHeladosShop,
                foodTypes
        );


        //Saving initial elements

        this.locationRepository.save(aShopLocation);
        this.locationRepository.save(unqLocation);
        this.locationRepository.save(piaveBernalLocation);
        this.locationRepository.save(outletQuilmLocation);
        this.locationRepository.save(bkBeraLocation);
        this.locationRepository.save(piaveHudsonLocation);
        this.locationRepository.save(clubMilaPlataLocation);
        this.locationRepository.save(queimadaPlataLocation);
        this.locationRepository.save(contiBestHeladosLocation);

        this.shopRepository.save(aShop);
        this.shopRepository.save(unqShop);
        this.shopRepository.save(piaveBernalShop);
        this.shopRepository.save(outletQuilShop);
        this.shopRepository.save(bkBeraShop);
        this.shopRepository.save(piaveHudsonShop);
        this.shopRepository.save(clubMilaPlataShop);
        this.shopRepository.save(queimadaPlataShop);
        this.shopRepository.save(contiBestHeladosShop);

        this.productRepository.save(aProduct);
        this.productRepository.save(anotherProduct);
        this.productRepository.save(unqBook);
        this.productRepository.save(unqGear);
        this.productRepository.save(piaveBerTorta);
        this.productRepository.save(piaveBerSang);
        this.productRepository.save(outRemera);
        this.productRepository.save(bkBeraHamb);
        this.productRepository.save(piaveHudTorta);
        this.productRepository.save(clubMilaSuper);
        this.productRepository.save(clubMilaHiper);
        this.productRepository.save(queimadaGeneric);
        this.productRepository.save(bestHelado);

        //Discounts
        LocalDate startingDate = LocalDate.of(2020, 6, 10);
        LocalDate endingDate = LocalDate.of(2020, 6, 30);

        Discount randomUNQDiscount = new DiscountBySingle(
                20,
                startingDate,
                endingDate,
                unqShop,
                unqGear
                );

        Discount randomPiaveDiscount = new DiscountByCategory(
                10,
                startingDate,
                endingDate,
                piaveBernalShop,
                ProductType.FoodsAndDrinks
        );

        Discount randomMilaDiscount = new DiscountByMultiple(
                15,
                startingDate,
                endingDate,
                clubMilaPlataShop,
                clubMilaPlataShop.getProducts()
        );

        discountRepository.save(randomUNQDiscount);
        discountRepository.save(randomPiaveDiscount);
        discountRepository.save(randomMilaDiscount);

        //Updating elements

        aShop.addProduct(aProduct);
        aShop.addProduct(anotherProduct);
        unqShop.addProduct(unqBook);
        unqShop.addProduct(unqGear);
        piaveBernalShop.addProduct(piaveBerTorta);
        piaveBernalShop.addProduct(piaveBerSang);
        outletQuilShop.addProduct(outRemera);
        bkBeraShop.addProduct(bkBeraHamb);
        piaveHudsonShop.addProduct(piaveHudTorta);
        clubMilaPlataShop.addProduct(clubMilaSuper);
        clubMilaPlataShop.addProduct(clubMilaHiper);
        queimadaPlataShop.addProduct(queimadaGeneric);
        contiBestHeladosShop.addProduct(bestHelado);

        unqShop.addDiscount(randomUNQDiscount);
        piaveBernalShop.addDiscount(randomPiaveDiscount);
        clubMilaPlataShop.addDiscount(randomMilaDiscount);

        aShop.setUser(aShopManager);
        unqShop.setUser(aShopManager);
        piaveBernalShop.setUser(aShopManager);
        outletQuilShop.setUser(aShopManager);
        bkBeraShop.setUser(aShopManager);
        piaveHudsonShop.setUser(aShopManager);
        clubMilaPlataShop.setUser(aShopManager);
        queimadaPlataShop.setUser(aShopManager);
        contiBestHeladosShop.setUser(aShopManager);

        this.userRepository.save(aShopManager);

        this.shopRepository.save(aShop);
        this.shopRepository.save(unqShop);
        this.shopRepository.save(piaveBernalShop);
        this.shopRepository.save(outletQuilShop);
        this.shopRepository.save(bkBeraShop);
        this.shopRepository.save(piaveHudsonShop);
        this.shopRepository.save(clubMilaPlataShop);
        this.shopRepository.save(queimadaPlataShop);
        this.shopRepository.save(contiBestHeladosShop);
    }
}
