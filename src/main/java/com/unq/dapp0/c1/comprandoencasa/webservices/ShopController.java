package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Discount;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShopDelivery;
import com.unq.dapp0.c1.comprandoencasa.services.ShopService;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.DeliveryDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.DiscountArgumentsMismatchException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.DiscountDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.ProductDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.ShopHasActiveDeliveriesException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.UserDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.security.UserPrincipal;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.DiscountCreateDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.DiscountDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.DiscountModifyDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.DiscountNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopCreationDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopDeliveryDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopFullDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopModificationDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopSmallDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.DeliveryNotFound;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.DiscountArgumentBadRequest;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ProductNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.UserNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.security.CurrentUser;
import com.unq.dapp0.c1.comprandoencasa.webservices.security.user.BadRequestException;
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

import java.util.List;

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
            return new ResponseEntity<>(new ShopFullDTO(shop), HttpStatus.OK);
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
        } catch (ShopHasActiveDeliveriesException exception){
            throw new BadRequestException(exception.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/shop/discounts")
    public ResponseEntity<List<DiscountDTO>> getDiscounts(@CurrentUser UserPrincipal userPrincipal,
                                                    @RequestParam(value = "shopId") String shopId){
        try {
            List<Discount> discounts = this.shopService.getDiscounts(userPrincipal.getId(), Long.parseLong(shopId));
            return new ResponseEntity<>(DiscountDTO.createDiscounts(discounts), HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (ShopDoesntExistException exception){
            throw new ShopNotFoundException(shopId);
        }
    }

    @CrossOrigin
    @PostMapping("/shop/discount")
    public ResponseEntity<DiscountDTO> createDiscount(@CurrentUser UserPrincipal userPrincipal,
                                                      @RequestBody DiscountCreateDTO discountCreateDTO){
        try {
            if (discountCreateDTO.productId == null && discountCreateDTO.productsIds == null
                    && discountCreateDTO.productType == null){
                throw new BadRequestException("Request body should have at least one of the following fields: productId, productsIds or productType");
            }
            Discount discount = this.shopService.createDiscount(userPrincipal.getId(), discountCreateDTO);
            return new ResponseEntity<>(new DiscountDTO(discount), HttpStatus.CREATED);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (ShopDoesntExistException exception){
            throw new ShopNotFoundException(String.valueOf(discountCreateDTO.shopId));
        } catch (ProductDoesntExistException exception){
            throw new ProductNotFoundException(exception.getMessage());
        }
    }

    @CrossOrigin
    @PatchMapping("/shop/discount")
    public ResponseEntity<DiscountDTO> modifyDiscount(@CurrentUser UserPrincipal userPrincipal,
                                                      @RequestBody DiscountModifyDTO discountModifyDTO){
        try {
            if (discountModifyDTO.productId == null && discountModifyDTO.productsIds == null
                    && discountModifyDTO.productType == null){
                throw new BadRequestException("Request body should have at least one of the following fields: productId, productsIds or productType");
            }
            Discount discount = this.shopService.modifyDiscount(userPrincipal.getId(), discountModifyDTO);
            return new ResponseEntity<>(new DiscountDTO(discount), HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (ShopDoesntExistException exception){
            throw new ShopNotFoundException(String.valueOf(discountModifyDTO.shopId));
        } catch (ProductDoesntExistException exception){
            throw new ProductNotFoundException(exception.getMessage());
        } catch (DiscountDoesntExistException exception){
            throw new DiscountNotFoundException(exception.getMessage());
        } catch (DiscountArgumentsMismatchException exception){
            throw new DiscountArgumentBadRequest(exception.getMessage());
        }
    }

    @CrossOrigin
    @DeleteMapping("/shop/discount")
    public ResponseEntity<DiscountDTO> deleteDiscount(@CurrentUser UserPrincipal userPrincipal,
                                                      @RequestParam(value = "shopId") String shopId,
                                                      @RequestParam(value = "discountId") String discountId){
        try {
            Discount discount = this.shopService.deleteDiscount(
                    userPrincipal.getId(), Long.parseLong(shopId), Long.parseLong(discountId));
            return new ResponseEntity<>(new DiscountDTO(discount), HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (ShopDoesntExistException exception){
            throw new ShopNotFoundException(shopId);
        } catch (DiscountDoesntExistException exception){
            throw new DiscountNotFoundException(exception.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/shop/deliveries")
    public ResponseEntity<List<ShopDeliveryDTO>> getDeliveries(@CurrentUser UserPrincipal userPrincipal,
                                                         @RequestParam(value = "shopId") String shopId){
        try {
            List<ShopDelivery> deliveries = this.shopService.getDeliveries(
                    userPrincipal.getId(), Long.parseLong(shopId));
            return new ResponseEntity<>(ShopDeliveryDTO.createDeliveries(deliveries), HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (ShopDoesntExistException exception){
            throw new ShopNotFoundException(shopId);
        }
    }

    @CrossOrigin
    @DeleteMapping("/shop/delivery")
    public ResponseEntity<ShopDeliveryDTO> removeDelivery(@CurrentUser UserPrincipal userPrincipal,
                                                          @RequestParam(value = "shopId") String shopId,
                                                          @RequestParam(value = "deliveryId") String deliveryId){
        try {
            ShopDelivery delivery = this.shopService.removeDelivery(
                    userPrincipal.getId(), Long.parseLong(shopId), Long.parseLong(deliveryId));
            return new ResponseEntity<>(new ShopDeliveryDTO(delivery), HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (ShopDoesntExistException exception){
            throw new ShopNotFoundException(shopId);
        } catch (DeliveryDoesntExistException exception){
            throw new DeliveryNotFound(exception.getMessage());
        }
    }
}
