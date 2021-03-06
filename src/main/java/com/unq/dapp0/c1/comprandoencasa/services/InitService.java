package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.objects.DeliveryAtHome;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DeliveryAtShop;
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
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingList;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingListEntry;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Turn;
import com.unq.dapp0.c1.comprandoencasa.model.objects.User;
import com.unq.dapp0.c1.comprandoencasa.repositories.DeliveryRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.DiscountRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.LocationRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.ProductRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.ShopRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.ShoppingListEntryRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.ShoppingListRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.TurnRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private ShoppingListEntryRepository shoppingListEntryRepository;

    @Autowired
    private TurnRepository turnRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

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

        List<PaymentMethod> digitalPaymentMethods = new ArrayList<>();
        digitalPaymentMethods.add(PaymentMethod.DEBIT);
        digitalPaymentMethods.add(PaymentMethod.CREDIT);
        digitalPaymentMethods.add(PaymentMethod.MERCADOPAGO);

        List<PaymentMethod> allPaymentMethods = new ArrayList<>();
        allPaymentMethods.add(PaymentMethod.CASH);
        allPaymentMethods.add(PaymentMethod.DEBIT);
        allPaymentMethods.add(PaymentMethod.CREDIT);
        allPaymentMethods.add(PaymentMethod.MERCADOPAGO);

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
                LocalTime.of(11, 0, 0, 0),
                LocalTime.of(15, 0, 0, 0),
                allPaymentMethods,
                20,
                "https://eltermometroweb.com/wp-content/uploads/2020/03/UNQ.jpg");

        Shop piaveBernalShop = new Shop(
                "Piave Bernal",
                foodCat,
                piaveBernalLocation,
                regularDays,
                opening,
                closing,
                digitalPaymentMethods,
                10,
                "https://dondecomequilmes.com/wp-content/uploads/2016/05/El_Piave_lgn1.jpg");

        Shop outletQuilShop = new Shop(
                "Outlet Quilmes",
                shoppingCat,
                outletQuilmLocation,
                regularDays,
                LocalTime.of(6, 0, 0, 0),
                LocalTime.of(19, 0, 0, 0),
                regularPay,
                5,
                "https://www.perspectivasur.com/archivos/noticias/fotografias/60035_3.jpg");

        Shop bkBeraShop = new Shop(
                "Burger King Berazategui",
                foodCat,
                bkBeraLocation,
                regularDays,
                opening,
                closing,
                allPaymentMethods,
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
                LocalTime.of(18, 0, 0, 0),
                LocalTime.of(23, 0, 0, 0),
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

        this.locationRepository.save(unqLocation);
        this.locationRepository.save(piaveBernalLocation);
        this.locationRepository.save(outletQuilmLocation);
        this.locationRepository.save(bkBeraLocation);
        this.locationRepository.save(piaveHudsonLocation);
        this.locationRepository.save(clubMilaPlataLocation);
        this.locationRepository.save(queimadaPlataLocation);
        this.locationRepository.save(contiBestHeladosLocation);

        this.shopRepository.save(unqShop);
        this.shopRepository.save(piaveBernalShop);
        this.shopRepository.save(outletQuilShop);
        this.shopRepository.save(bkBeraShop);
        this.shopRepository.save(piaveHudsonShop);
        this.shopRepository.save(clubMilaPlataShop);
        this.shopRepository.save(queimadaPlataShop);
        this.shopRepository.save(contiBestHeladosShop);

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

        unqShop.setUser(aShopManager);
        piaveBernalShop.setUser(aShopManager);
        outletQuilShop.setUser(aShopManager);
        bkBeraShop.setUser(aShopManager);
        piaveHudsonShop.setUser(aShopManager);
        clubMilaPlataShop.setUser(aShopManager);
        queimadaPlataShop.setUser(aShopManager);
        contiBestHeladosShop.setUser(aShopManager);

        this.userRepository.save(aShopManager);

        this.shopRepository.save(unqShop);
        this.shopRepository.save(piaveBernalShop);
        this.shopRepository.save(outletQuilShop);
        this.shopRepository.save(bkBeraShop);
        this.shopRepository.save(piaveHudsonShop);
        this.shopRepository.save(clubMilaPlataShop);
        this.shopRepository.save(queimadaPlataShop);
        this.shopRepository.save(contiBestHeladosShop);

        ShoppingList aShoppingList = new ShoppingList(unqLocation, aShopManager);
        ShoppingListEntry oneEntry = aShoppingList.addProduct(unqBook, 2);
        ShoppingListEntry anotherEntry = aShoppingList.addProduct(unqGear, 1);

        DeliveryAtHome aDelivery = new DeliveryAtHome(
                unqShop,
                aShoppingList.getEntriesList(),
                aShopManager, unqLocation,
                LocalDateTime.of(2020, 6, 1, 20, 0));

        ShoppingList aShoppingList2 = new ShoppingList(unqLocation, aShopManager);
        ShoppingListEntry oneEntry2 = aShoppingList2.addProduct(unqBook, 4);

        Turn aTurn = new Turn(unqShop, LocalDateTime.of(2020, 7, 1, 10, 0));

        DeliveryAtShop anotherDelivery = new DeliveryAtShop(
                unqShop, aShoppingList2.getEntriesList(), aShopManager, aTurn);

        aShopManager.addLocation(unqLocation);
        aShopManager.setTotalThreshold(BigDecimal.valueOf(1000));
        aShopManager.addHistoricShoppingList(aShoppingList);
        aShopManager.addHistoricShoppingList(aShoppingList2);
        aShopManager.addNewDelivery(aDelivery);
        aShopManager.addNewDelivery(anotherDelivery);
        aShopManager.confirmDeliveryReception(aDelivery);

        this.shoppingListEntryRepository.save(oneEntry);
        this.shoppingListEntryRepository.save(oneEntry2);
        this.shoppingListEntryRepository.save(anotherEntry);

        this.shoppingListRepository.save(aShoppingList);
        this.shoppingListRepository.save(aShoppingList2);

        this.turnRepository.save(aTurn);

        this.deliveryRepository.save(aDelivery);
        this.deliveryRepository.save(anotherDelivery);

        this.userRepository.save(aShopManager);
    }
}
