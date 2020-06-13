package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;
import com.unq.dapp0.c1.comprandoencasa.services.ProductService;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ProductDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ProductTypeBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@EnableAutoConfiguration
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/api/products")
    public List<Product> allProducts() {
        return this.productService.findAll();
    }

    @GetMapping("/api/search")
    public List<ProductDTO> searchProducts(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                           @RequestParam(value = "categories") List<String> categories,
                                           @RequestParam(value = "locationId") String locationId,
                                           @RequestParam(value = "page") String page,
                                           @RequestParam(value = "size") String size){
        try{
            List<ProductType> types = this.parseToType(categories);
            List<Product> response = productService.searchBy(keyword, types, Long.valueOf(locationId), Integer.valueOf(page), Integer.valueOf(size));
            return parseProducts(response);
        } catch (IllegalArgumentException e){
            throw new ProductTypeBadRequestException();
        }
    }

    static public List<ProductDTO> parseProducts(List<Product> products) {
        List<ProductDTO> response = new ArrayList<>();
        for (Product product : products){
            response.add(new ProductDTO(product));
        }
        return response;
    }

    private List<ProductType> parseToType(List<String> categories) {
        List<ProductType> list = new ArrayList<>();
        for (String category : categories){
            list.add(ProductType.valueOf(category));
        }
        return list;
    }

}
