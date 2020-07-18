package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.objects.AuthProvider;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ProductType;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingList;
import com.unq.dapp0.c1.comprandoencasa.model.objects.User;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDTO {
    public Map<ProductType, BigDecimal> typeThresholds;
    public Map<ProductType, BigDecimal> suggestedTypeThresholds;
    public List<ShopSmallDTO> shops;
    public String name;
    public String email;
    public String imgUrl;
    public AuthProvider provider;
    public List<Location> locations;
    public BigDecimal totalThreshold;
    @Nullable
    public ShoppingListDTO shoppingList;

    public UserDTO(){}

    public UserDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.imgUrl = user.getImageUrl();
        this.provider = user.getProvider();
        this.locations = user.getLocations();
        this.totalThreshold = user.getTotalThreshold();
        this.typeThresholds = user.getTypeThresholds();
        this.suggestedTypeThresholds = user.getSuggestedTypeThresholds();
        this.shops = parseShops(user.getShops());
        ShoppingList shoppingList = user.getActiveShoppingList();
        if (shoppingList.getId() != null){
            this.shoppingList = new ShoppingListDTO(shoppingList);
        }
    }

    private List<ShopSmallDTO> parseShops(List<Shop> shops) {
        List<ShopSmallDTO> returnList = new ArrayList<>();
        for (Shop shop : shops){
            returnList.add(new ShopSmallDTO(shop));
        }
        return returnList;
    }
}
