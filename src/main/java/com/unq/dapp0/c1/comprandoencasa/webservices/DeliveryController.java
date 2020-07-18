package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.objects.ShopDelivery;
import com.unq.dapp0.c1.comprandoencasa.services.DeliveryService;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.DeliveryDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.UserDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.security.UserPrincipal;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopDeliveryDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.DeliveryNotFound;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @CrossOrigin
    @GetMapping("/all")
    public ResponseEntity<List<ShopDeliveryDTO>> getAllActiveDeliveries(@CurrentUser UserPrincipal userPrincipal){
        try{
            List<ShopDelivery> deliveries = this.deliveryService.getUserActiveDeliveries(userPrincipal.getId());
            return new ResponseEntity<>(ShopDeliveryDTO.createDeliveries(deliveries), HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/historic")
    public ResponseEntity<List<ShopDeliveryDTO>> getAllHistoricDeliveries(@CurrentUser UserPrincipal userPrincipal){
        try{
            List<ShopDelivery> deliveries = this.deliveryService.getUserHistoricDeliveries(userPrincipal.getId());
            return new ResponseEntity<>(ShopDeliveryDTO.createDeliveries(deliveries), HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<ShopDeliveryDTO> confirmDeliveryReception(@CurrentUser UserPrincipal userPrincipal,
                                                                    @RequestParam(value = "deliveryId") String deliveryId){
        try{
            ShopDelivery delivery = this.deliveryService.confirmDeliveryReception(userPrincipal.getId(), Long.valueOf(deliveryId));
            return new ResponseEntity<>(new ShopDeliveryDTO(delivery), HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (DeliveryDoesntExistException exception){
            throw new DeliveryNotFound(exception.getMessage());
        }
    }

    @CrossOrigin
    @DeleteMapping
    public ResponseEntity<ShopDeliveryDTO> cancelDelivery(@CurrentUser UserPrincipal userPrincipal,
                                                          @RequestParam(value = "deliveryId") String deliveryId){
        try{
            ShopDelivery delivery = this.deliveryService.cancelDelivery(userPrincipal.getId(), Long.valueOf(deliveryId));
            return new ResponseEntity<>(new ShopDeliveryDTO(delivery), HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (DeliveryDoesntExistException exception){
            throw new DeliveryNotFound(exception.getMessage());
        }
    }

}
