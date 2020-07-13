package com.unq.dapp0.c1.comprandoencasa.model.objects;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.DayAlreadyExistsException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.DiscountAlreadyExistsException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.PaymentMethodAlreadyExistsException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.ProductAlreadyPresentException;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents the internal administrative data of a shop and it's mecanics.
 */
@Entity
@Table
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ElementCollection
    private List<ShopCategory> shopCategories;

    @OneToOne
    private Location location;

    @ElementCollection
    private List<DayOfWeek> days;

    @Column
    private LocalTime openingHour;

    @Column
    private LocalTime closingHour;

    @ElementCollection
    private List<PaymentMethod> paymentMethods;

    @Column
    private Integer deliveryRadius;

    @OneToOne
    private User user;

    @OneToMany
    private List<Product> products;

    @OneToMany
    private List<Discount> discounts;

    @OneToMany
    private List<ShopDelivery> activeDeliveries;

    @OneToMany
    private List<ShopDelivery> historicDeliveries;

    public Shop() {
    }

    public Shop(String name,
                List<ShopCategory> shopCategories,
                Location location,
                List<DayOfWeek> days,
                LocalTime openingHour,
                LocalTime closingHour,
                List<PaymentMethod> paymentMethods,
                Integer deliveryRadius) {
        this.name = name;
        this.shopCategories = shopCategories;
        this.location = location;
        this.days = days;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.paymentMethods = paymentMethods;
        this.deliveryRadius = deliveryRadius;
        this.products = new ArrayList<>();
        this.discounts = new ArrayList<>();
        this.activeDeliveries = new ArrayList<>();
        this.historicDeliveries = new ArrayList<>();
    }

    public void setUser(User user){
        user.addShop(this);
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public List<ShopCategory> getShopCategories() {
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

    public List<DayOfWeek> getDays() {
        return this.days;
    }

    public LocalTime getOpeningHour() {
        return this.openingHour;
    }

    public void setOpeningHour(LocalTime newHour) {
        this.openingHour = newHour;
    }

    public LocalTime getClosingHour() {
        return this.closingHour;
    }

    public void setClosingHour(LocalTime newHour) {
        this.closingHour = newHour;
    }

    public void removeDay(DayOfWeek dia) {
        this.days.remove(dia);
    }

    /**
     * @throws DayAlreadyExistsException if the day is already present.
     */
    public void addDay(DayOfWeek day) {
        if (this.days.contains(day)) {
            throw new DayAlreadyExistsException(day);
        }
        this.days.add(day);
    }

    public List<PaymentMethod> getPaymentMethods() {
        return this.paymentMethods;
    }

    /**
     * @throws PaymentMethodAlreadyExistsException if the payment method is already present.
     */
    public void addPaymentMethod(PaymentMethod paymentMethod) {
        if (this.paymentMethods.contains(paymentMethod)) {
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
     *
     * @param user to validate
     */
    public void validateManager(User user) throws Exception {
        this.user.validate(user);
    }

    public List<Product> getProducts() {
        return this.products;
    }

    /**
     * @throws ProductAlreadyPresentException if the product is already present.
     */
    public void addProduct(Product product) {
        if (!this.findProduct(product).isPresent()) {
            this.products.add(product);
        } else {
            throw new ProductAlreadyPresentException(product);
        }
    }

    public void removeProduct(Product product) {
        this.findProduct(product).ifPresent(value -> this.products.remove(value));
    }

    private Optional<Product> findProduct(Product product) {
        return this.products.stream().filter(p -> p.getId().equals(product.getId())).findFirst();
    }

    public List<Discount> getDiscounts() {
        return this.discounts;
    }

    /**
     * @throws DiscountAlreadyExistsException if the discount is already present.
     */
    public void addDiscount(Discount discount) {
        if (!this.findDiscount(discount).isPresent()) {
            this.discounts.add(discount);
        } else {
            throw new DiscountAlreadyExistsException(discount);
        }
    }

    public void removeDiscount(Discount discount) {
        this.findDiscount(discount).ifPresent(value -> this.discounts.remove(value));
    }

    private Optional<Discount> findDiscount(Discount discount) {
        return this.discounts.stream().filter(d -> d.getId().equals(discount.getId())).findFirst();
    }

    public List<Discount> getActiveDiscounts() {
        return this.discounts.stream().filter(Discount::isActive).collect(Collectors.toCollection(ArrayList::new));
    }

    public void addDelivery(ShopDelivery delivery) {
        this.activeDeliveries.add(delivery);
    }

    public List<ShopDelivery> getActiveDeliveries() {
        return this.activeDeliveries;
    }

    public List<Turn> getActiveTurns() {
        ArrayList<ShopDelivery> deliveries = this.activeDeliveries.stream()
                .filter(delivery -> delivery instanceof DeliveryAtShop).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Turn> turns = new ArrayList<>();
        for (ShopDelivery delivery : deliveries) {
            turns.add(((DeliveryAtShop) delivery).getTurn());
        }
        return turns;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShopCategories(List<ShopCategory> categories) {
        this.shopCategories = categories;
    }

    public void setDays(List<DayOfWeek> days) {
        this.days = days;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public void removeActiveDelivery(ShopDelivery delivery) {
        this.activeDeliveries.remove(delivery);
    }

    public List<ShopDelivery> getHistoricDeliveries() {
        return this.historicDeliveries;
    }

    public void setHistoricDeliveries(List<ShopDelivery> deliveries){this.historicDeliveries = deliveries;}
}
