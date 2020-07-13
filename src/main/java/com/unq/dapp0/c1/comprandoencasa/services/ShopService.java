package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Discount;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DiscountByCategory;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DiscountByMultiple;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DiscountBySingle;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Product;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShopDelivery;
import com.unq.dapp0.c1.comprandoencasa.model.objects.User;
import com.unq.dapp0.c1.comprandoencasa.repositories.DiscountRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.ShopRepository;

import com.unq.dapp0.c1.comprandoencasa.services.exceptions.DeliveryDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.DiscountArgumentsMismatchException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.DiscountDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.ProductDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.ShopHasActiveDeliveriesException;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.DiscountCreateDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.DiscountModifyDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopCreationDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopModificationDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopDoesntExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ShopService {

    @Autowired
    private ShopRepository repository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private DeliveryService deliveryService;

    @Transactional
    public Shop save(Shop model) {
        return this.repository.save(model);
    }

    @Transactional
    public Shop findShopById(Long id) {
        Optional<Shop> result = this.repository.findById(id);
        if (result.isPresent()){
            return result.get();
        } else {
            throw new ShopDoesntExistException(id);
        }
    }

    @Transactional
    public Shop createShop(Long userId, ShopCreationDTO shopData) {
        User user = this.userService.findUserById(userId);
        Location location = this.locationService.save(shopData.location);
        Shop shop = new Shop(
                shopData.name,
                shopData.categories,
                location,
                shopData.days,
                shopData.openingHour,
                shopData.closingHour,
                shopData.paymentMethods,
                shopData.deliveryRadius
        );
        this.repository.save(shop);
        shop.setUser(user);
        this.userService.save(user);
        this.repository.save(shop);
        return shop;
    }

    @Transactional
    public Shop modifyShop(Long userId, ShopModificationDTO shopData) {
        User user = this.userService.findUserById(userId);
        Shop shop = getShopFromUser(user, shopData.id);
        Location oldLocation = shop.getLocation();
        if (shopData.location.getId() != null && !oldLocation.getId().equals(shopData.location.getId())){
            this.locationService.save(shopData.location);
            shop.setLocation(shopData.location);
            this.locationService.delete(oldLocation);
        }
        shop.setName(shopData.name);
        shop.setShopCategories(shopData.categories);
        shop.setDays(shopData.days);
        shop.setOpeningHour(shopData.openingHour);
        shop.setClosingHour(shopData.closingHour);
        shop.setPaymentMethods(shopData.paymentMethods);
        shop.setDeliveryRadius(shopData.deliveryRadius);
        this.repository.save(shop);
        return shop;
    }

    @Transactional
    public Shop deleteShop(Long userId, long shopId) {
        User user = this.userService.findUserById(userId);
        Shop shop = getShopFromUser(user, shopId);
        checkNoDeliveriesPending(shop);
        deleteDiscountsFromShop(shop);
        deleteProductsFromShop(shop);
        user.removeShop(shop);
        this.userService.save(user);
        this.repository.delete(shop);
        return shop;
    }

    private void deleteProductsFromShop(Shop shop) {
        List<Product> products = shop.getProducts();
        for (Product product : products){
            this.productService.deleteProduct(shop, product);
        }
    }

    private void deleteDiscountsFromShop(Shop shop) {
        List<Discount> discounts = shop.getDiscounts();
        for (Discount discount : discounts){
            shop.removeDiscount(discount);
            this.discountRepository.delete(discount);
        }
    }

    private void checkNoDeliveriesPending(Shop shop) {
        List<ShopDelivery> deliveryList = shop.getActiveDeliveries();
        if (!deliveryList.isEmpty()){
            throw new ShopHasActiveDeliveriesException(shop);
        }
    }

    private Shop getShopFromUser(User user, long shopId) {
        Optional<Shop> result = user.getShops().stream().filter(sh -> sh.getId().equals(shopId)).findFirst();
        if (result.isPresent()){
            return result.get();
        } else {
            throw new ShopDoesntExistException(shopId);
        }
    }


    @Transactional
    public List<Discount> getDiscounts(Long userId, long shopId) {
        User user = this.userService.findUserById(userId);
        Shop shop = getShopFromUser(user, shopId);
        return shop.getDiscounts();
    }

    @Transactional
    public Discount createDiscount(Long userId, DiscountCreateDTO discountCreateDTO) {
        User user = this.userService.findUserById(userId);
        Shop shop = getShopFromUser(user, discountCreateDTO.shopId);
        Discount discount;
        if (discountCreateDTO.productId != null){
            Product product = this.getProductFromShop(shop, discountCreateDTO.productId);
            discount = new DiscountBySingle(
                    discountCreateDTO.percentage,
                    discountCreateDTO.startingDate,
                    discountCreateDTO.endingDate,
                    shop,
                    product
            );
        } else if (discountCreateDTO.productsIds != null){
            List<Product> products = this.getProductsFromShop(shop, discountCreateDTO.productsIds);
            discount = new DiscountByMultiple(
                    discountCreateDTO.percentage,
                    discountCreateDTO.startingDate,
                    discountCreateDTO.endingDate,
                    shop,
                    products
            );
        } else {
            discount = new DiscountByCategory(
                    discountCreateDTO.percentage,
                    discountCreateDTO.startingDate,
                    discountCreateDTO.endingDate,
                    shop,
                    discountCreateDTO.productType
            );
        }
        shop.addDiscount(discount);
        this.discountRepository.save(discount);
        this.save(shop);
        return discount;
    }

    private List<Product> getProductsFromShop(Shop shop, Collection<Long> productsIds) {
        List<Product> returnList = new ArrayList<>();
        for (Long productId : productsIds){
            returnList.add(this.getProductFromShop(shop, productId));
        }
        return returnList;
    }

    private Product getProductFromShop(Shop shop, Long productId) {
        Optional<Product> result = shop.getProducts().stream().filter(product -> product.getId().equals(productId)).findFirst();
        if (result.isPresent()){
            return result.get();
        } else {
            throw new ProductDoesntExistException(productId);
        }
    }

    @Transactional
    public Discount modifyDiscount(Long userId, DiscountModifyDTO discountModifyDTO) {
        User user = this.userService.findUserById(userId);
        Shop shop = getShopFromUser(user, discountModifyDTO.shopId);
        Discount discount = getDiscountFromShop(shop, discountModifyDTO.id);
        discount.setStartingDate(discountModifyDTO.startingDate);
        discount.setEndingDate(discountModifyDTO.endingDate);
        discount.setPercentage(discountModifyDTO.percentage);
        if (discount.isTypeSingle() && discountModifyDTO.productId != null){
            Product product = this.getProductFromShop(shop, discountModifyDTO.productId);
            ((DiscountBySingle) discount).setProduct(product);
        } else if (discount.isTypeMultiple() && discountModifyDTO.productsIds != null){
            List<Product> products = this.getProductsFromShop(shop, discountModifyDTO.productsIds);
            ((DiscountByMultiple) discount).setProducts(products);
        } else if (discount.isTypeCategory() && discountModifyDTO.productType != null){
            ((DiscountByCategory) discount).setProductType(discountModifyDTO.productType);
        } else {
            throw new DiscountArgumentsMismatchException(discount);
        }
        this.discountRepository.save(discount);
        return discount;
    }

    private Discount getDiscountFromShop(Shop shop, Long discountId) {
        Optional<Discount> result = shop.getDiscounts().stream().filter(discount -> discount.getId().equals(discountId)).findFirst();
        if (result.isPresent()){
            return result.get();
        } else {
            throw new DiscountDoesntExistException(discountId);
        }
    }

    @Transactional
    public Discount deleteDiscount(Long userId, Long shopId, Long discountId) {
        User user = this.userService.findUserById(userId);
        Shop shop = getShopFromUser(user, shopId);
        Discount discount = getDiscountFromShop(shop, discountId);
        shop.removeDiscount(discount);
        this.discountRepository.delete(discount);
        this.repository.save(shop);
        return discount;
    }

    @Transactional
    public List<ShopDelivery> getDeliveries(Long userId, Long shopId) {
        User user = this.userService.findUserById(userId);
        Shop shop = getShopFromUser(user, shopId);
        return shop.getActiveDeliveries();
    }

    @Transactional
    public ShopDelivery removeDelivery(Long userId, Long shopId, Long deliveryId) {
        User user = this.userService.findUserById(userId);
        Shop shop = getShopFromUser(user, shopId);
        Optional<ShopDelivery> result = shop.getActiveDeliveries().stream()
                .filter(shopDelivery -> shopDelivery.getId().equals(deliveryId)).findFirst();
        if (result.isPresent()){
            ShopDelivery delivery = result.get();
            shop.removeActiveDelivery(delivery);
            this.deliveryService.delete(delivery);
            this.save(shop);
            return delivery;
        } else {
            throw new DeliveryDoesntExistException(deliveryId);
        }
    }
}
