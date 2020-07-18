package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.ProductIsInvalidForDiscount;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingList;
import com.unq.dapp0.c1.comprandoencasa.services.DeliveryService;
import com.unq.dapp0.c1.comprandoencasa.services.UserService;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.*;
import com.unq.dapp0.c1.comprandoencasa.services.security.UserPrincipal;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.*;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.*;
import com.unq.dapp0.c1.comprandoencasa.webservices.security.CurrentUser;
import com.unq.dapp0.c1.comprandoencasa.webservices.security.user.BadRequestException;
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

    @Autowired
    private DeliveryService deliveryService;

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
    public ResponseEntity<ShoppingListActiveDTO> createNewCart(@CurrentUser UserPrincipal userPrincipal,
                                                         @RequestParam(value = "locationId") String locationId){
        try{
            ShoppingListActiveDTO shoppingList = userService.createShoppingList(userPrincipal.getId(), Long.valueOf(locationId));
            return new ResponseEntity<>(shoppingList, HttpStatus.CREATED);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (ProductIsInvalidForDiscount exception){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage());
        } catch (LocationDoesNotExistException exception){
            throw new LocationNotFoundException(exception.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/product")
    public ResponseEntity<ShoppingListActiveDTO> putUpdateProductInCart(@CurrentUser UserPrincipal userPrincipal,
                                                                  @RequestBody ShoppingListEntryAddDTO listEntryAddDTO){
        try {
            ShoppingListActiveDTO shoppingList = userService
                    .putUpdateProductInCart(userPrincipal.getId(),listEntryAddDTO.productId, listEntryAddDTO.amount);
            return new ResponseEntity<>(shoppingList, HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (NotAnActiveShoppingListException exception){
            throw new BadRequestException(exception.getMessage());
        } catch (ProductDoesntExistException exception){
            throw new ProductNotFoundException(exception.getMessage());
        } catch (ProductIsInvalidForDiscount exception){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage());
        }
    }

    @CrossOrigin
    @DeleteMapping("/product")
    public ResponseEntity<ShoppingListActiveDTO> deleteProductInCart(@CurrentUser UserPrincipal userPrincipal,
                                                               @RequestParam(value = "productId") String productId){
        try {
            ShoppingListActiveDTO shoppingList = userService
                    .deleteProductInCart(userPrincipal.getId(), Long.valueOf(productId));
            return new ResponseEntity<>(shoppingList, HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (NotAnActiveShoppingListException exception){
            throw new BadRequestException(exception.getMessage());
        } catch (ProductDoesntExistException exception){
            throw new ProductNotFoundException(exception.getMessage());
        } catch (ProductIsInvalidForDiscount exception){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/purchase")
    public ResponseEntity<List<ShopDeliveryDTO>> doPurchase(@CurrentUser UserPrincipal userPrincipal,
                                                            @RequestBody CartPurchaseDTO purchaseDTO){
        try {
            List<ShopDeliveryDTO> list = this.deliveryService.doPurchase(userPrincipal.getId(), purchaseDTO.deliveries);
            return new ResponseEntity<>(list, HttpStatus.CREATED);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (NotAnActiveShoppingListException | MissingFieldsForDeliveryCreationException exception){
            throw new BadRequestException(exception.getMessage());
        } catch (ProductDoesntExistException exception){
            throw new ProductNotFoundException(exception.getMessage());
        } catch (ShopDoesntExistException exception){
            throw new ShopNotFoundException(exception.getMessage());
        }
    }

}
