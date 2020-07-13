package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.objects.AuthProvider;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    public List<ShopSmallDTO> shops;
    public String name;
    public String email;
    public String imgUrl;
    public AuthProvider provider;
    public List<Location> locations;
    public BigDecimal totalThreshold;
    public ShoppingListDTO shoppingList;

    public UserDTO(){}

    public UserDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.imgUrl = user.getImageUrl();
        this.provider = user.getProvider();
        this.locations = user.getLocations();
        this.totalThreshold = user.getTotalThreshold();
        this.shops = parseShops(user.getShops());
        this.shoppingList = new ShoppingListDTO(user.getActiveShoppingList());
    }

    private List<ShopSmallDTO> parseShops(List<Shop> shops) {
        List<ShopSmallDTO> returnList = new ArrayList<>();
        for (Shop shop : shops){
            returnList.add(new ShopSmallDTO(shop));
        }
        return returnList;
    }
}
