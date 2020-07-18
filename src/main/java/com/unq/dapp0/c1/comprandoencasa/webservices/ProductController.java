package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Product;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ProductType;
import com.unq.dapp0.c1.comprandoencasa.services.ProductService;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.NoActiveShoppingListException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.ProductDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.ProductIsInDiscountException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.UserDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.security.UserPrincipal;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ProductBatchDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ProductDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ProductSmallDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ProductNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ProductTypeBadRequestException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShoppingListNotFound;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
            throw new ProductNotFoundException(Long.valueOf(productId));
        }
    }

    @CrossOrigin
    @GetMapping("/product/search")
    public List<ProductDTO> searchProducts(@CurrentUser UserPrincipal userPrincipal,
                                           @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                           @RequestParam(value = "categories", defaultValue = "") List<String> categories,
                                           @RequestParam(value = "page", defaultValue = "0") String page,
                                           @RequestParam(value = "size", defaultValue = "10") String size,
                                           @RequestParam(value = "order", defaultValue = "idDesc") String order){
        try{
            List<ProductType> types = parseToTypes(categories);
            List<Product> response = productService.searchBy(userPrincipal.getId(), keyword, types, Integer.valueOf(page), Integer.valueOf(size), order);
            return ProductDTO.parseProducts(response);
        } catch (IllegalArgumentException e){
            throw new ProductTypeBadRequestException();
        } catch (UserDoesntExistException e){
            throw new UserNotFoundException(e.getLocalizedMessage());
        } catch (NoActiveShoppingListException e){
            throw new ShoppingListNotFound(e.getMessage());
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
            throw new ShopNotFoundException(exception.getMessage());
        } catch (ProductDoesntExistException exception){
            throw new ProductNotFoundException(productSmallDTO.id);
        }
    }

    @CrossOrigin
    @DeleteMapping("/product")
    public ResponseEntity<ProductSmallDTO> deleteProduct(@CurrentUser UserPrincipal userPrincipal,
                                                         @RequestParam(value = "shopId") String shopId,
                                                         @RequestParam(value = "productId") String productId){
        try{
            Product product = this.productService.deleteProduct(
                    userPrincipal.getId(), Long.parseLong(shopId), Long.parseLong(productId));
            return new ResponseEntity<>(new ProductSmallDTO(product), HttpStatus.OK);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (ShopDoesntExistException exception){
            throw new ShopNotFoundException(exception.getMessage());
        } catch (ProductDoesntExistException exception){
            throw new ProductNotFoundException(Long.valueOf(productId));
        } catch (ProductIsInDiscountException exception){
            throw new BadRequestException(exception.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/products")
    public ResponseEntity<List<ProductSmallDTO>> createModifyProductBatch(@CurrentUser UserPrincipal userPrincipal,
                                                                          @RequestBody ProductBatchDTO productBatchDTO){
        try{
            List<Product> products = this.productService.createModifyProductList(userPrincipal.getId(), productBatchDTO);
            return new ResponseEntity<>(ProductSmallDTO.createProducts(products), HttpStatus.CREATED);
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        } catch (ShopDoesntExistException exception){
            throw new ShopNotFoundException(exception.getMessage());
        } catch (ProductDoesntExistException exception){
            throw new ProductNotFoundException(exception.getMessage());
        }
    }

    public static List<ProductDTO> parseProducts(List<Product> products) {
        List<ProductDTO> response = new ArrayList<>();
        for (Product product : products){
            response.add(new ProductDTO(product));
        }
        return response;
    }

    public static List<ProductType> parseToTypes(List<String> categories) {
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
