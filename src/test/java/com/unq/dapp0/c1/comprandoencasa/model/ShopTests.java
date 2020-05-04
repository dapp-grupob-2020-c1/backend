package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShopTests {

    @Test
    public void aShopHasTheFoodsAndDrinksAndOfficeSuppliesCategories(){
        ArrayList<ShopCategory> shopCategories = new ArrayList<>();
        shopCategories.add(ShopCategory.FoodsAndDrinks);
        shopCategories.add(ShopCategory.OfficeSupplies);

        Shop shop = ShopFactory.aShop().withCategories(shopCategories).build();

        assertTrue(shop.getShopCategories().contains(ShopCategory.FoodsAndDrinks));
        assertTrue(shop.getShopCategories().contains(ShopCategory.OfficeSupplies));

        assertFalse(shop.getShopCategories().contains(ShopCategory.ElectronicsAndHomeAppliances));
        assertFalse(shop.getShopCategories().contains(ShopCategory.BooksMoviesAndGames));
        assertFalse(shop.getShopCategories().contains(ShopCategory.Services));
        assertFalse(shop.getShopCategories().contains(ShopCategory.Bazaar));
        assertFalse(shop.getShopCategories().contains(ShopCategory.PetsAndAnimals));
        assertFalse(shop.getShopCategories().contains(ShopCategory.VehiclesAndAccesories));
        assertFalse(shop.getShopCategories().contains(ShopCategory.Clothing));
        assertFalse(shop.getShopCategories().contains(ShopCategory.Pharmacy));
    }

    @Test
    public void aShopCanAddANewCategoryToHisList(){
        ArrayList<ShopCategory> shopCategories = new ArrayList<>();
        shopCategories.add(ShopCategory.FoodsAndDrinks);

        Shop shop = ShopFactory.aShop().withCategories(shopCategories).build();

        assertTrue(shop.getShopCategories().contains(ShopCategory.FoodsAndDrinks));
        assertFalse(shop.getShopCategories().contains(ShopCategory.OfficeSupplies));

        shop.addShopCategory(ShopCategory.OfficeSupplies);
        assertTrue(shop.getShopCategories().contains(ShopCategory.OfficeSupplies));
    }

    @Test
    public void aShopCanRemoveACategoryFromHisList(){
        ArrayList<ShopCategory> shopCategories = new ArrayList<>();
        shopCategories.add(ShopCategory.FoodsAndDrinks);
        shopCategories.add(ShopCategory.OfficeSupplies);

        Shop shop = ShopFactory.aShop().withCategories(shopCategories).build();

        assertTrue(shop.getShopCategories().contains(ShopCategory.FoodsAndDrinks));
        assertTrue(shop.getShopCategories().contains(ShopCategory.OfficeSupplies));

        shop.removeShopCategory(ShopCategory.OfficeSupplies);
        assertFalse(shop.getShopCategories().contains(ShopCategory.OfficeSupplies));
    }

    @Test
    public void aShopHasADomicile(){
        String domicile = "Test 123";

        Shop shop = ShopFactory.aShop().withDomicile(domicile).build();

        assertEquals(shop.getDomicile(), "Test 123");
    }

    @Test
    public void aShopCanChangeHisDomicile(){
        String domicile = "Test 123";

        Shop shop = ShopFactory.aShop().withDomicile(domicile).build();

        assertEquals("Test 123", shop.getDomicile());

        shop.setDomicile("False 123");

        assertEquals("False 123", shop.getDomicile());
    }

    @Test
    public void aShopHasOpeningDays(){
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);

        Shop shop = ShopFactory.aShop().withDays(days).build();

        assertTrue(shop.getDays().contains(DayOfWeek.MONDAY));
        assertTrue(shop.getDays().contains(DayOfWeek.TUESDAY));
        assertTrue(shop.getDays().contains(DayOfWeek.WEDNESDAY));
        assertFalse(shop.getDays().contains(DayOfWeek.THURSDAY));
        assertFalse(shop.getDays().contains(DayOfWeek.FRIDAY));
        assertFalse(shop.getDays().contains(DayOfWeek.SATURDAY));
        assertFalse(shop.getDays().contains(DayOfWeek.SUNDAY));
    }

    @Test
    public void aShopCanRemoveAnOpeningDay(){
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);

        Shop shop = ShopFactory.aShop().withDays(days).build();

        assertTrue(shop.getDays().contains(DayOfWeek.MONDAY));
        assertTrue(shop.getDays().contains(DayOfWeek.TUESDAY));

        shop.removeDay(DayOfWeek.TUESDAY);

        assertFalse(shop.getDays().contains(DayOfWeek.TUESDAY));
    }

    @Test
    public void aShopCanAddAnOpeningDay(){
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.MONDAY);

        Shop shop = ShopFactory.aShop().withDays(days).build();

        assertTrue(shop.getDays().contains(DayOfWeek.MONDAY));
        assertFalse(shop.getDays().contains(DayOfWeek.TUESDAY));

        shop.addDay(DayOfWeek.TUESDAY);

        assertTrue(shop.getDays().contains(DayOfWeek.TUESDAY));
    }

    @Test
    public void aShopCannotAddAnOpeningDayThatItAlreadyHas(){
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);

        Shop shop = ShopFactory.aShop().withDays(days).build();

        assertTrue(shop.getDays().contains(DayOfWeek.MONDAY));
        assertTrue(shop.getDays().contains(DayOfWeek.TUESDAY));

        Throwable exception = assertThrows(DayAlreadyExistsException.class, ()-> shop.addDay(DayOfWeek.TUESDAY));

        assertEquals("Day "+DayOfWeek.TUESDAY.name()+" is already present",exception.getMessage());
        assertEquals(shop.getDays().size(), 2);
    }

    @Test
    public void aShopHasOpeningAndClosingHours(){
        LocalTime openingHour = LocalTime.of(8,0);
        LocalTime closingHour = LocalTime.of(16, 0);

        Shop shop = ShopFactory.aShop()
                .withOpeningHour(openingHour)
                .withClosingHour(closingHour)
                .build();

        assertEquals(shop.getOpeningHour(), LocalTime.of(8, 0));
        assertEquals(shop.getClosingHour(), LocalTime.of(16, 0));
    }

    @Test
    public void aShopCanChangeHisOpeningHour(){
        LocalTime openingHour = LocalTime.of(8,0);

        Shop shop = ShopFactory.aShop()
                .withOpeningHour(openingHour)
                .build();

        assertEquals(shop.getOpeningHour(), LocalTime.of(8, 0));

        shop.setOpeningHour(LocalTime.of(17, 0));

        assertEquals(shop.getOpeningHour(), LocalTime.of(17, 0));
    }

    @Test
    public void aShopCanChangeHisClosingHour(){
        LocalTime closingHour = LocalTime.of(16, 0);

        Shop shop = ShopFactory.aShop()
                .withClosingHour(closingHour)
                .build();

        assertEquals(shop.getClosingHour(), LocalTime.of(16, 0));

        shop.setClosingHour(LocalTime.of(5, 0));

        assertEquals(shop.getClosingHour(), LocalTime.of(5, 0));
    }

    @Test
    public void aShopHasPaymentMethods(){
        ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(PaymentMethod.CASH);
        paymentMethods.add(PaymentMethod.DEBIT);

        Shop shop = ShopFactory.aShop()
                .withPaymentMethods(paymentMethods)
                .build();

        assertTrue(shop.getPaymentMethods().contains(PaymentMethod.CASH));
        assertTrue(shop.getPaymentMethods().contains(PaymentMethod.DEBIT));
        assertFalse(shop.getPaymentMethods().contains(PaymentMethod.MERCADOPAGO));
        assertFalse(shop.getPaymentMethods().contains(PaymentMethod.CREDIT));
    }

    @Test
    public void aShopCanAddNewPaymentMethods(){
        ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(PaymentMethod.CASH);

        Shop shop = ShopFactory.aShop()
                .withPaymentMethods(paymentMethods)
                .build();

        assertTrue(shop.getPaymentMethods().contains(PaymentMethod.CASH));
        assertFalse(shop.getPaymentMethods().contains(PaymentMethod.DEBIT));

        shop.addPaymentMethod(PaymentMethod.DEBIT);

        assertTrue(shop.getPaymentMethods().contains(PaymentMethod.DEBIT));
    }

    @Test
    public void ashopCanRemoveAPaymentMethod(){
        ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(PaymentMethod.CASH);
        paymentMethods.add(PaymentMethod.DEBIT);

        Shop shop = ShopFactory.aShop()
                .withPaymentMethods(paymentMethods)
                .build();

        assertTrue(shop.getPaymentMethods().contains(PaymentMethod.CASH));
        assertTrue(shop.getPaymentMethods().contains(PaymentMethod.DEBIT));

        shop.removePaymentMethod(PaymentMethod.DEBIT);

        assertFalse(shop.getPaymentMethods().contains(PaymentMethod.DEBIT));
    }

    @Test
    public void aShopCannotAddAPaymentMethodThatItAlreadyHas(){
        ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(PaymentMethod.CASH);
        paymentMethods.add(PaymentMethod.DEBIT);

        Shop shop = ShopFactory.aShop()
                .withPaymentMethods(paymentMethods)
                .build();

        assertTrue(shop.getPaymentMethods().contains(PaymentMethod.CASH));
        assertTrue(shop.getPaymentMethods().contains(PaymentMethod.DEBIT));

        Throwable exception = assertThrows(PaymentMethodAlreadyExistsException.class,
                ()-> shop.addPaymentMethod(PaymentMethod.DEBIT));

        assertEquals("Payment method "+ PaymentMethod.DEBIT.name()+" already exists",
                exception.getMessage());
        assertEquals(2, shop.getPaymentMethods().size());
    }

    @Test
    public void aShopHasADeliveryRadius(){
        Integer distanceInKM = 2;

        Shop shop = ShopFactory.aShop()
                .withDeliveryRadius(distanceInKM)
                .build();

        assertEquals(2, shop.getDeliveryRadius());
    }

    @Test
    public void aShopCanChangeHisDeliveryRadius(){
        Integer distanceInKM = 2;

        Shop shop = ShopFactory.aShop()
                .withDeliveryRadius(distanceInKM)
                .build();

        assertEquals(2, shop.getDeliveryRadius());

        shop.setDeliveryRadius(3);

        assertEquals(3, shop.getDeliveryRadius());
    }

    @Test
    public void aShopHasAManagerWithWichToValidate(){
        Manager manager = mock(Manager.class);
        Manager anotherManager = mock(Manager.class);

        doThrow(InvalidManagerException.class).when(manager).validate(anotherManager);

        Shop shop = ShopFactory.aShop()
                .withManager(manager)
                .build();

        assertDoesNotThrow(()-> shop.validateManager(manager));
        assertThrows(InvalidManagerException.class, ()-> shop.validateManager(anotherManager));
    }

    @Test
    public void aShopHasAListOfProducts(){
        Product prodMock1 = mock(Product.class);
        Product prodMock2 = mock(Product.class);

        ArrayList<Product> products = new ArrayList<>();
        products.add(prodMock1);
        products.add(prodMock2);

        Shop shop = ShopFactory.aShop().withProducts(products).build();

        assertTrue(shop.getProducts().contains(prodMock1));
        assertTrue(shop.getProducts().contains(prodMock2));
    }

    @Test
    public void aShopCanAddANewProduct(){
        Product prodMock1 = mock(Product.class);
        Product prodMock2 = mock(Product.class);

        when(prodMock1.getID()).thenReturn(1L);
        when(prodMock2.getID()).thenReturn(2L);

        ArrayList<Product> products = new ArrayList<>();
        products.add(prodMock1);

        Shop shop = ShopFactory.aShop().withProducts(products).build();

        assertTrue(shop.getProducts().contains(prodMock1));
        assertFalse(shop.getProducts().contains(prodMock2));

        shop.addProduct(prodMock2);

        assertTrue(shop.getProducts().contains(prodMock2));
    }

    @Test
    public void aShopCanRemoveAProduct(){
        Product prodMock1 = mock(Product.class);
        Product prodMock2 = mock(Product.class);

        when(prodMock1.getID()).thenReturn(1L);
        when(prodMock2.getID()).thenReturn(2L);

        ArrayList<Product> products = new ArrayList<>();
        products.add(prodMock1);
        products.add(prodMock2);

        Shop shop = ShopFactory.aShop().withProducts(products).build();

        assertTrue(shop.getProducts().contains(prodMock1));
        assertTrue(shop.getProducts().contains(prodMock2));

        shop.removeProduct(prodMock2);

        assertFalse(shop.getProducts().contains(prodMock2));
    }

    @Test
    public void aShopCannotAddTheSameProductTwice(){
        Product prodMock = mock(Product.class);
        when(prodMock.getID()).thenReturn(1L);

        ArrayList<Product> products = new ArrayList<>();
        products.add(prodMock);

        Shop shop = ShopFactory.aShop().withProducts(products).build();

        assertTrue(shop.getProducts().contains(prodMock));

        Throwable exception = assertThrows(ProductAlreadyPresentException.class, ()->shop.addProduct(prodMock));
        assertEquals("The product "+1L+" is already present in the list", exception.getMessage());
        assertEquals(1, shop.getProducts().size());
    }
}

