package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.DayAlreadyExistsException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.DiscountAlreadyExistsException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidManagerException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.PaymentMethodAlreadyExistsException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.ProductAlreadyPresentException;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doThrow;

public class ShopTests {

    @Test
    public void aShopHasAName(){
        String name = "foo";

        Shop shop = ShopBuilder.anyShop()
                .withName(name)
                .build();

        assertEquals(name, shop.getName());
    }

    @Test
    public void aShopHasTheFoodsAndDrinksAndOfficeSuppliesCategories(){
        ArrayList<ShopCategory> shopCategories = new ArrayList<>();
        shopCategories.add(ShopCategory.FoodsAndDrinks);
        shopCategories.add(ShopCategory.OfficeSupplies);

        Shop shop = ShopBuilder.anyShop().withCategories(shopCategories).build();

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

        Shop shop = ShopBuilder.anyShop().withCategories(shopCategories).build();

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

        Shop shop = ShopBuilder.anyShop().withCategories(shopCategories).build();

        assertTrue(shop.getShopCategories().contains(ShopCategory.FoodsAndDrinks));
        assertTrue(shop.getShopCategories().contains(ShopCategory.OfficeSupplies));

        shop.removeShopCategory(ShopCategory.OfficeSupplies);
        assertFalse(shop.getShopCategories().contains(ShopCategory.OfficeSupplies));
    }

    @Test
    public void aShopHasALocation(){
        Location location = mock(Location.class);

        Shop shop = ShopBuilder.anyShop().withLocation(location).build();

        assertEquals(location, shop.getLocation());
    }

    @Test
    public void aShopCanChangeHisLocation(){
        Location location = mock(Location.class);
        Location newLocation = mock(Location.class);

        Shop shop = ShopBuilder.anyShop().withLocation(location).build();

        assertEquals(location, shop.getLocation());
        assertNotEquals(newLocation, shop.getLocation());

        shop.setLocation(newLocation);

        assertEquals(newLocation, shop.getLocation());
        assertNotEquals(location, shop.getLocation());
    }

    @Test
    public void aShopHasOpeningDays(){
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);

        Shop shop = ShopBuilder.anyShop().withDays(days).build();

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

        Shop shop = ShopBuilder.anyShop().withDays(days).build();

        assertTrue(shop.getDays().contains(DayOfWeek.MONDAY));
        assertTrue(shop.getDays().contains(DayOfWeek.TUESDAY));

        shop.removeDay(DayOfWeek.TUESDAY);

