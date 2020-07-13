package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.LocationDoesNotExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.ProductDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.ProductService;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.UserDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.security.UserPrincipal;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ProductDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ProductSmallDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.*;
import com.unq.dapp0.c1.comprandoencasa.webservices.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@EnableAutoConfiguration
public class ProductController {

    @Autowired
    private ProductService productService;

    @CrossOrigin
    @GetMapping("/product")
    public ProductDTO getProduct(@RequestParam(value = "productId") String productId) {
        try{
            Product product = this.productService.findProductById(Long.valueOf(productId));
            return new ProductDTO(product);
        } catch (ProductDoesntExistException exception){
            throw new ProductNotFoundException(productId);
        }
    }

    @CrossOrigin
    @GetMapping("/product/search")
    public List<ProductDTO> searchProducts(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                           @RequestParam(value = "categories", defaultValue = "") List<String> categories,
                                           @RequestParam(value = "locationId") String locationId,
                                           @RequestParam(value = "page", defaultValue = "0") String page,
                                           @RequestParam(value = "size", defaultValue = "10") String size,
                                           @RequestParam(value = "order", defaultValue = "idDesc") String order){
        try{
            List<ProductType> types = parseToTypes(categories);
            List<Product> response = productService.searchBy(keyword, types, Long.valueOf(locationId), Integer.valueOf(page), Integer.valueOf(size), order);
            return parseProducts(response);
        } catch (IllegalArgumentException e){
            throw new ProductTypeBadRequestException();
        } catch (LocationDoesNotExistException e){
            throw new LocationNotFoundException(locationId);
        }
    }

    @CrossOrigin
    @PostMapping("/product")
    public ResponseEntity<ProductSmallDTO> createModifyProduct(@CurrentUser UserPrincipal userPrincipal,
                                                               @RequestBody ProductSmallDTO productSmallDTO){
        try{
            Product product = this.productService.createModifyProduct(userPrincipal.getId(), productSmallDTO);
            return new ResponseEntity<>(new ProductSmallDTO(product), HttpStatus.CREATED);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (ShopDoesntExistException exception){
            throw new ShopNotFoundException(String.valueOf(productSmallDTO.shopId));
        } catch (ProductDoesntExistException exception){
            throw new ProductNotFoundException(String.valueOf(productSmallDTO.id));
        }
    }

    static public List<ProductDTO> parseProducts(List<Product> products) {
        List<ProductDTO> response = new ArrayList<>();
        for (Product product : products){
            response.add(new ProductDTO(product));
        }
        return response;
    }

    static public List<ProductType> parseToTypes(List<String> categories) {
        if (categories.isEmpty()){
            return Arrays.stream(ProductType.values()).collect(Collectors.toCollection(ArrayList::new));
        }
        List<ProductType> list = new ArrayList<>();
        for (String category : categories){
            list.add(ProductType.valueOf(category));
        }
        return list;
    }

}
