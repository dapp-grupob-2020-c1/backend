package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.services.security.UserPrincipal;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopDeliveryDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.security.CurrentUser;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/delivery")
public class DeliveryController {

    /*
    @CrossOrigin
    @GetMapping("/all")
    public ResponseEntity<List<ShopDeliveryDTO>> getAllActiveDeliveries(@CurrentUser UserPrincipal userPrincipal){

    }

    @CrossOrigin
    @GetMapping("/historic")
    public ResponseEntity<ShopDeliveryDTO> getAllHistoricDeliveries(@CurrentUser UserPrincipal userPrincipal){

    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<ShopDeliveryDTO> confirmDeliveryReception(@CurrentUser UserPrincipal userPrincipal,
                                                                    @RequestParam(value = "deliveryId") String deliveryId){

    }

    @CrossOrigin
    @DeleteMapping
    public ResponseEntity<ShopDeliveryDTO> cancelDelivery(@CurrentUser UserPrincipal userPrincipal,
                                                          @RequestParam(value = "deliveryId") String deliveryId){

    }*/

}
