package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import com.unq.dapp0.c1.comprandoencasa.services.ShopService;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.UserDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.security.UserPrincipal;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopCreationDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopFullDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopModificationDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopSmallDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.UserNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class ShopController {

    @Autowired
    private ShopService shopService;

    @CrossOrigin
    @GetMapping("/shop")
    public ShopDTO getShop(@RequestParam(value = "shopId") String shopId) {
        try{
            Shop shop = this.shopService.findShopById(Long.valueOf(shopId));
            return new ShopDTO(shop);
        } catch (ShopDoesntExistException exception){
            throw new ShopNotFoundException(shopId);
        }
    }

    @CrossOrigin
    @PostMapping("/shop")
    public ResponseEntity<ShopFullDTO> createShop(@CurrentUser UserPrincipal userPrincipal,
                                                 @RequestBody ShopCreationDTO shopData){
        try {
            Shop shop = this.shopService.createShop(userPrincipal.getId(), shopData);
            return new ResponseEntity<>(new ShopFullDTO(shop), HttpStatus.CREATED);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        }
    }

    @CrossOrigin
    @PatchMapping("/shop")
    public ResponseEntity<ShopFullDTO> modifyShop(@CurrentUser UserPrincipal userPrincipal,
                                                  @RequestBody ShopModificationDTO shopData){
        try {
            Shop shop = this.shopService.modifyShop(userPrincipal.getId(), shopData);
            return new ResponseEntity<>(new ShopFullDTO(shop), HttpStatus.CREATED);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (ShopDoesntExistException exception){
            throw new ShopNotFoundException(String.valueOf(shopData.id));
        }
    }

    @CrossOrigin
    @DeleteMapping("/shop")
    public ResponseEntity<ShopSmallDTO> deleteShop(@CurrentUser UserPrincipal userPrincipal,
                                                   @RequestParam(value = "shopId") String shopId){
        try {
            Shop shop = this.shopService.deleteShop(userPrincipal.getId(), Long.parseLong(shopId));
            return new ResponseEntity<>(new ShopSmallDTO(shop), HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (ShopDoesntExistException exception){
            throw new ShopNotFoundException(shopId);
        }
    }
}
