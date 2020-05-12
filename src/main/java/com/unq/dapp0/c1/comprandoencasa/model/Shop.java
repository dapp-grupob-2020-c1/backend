package com.unq.dapp0.c1.comprandoencasa.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents the internal administrative data of a shop and it's mecanics.
 */
public class Shop {

    private ArrayList<ShopCategory> shopCategories;
    private Location location;
    private ArrayList<DayOfWeek> days;
    private LocalTime openingHour;
    private LocalTime closingHour;
    private ArrayList<PaymentMethod> paymentMethods;
    private Integer deliveryRadius;
    private final Manager manager;
    private ArrayList<Product> products;
    private ArrayList<Discount> discounts;
    private ArrayList<ShopDelivery> activeDeliveries;

    public Shop(ArrayList<ShopCategory> shopCategories, Location location, ArrayList<DayOfWeek> days, LocalTime openingHour, LocalTime closingHour, ArrayList<PaymentMethod> paymentMethods, Integer deliveryRadius, Manager manager, ArrayList<Product> products) {
        this.shopCategories = shopCategories;
        this.location = location;
        this.days = days;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.paymentMethods = paymentMethods;
        this.deliveryRadius = deliveryRadius;
        this.manager = manager;
        this.products = products;
        this.discounts = new ArrayList<>();
        this.activeDeliveries = new ArrayList<>();
    }

    public ArrayList<ShopCategory> getShopCategories() {
        return this.shopCategories;
    }

    public void addShopCategory(ShopCategory shopCategory) {
        this.shopCategories.add(shopCategory);
    }

    public void removeShopCategory(ShopCategory shopCategory) {
        this.shopCategories.remove(shopCategory);
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location newLocation) {
        this.location = newLocation;
    }

    public ArrayList<DayOfWeek> getDays() {
        return this.days;
    }

    public LocalTime getOpeningHour() {
        return this.openingHour;
    }

    public LocalTime getClosingHour() {
        return this.closingHour;
    }

    public void removeDay(DayOfWeek dia) {
        this.days.remove(dia);
    }

    /**
     * @throws DayAlreadyExistsException if the day is already present.
     */
    public void addDay(DayOfWeek day) {
        if (this.days.contains(day)){
            throw new DayAlreadyExistsException(day);
        }
        this.days.add(day);
    }

    public void setOpeningHour(LocalTime newHour) {
        this.openingHour = newHour;
    }

    public void setClosingHour(LocalTime newHour) {
        this.closingHour = newHour;
    }

    public ArrayList<PaymentMethod> getPaymentMethods() {
        return this.paymentMethods;
    }

    /**
     * @throws PaymentMethodAlreadyExistsException if the payment method is already present.
     */
    public void addPaymentMethod(PaymentMethod paymentMethod) {
        if (this.paymentMethods.contains(paymentMethod)){
            throw new PaymentMethodAlreadyExistsException(paymentMethod);
        }
        this.paymentMethods.add(paymentMethod);
    }

    public void removePaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethods.remove(paymentMethod);
    }

    /**
     * @return radius in km as an Integer.
     */
    public Integer getDeliveryRadius() {
        return this.deliveryRadius;
    }

    /**
     * @param distanceInKM as an Integer.
     */
    public void setDeliveryRadius(Integer distanceInKM) {
        this.deliveryRadius = distanceInKM;
    }

    /**
     * Validates that the given manager is the same as the manager in charge of the Shop.
     * Delegates the verification to his own manager.
     * @param manager to validate
     */
    public void validateManager(Manager manager) throws Exception {
        this.manager.validate(manager);
    }

    public ArrayList<Product> getProducts() {
        return this.products;
    }

    /**
     * @throws ProductAlreadyPresentException if the product is already present.
     */
    public void addProduct(Product product) {
        if (!this.findProduct(product).isPresent()){
            this.products.add(product);
        } else {
            throw new ProductAlreadyPresentException(product);
        }
    }

    public void removeProduct(Product product) {
        this.findProduct(product).ifPresent(value -> this.products.remove(value));
    }

    private Optional<Product> findProduct(Product product){
        return this.products.stream().filter(p-> p.getID().equals(product.getID())).findFirst();
    }

    public ArrayList<Discount> getDiscounts() {
        return this.discounts;
    }

    /**
     * @throws DiscountAlreadyExistsException if the discount is already present.
     */
    public void addDiscount(Discount discount) {
        if (!this.findDiscount(discount).isPresent()){
            this.discounts.add(discount);
        } else {
            throw new DiscountAlreadyExistsException(discount);
        }
    }

    public void removeDiscount(Discount discount) {
        this.findDiscount(discount).ifPresent(value -> this.discounts.remove(value));
    }

    private Optional<Discount> findDiscount(Discount discount) {
        return this.discounts.stream().filter(d-> d.getID().equals(discount.getID())).findFirst();
    }

    public ArrayList<Discount> getActiveDiscounts() {
        return this.discounts.stream().filter(Discount::isActive).collect(Collectors.toCollection(ArrayList::new));
    }

    public void addDelivery(ShopDelivery delivery) {
        this.activeDeliveries.add(delivery);
    }

    public ArrayList<ShopDelivery> getActiveDeliveries() {
        return this.activeDeliveries;
    }

    public ArrayList<Turn> getActiveTurns() {
        ArrayList<ShopDelivery> deliveries = this.activeDeliveries.stream()
                .filter(delivery -> delivery instanceof DeliveryAtShop).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Turn> turns = new ArrayList<>();
        for (ShopDelivery delivery : deliveries){
            turns.add(((DeliveryAtShop) delivery).getTurn());
        }
        return turns;
    }
}
