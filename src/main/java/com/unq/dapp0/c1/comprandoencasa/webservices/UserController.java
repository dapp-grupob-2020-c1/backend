package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.User;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.LocationDoesNotExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.UserDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.UserService;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.services.security.UserPrincipal;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopFullDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopSmallDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ThresholdSetDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.UserDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.UserThresholdsDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.EmptyFieldsBadRequestException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.LocationNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.UserNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        User user = userService.findUserById(userPrincipal.getId());
        return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getLocations(@CurrentUser UserPrincipal userPrincipal) {
        try{
            List<Location> locationList = this.userService.getLocationsOf(userPrincipal.getId());
            return new ResponseEntity<>(locationList, HttpStatus.OK);
        } catch (EmptyFieldException exception){
            throw new EmptyFieldsBadRequestException(exception.getMessage());
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/location")
    public ResponseEntity<Location> createLocation(@CurrentUser UserPrincipal userPrincipal,
                                                   @RequestParam(value = "address") String address,
                                                   @RequestParam(value = "latitude") String latitude,
                                                   @RequestParam(value = "longitude") String longitude) {
        try{
            checkField(address, "address");
            checkField(latitude, "latitude");
            checkField(longitude, "longitude");
            Location location = this.userService.addLocationTo(userPrincipal.getId(),
                    address,
                    Double.valueOf(latitude),
                    Double.valueOf(longitude));
            return new ResponseEntity<>(location, HttpStatus.CREATED);
        } catch (EmptyFieldException exception){
            throw new EmptyFieldsBadRequestException(exception.getMessage());
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        }
    }

    @CrossOrigin
    @DeleteMapping("/location")
    public ResponseEntity<Location> deleteLocation(@CurrentUser UserPrincipal userPrincipal,
                                                   @RequestParam(value = "locationId") String locationId){
        try{
            checkField(locationId, "locationId");
            Location location = this.userService.removeLocationOf(userPrincipal.getId(),
                    Long.parseLong(locationId));
            return new ResponseEntity<>(location, HttpStatus.OK);
        } catch (EmptyFieldException exception){
            throw new EmptyFieldsBadRequestException(exception.getMessage());
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (LocationDoesNotExistException exception){
            throw new LocationNotFoundException(exception.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/myshops")
    public ResponseEntity<List<ShopSmallDTO>> getMyShops(@CurrentUser UserPrincipal userPrincipal){
        List<Shop> shops = this.userService.getShopsFrom(userPrincipal.getId());
        return new ResponseEntity<>(ShopSmallDTO.generateShopSmallDTOList(shops), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/myshop")
    public ResponseEntity<ShopFullDTO> getMyShop(@CurrentUser UserPrincipal userPrincipal,
                                                 @RequestParam(value = "shopId") String shopId){
        try{
            Shop shop = this.userService.getShopFrom(userPrincipal.getId(), Long.parseLong(shopId));
            return new ResponseEntity<>(new ShopFullDTO(shop), HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (ShopDoesntExistException exception){
            throw new ShopNotFoundException(exception.getMessage());
        }
    }


    private void checkField(String field, String fieldName) {
        if (field.isEmpty()){
            throw new EmptyFieldException(fieldName);
        }
    }

    @CrossOrigin
    @GetMapping("/thresholds")
    public ResponseEntity<UserThresholdsDTO> getUserThresholds(@CurrentUser UserPrincipal userPrincipal){
        try{
            UserThresholdsDTO thresholdsDTO = this.userService.getThresholds(userPrincipal.getId());
            return new ResponseEntity<>(thresholdsDTO, HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/threshold/type")
    public ResponseEntity<UserThresholdsDTO> setUserTypeThreshold(@CurrentUser UserPrincipal userPrincipal,
                                                                  @RequestBody ThresholdSetDTO thresholdDTO){
        try{
            UserThresholdsDTO thresholdsDTO = this.userService.setThreshold(userPrincipal.getId(), thresholdDTO);
            return new ResponseEntity<>(thresholdsDTO, HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/threshold/total")
    public ResponseEntity<UserThresholdsDTO> setUserTotalThreshold(@CurrentUser UserPrincipal userPrincipal,
                                                                   @RequestParam(value = "threshold") String threshold){
        try{
            UserThresholdsDTO thresholdsDTO = this.userService
                    .setTotalThreshold(userPrincipal.getId(), BigDecimal.valueOf(Long.parseLong(threshold)));
            return new ResponseEntity<>(thresholdsDTO, HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        }
    }

}
