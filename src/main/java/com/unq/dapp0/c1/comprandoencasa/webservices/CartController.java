package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.ProductIsInvalidForDiscount;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingList;
import com.unq.dapp0.c1.comprandoencasa.services.UserService;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.LocationDoesNotExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.UserDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.security.UserPrincipal;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.*;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.LocationNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.UserNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private UserService userService;

    @CrossOrigin
    @GetMapping("/historic")
    public ResponseEntity<List<ShoppingListDTO>> getOldCarts(@CurrentUser UserPrincipal userPrincipal){
        try {
            List<ShoppingList> shoppingLists = this.userService.getHistoricCarts(userPrincipal.getId());
            return new ResponseEntity<>(ShoppingListDTO.parseList(shoppingLists), HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<ShoppingListActiveDTO> getMyActiveCart(@CurrentUser UserPrincipal userPrincipal){
        try {
            ShoppingListActiveDTO activeDTO = this.userService.getActiveCart(userPrincipal.getId());
            return new ResponseEntity<>(activeDTO, HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (ProductIsInvalidForDiscount exception){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage());
        }
    }


    @CrossOrigin
    @PostMapping
    public ResponseEntity<ShoppingListDTO> createNewCart(@CurrentUser UserPrincipal userPrincipal,
                                                         @RequestParam(value = "locationId") String locationId){
        try{
            ShoppingList shoppingList = userService.createShoppingList(userPrincipal.getId(), Long.valueOf(locationId));
            return new ResponseEntity<>(new ShoppingListDTO(shoppingList), HttpStatus.CREATED);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (ProductIsInvalidForDiscount exception){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage());
        } catch (LocationDoesNotExistException exception){
            throw new LocationNotFoundException(exception.getMessage());
        }
    }

    /*
    @CrossOrigin
    @PostMapping("/product")
    public ResponseEntity<ShoppingListDTO> putUpdateProductInCart(@CurrentUser UserPrincipal userPrincipal,
                                                                  @RequestBody ShoppingListEntryAddDTO listEntryAddDTO){

    }

    @CrossOrigin
    @DeleteMapping("/product")
    public ResponseEntity<ShoppingListDTO> deleteProductInCart(@CurrentUser UserPrincipal userPrincipal,
                                                               @RequestParam(value = "productId") String productId){

    }

    @CrossOrigin
    @PostMapping("/purchase")
    public ResponseEntity<List<ShopDeliveryDTO>> doPurchase(@CurrentUser UserPrincipal userPrincipal,
                                                            @RequestBody CartPurchaseDTO purchaseDTO){

    }*/

}