        assertFalse(shop.getDays().contains(DayOfWeek.TUESDAY));
    }

    @Test
    public void aShopCanAddAnOpeningDay(){
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.MONDAY);

        Shop shop = ShopBuilder.anyShop().withDays(days).build();

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

        Shop shop = ShopBuilder.anyShop().withDays(days).build();

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

        Shop shop = ShopBuilder.anyShop()
                .withOpeningHour(openingHour)
                .withClosingHour(closingHour)
                .build();

        assertEquals(shop.getOpeningHour(), LocalTime.of(8, 0));
        assertEquals(shop.getClosingHour(), LocalTime.of(16, 0));
    }

    @Test
    public void aShopCanChangeHisOpeningHour(){
        LocalTime openingHour = LocalTime.of(8,0);

        Shop shop = ShopBuilder.anyShop()
                .withOpeningHour(openingHour)
                .build();

        assertEquals(shop.getOpeningHour(), LocalTime.of(8, 0));

        shop.setOpeningHour(LocalTime.of(17, 0));

        assertEquals(shop.getOpeningHour(), LocalTime.of(17, 0));
    }

    @Test
    public void aShopCanChangeHisClosingHour(){
        LocalTime closingHour = LocalTime.of(16, 0);

        Shop shop = ShopBuilder.anyShop()
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

        Shop shop = ShopBuilder.anyShop()
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

        Shop shop = ShopBuilder.anyShop()
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

        Shop shop = ShopBuilder.anyShop()
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

        Shop shop = ShopBuilder.anyShop()
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

        Shop shop = ShopBuilder.anyShop()
                .withDeliveryRadius(distanceInKM)
                .build();

        assertEquals(2, shop.getDeliveryRadius());
    }

    @Test
    public void aShopCanChangeHisDeliveryRadius(){
        Integer distanceInKM = 2;

        Shop shop = ShopBuilder.anyShop()
                .withDeliveryRadius(distanceInKM)
                .build();

        assertEquals(2, shop.getDeliveryRadius());

        shop.setDeliveryRadius(3);

        assertEquals(3, shop.getDeliveryRadius());
    }

    @Test
    public void aShopHasAManagerWithWichToValidate() throws Exception {
        Manager manager = mock(Manager.class);
        Manager anotherManager = mock(Manager.class);

        doThrow(InvalidManagerException.class).when(manager).validate(anotherManager);

        Shop shop = ShopBuilder.anyShop()
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

        Shop shop = ShopBuilder.anyShop().withProducts(products).build();

        assertTrue(shop.getProducts().contains(prodMock1));
        assertTrue(shop.getProducts().contains(prodMock2));
    }

    @Test
    public void aShopCanAddANewProduct(){
        Product prodMock1 = mock(Product.class);
        Product prodMock2 = mock(Product.class);

        when(prodMock1.getId()).thenReturn(1L);
        when(prodMock2.getId()).thenReturn(2L);

        ArrayList<Product> products = new ArrayList<>();
        products.add(prodMock1);

        Shop shop = ShopBuilder.anyShop().withProducts(products).build();

        assertTrue(shop.getProducts().contains(prodMock1));
        assertFalse(shop.getProducts().contains(prodMock2));

        shop.addProduct(prodMock2);

        assertTrue(shop.getProducts().contains(prodMock2));
    }

    @Test
    public void aShopCanRemoveAProduct(){
        Product prodMock1 = mock(Product.class);
        Product prodMock2 = mock(Product.class);

        when(prodMock1.getId()).thenReturn(1L);
        when(prodMock2.getId()).thenReturn(2L);

        ArrayList<Product> products = new ArrayList<>();
        products.add(prodMock1);
        products.add(prodMock2);

        Shop shop = ShopBuilder.anyShop().withProducts(products).build();

        assertTrue(shop.getProducts().contains(prodMock1));
        assertTrue(shop.getProducts().contains(prodMock2));

        shop.removeProduct(prodMock2);

        assertFalse(shop.getProducts().contains(prodMock2));
    }

    @Test
    public void aShopCannotAddTheSameProductTwice(){
        Product prodMock = mock(Product.class);
        when(prodMock.getId()).thenReturn(1L);

        ArrayList<Product> products = new ArrayList<>();
        products.add(prodMock);

        Shop shop = ShopBuilder.anyShop().withProducts(products).build();

        assertTrue(shop.getProducts().contains(prodMock));

        Throwable exception = assertThrows(ProductAlreadyPresentException.class, ()->shop.addProduct(prodMock));
        assertEquals("The product "+1L+" is already present in the list", exception.getMessage());
        assertEquals(1, shop.getProducts().size());
    }

    @Test
    public void aShopCanHaveDiscounts(){
        Shop shop = ShopBuilder.anyShop().build();

        assertEquals(0, shop.getDiscounts().size());
    }

    @Test
    public void aShopCanAddDiscounts(){
        Shop shop = ShopBuilder.anyShop().build();

        assertEquals(0, shop.getDiscounts().size());

        Discount discount = mock(Discount.class);
        when(discount.getID()).thenReturn(1L);

        shop.addDiscount(discount);

        assertEquals(1, shop.getDiscounts().size());
        assertEquals(discount, shop.getDiscounts().get(0));
    }

    @Test
    public void aShopCannotAddTheSameDiscountTwice(){
        Shop shop = ShopBuilder.anyShop().build();

        Discount discount = mock(Discount.class);
        when(discount.getID()).thenReturn(1L);

        shop.addDiscount(discount);

        Throwable exception = assertThrows(DiscountAlreadyExistsException.class, ()->shop.addDiscount(discount));
        assertEquals("The given discount "+discount.getID()+" already exists", exception.getMessage());
    }

    @Test
    public void aShopCanRemoveDiscounts(){
        Shop shop = ShopBuilder.anyShop().build();

        Discount discount = mock(Discount.class);
        when(discount.getID()).thenReturn(1L);
        shop.addDiscount(discount);

        assertEquals(discount, shop.getDiscounts().get(0));

        shop.removeDiscount(discount);

        assertEquals(0, shop.getDiscounts().size());
    }

    @Test
    public void aShopCanReturnActiveDiscounts(){
        Shop shop = ShopBuilder.anyShop().build();

        Discount activeDiscount = mock(Discount.class);
        when(activeDiscount.getID()).thenReturn(1L);
        when(activeDiscount.isActive()).thenReturn(true);

        shop.addDiscount(activeDiscount);

        Discount inactiveDiscount = mock(Discount.class);
        when(inactiveDiscount.getID()).thenReturn(2L);
        when(inactiveDiscount.isActive()).thenReturn(false);

        shop.addDiscount(inactiveDiscount);

        assertEquals(activeDiscount, shop.getActiveDiscounts().get(0));
    }

    @Test
    public void aShopCanReceiveADeliveryAtHomeRequest(){
        Shop shop = ShopBuilder.anyShop().build();

        DeliveryAtHome delivery = mock(DeliveryAtHome.class);

        shop.addDelivery(delivery);

        assertTrue(shop.getActiveDeliveries().contains(delivery));
    }

    @Test
    public void aShopCanReceiveADeliveryAtShopRequestAndKnowsWichTurnsItTakes(){
        Shop shop = ShopBuilder.anyShop().build();

        DeliveryAtShop delivery = mock(DeliveryAtShop.class);
        Turn turn = mock(Turn.class);

        when(delivery.getTurn()).thenReturn(turn);

        shop.addDelivery(delivery);

        assertTrue(shop.getActiveDeliveries().contains(delivery));

        assertTrue(shop.getActiveTurns().contains(turn));
    }
}
