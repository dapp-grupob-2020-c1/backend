package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.User;
import com.unq.dapp0.c1.comprandoencasa.repositories.ShopRepository;

import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopCreationDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopModificationDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopDoesntExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ShopService {

    @Autowired
    private ShopRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

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
        Optional<Shop> result = getShopFromUser(user, shopData.id);
        if(result.isPresent()){
            Shop shop = result.get();
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
        } else {
            throw new ShopDoesntExistException(shopData.id);
        }
    }

    @Transactional
    public Shop deleteShop(Long userId, long shopId) {
        User user = this.userService.findUserById(userId);
        Optional<Shop> result = getShopFromUser(user, shopId);
        if(result.isPresent()){
            Shop shop = result.get();
            user.removeShop(shop);
            this.userService.save(user);
            this.repository.delete(shop);
            return shop;
        } else {
            throw new ShopDoesntExistException(shopId);
        }
    }

    private Optional<Shop> getShopFromUser(User user, long shopId) {
        return user.getShops().stream().filter(sh -> sh.getId().equals(shopId)).findFirst();
    }


}
