package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.LocationDoesNotExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.ProductDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.ProductService;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ProductDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.LocationNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ProductNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ProductTypeBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