class ShopFactory {
    private ArrayList<ShopCategory> shopCategories;
    private String domicile;
    private ArrayList<DayOfWeek> days;
    private LocalTime openingHour;
    private LocalTime closingHour;
    private ArrayList<PaymentMethod> paymentMethods;
    private Integer deliveryRadius;
    private Manager manager;
    private ArrayList<Product> products;

    public static ShopFactory aShop(){
        return new ShopFactory();
    }

    public ShopFactory(){
        this.shopCategories = new ArrayList<>();
        this.domicile = "";
        this.days = new ArrayList<>();
        this.openingHour = LocalTime.of(8,0);
        this.closingHour = LocalTime.of(17,0);
        this.paymentMethods = new ArrayList<>();
        this.deliveryRadius = 1;
        this.manager = new Manager();
        this.products = new ArrayList<>();
    }

    public ShopFactory withCategories(ArrayList<ShopCategory> shopCategories){
        this.shopCategories = shopCategories;
        return this;
    }
    public ShopFactory withDomicile(String domicile){
        this.domicile = domicile;
        return this;
    }
    public ShopFactory withDays(ArrayList<DayOfWeek> days){
        this.days = days;
        return this;
    }
    public ShopFactory withOpeningHour(LocalTime openingHour){
        this.openingHour = openingHour;
        return this;
    }
    public ShopFactory withClosingHour(LocalTime closingHour){
        this.closingHour = closingHour;
        return this;
    }
    public ShopFactory withPaymentMethods(ArrayList<PaymentMethod> paymentMethods){
        this.paymentMethods = paymentMethods;
        return this;
    }
    public ShopFactory withDeliveryRadius(Integer deliveryRadius) {
        this.deliveryRadius = deliveryRadius;
        return this;
    }
    public ShopFactory withManager(Manager manager) {
        this.manager = manager;
        return this;
    }
    public ShopFactory withProducts(ArrayList<Product> products) {
        this.products = products;
        return this;
    }

    public Shop build(){
        return new Shop(shopCategories,
                domicile,
                days,
                openingHour,
                closingHour,
                paymentMethods,
                deliveryRadius,
                manager,
                products);
    }
}
