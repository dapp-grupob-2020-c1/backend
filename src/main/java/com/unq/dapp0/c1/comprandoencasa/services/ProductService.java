package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.*;
import com.unq.dapp0.c1.comprandoencasa.repositories.ProductRepository;

import com.unq.dapp0.c1.comprandoencasa.services.exceptions.LocationDoesNotExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.ProductDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ProductSmallDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopDoesntExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShopService shopService;

    @Transactional
    public Product save(Product model) {
        return this.productRepository.save(model);
    }

    @Transactional
    public Product findProductById(Long id) {
        Optional<Product> result = this.productRepository.findById(id);
        if (result.isPresent()){
            return result.get();
        } else {
            throw new ProductDoesntExistException(id);
        }
    }

    @Transactional
    public List<Product> searchBy(String keyword, List<ProductType> categories, Long locationId, Integer page, Integer size, String order) {
        Location location = locationService.findById(locationId);
        Sort sort = determineSort(order);
        Pageable pageable = PageRequest.of(page, size, sort);
        List<ProductType> cat = (categories.size() > 0) ? categories : Arrays.stream(ProductType.values()).collect(Collectors.toCollection(ArrayList::new));
        return productRepository.searchBy(keyword, cat, location.getLatitude(), location.getLongitude(), pageable);

    }

    private Sort determineSort(String order) {
        switch(order){
            case "idAsc":
                return Sort.by("id").ascending();
            case "priceDesc":
                return Sort.by("price").descending();
            case "priceAsc":
                return Sort.by("price").ascending();
            default:
                return Sort.by("id").descending();
        }
    }

    @Transactional
    public Product createModifyProduct(Long userId, ProductSmallDTO productSmallDTO) {
        User user = this.userService.findUserById(userId);
        Optional<Shop> result = user.getShops().stream().filter(shop1 -> shop1.getId().equals(productSmallDTO.shopId)).findFirst();
        if (result.isPresent()){
            Shop shop = result.get();
            Product product = createModifyForShop(shop, productSmallDTO);
            this.productRepository.save(product);
            this.shopService.save(shop);
            return product;
        } else {
            throw new ShopDoesntExistException(productSmallDTO.shopId);
        }
    }

    private Product createModifyForShop(Shop shop, ProductSmallDTO productSmallDTO) {
        if (productSmallDTO.id != null){
            return modifyProduct(shop, productSmallDTO);
        } else {
            return createProduct(shop, productSmallDTO);
        }
    }

    private Product createProduct(Shop shop, ProductSmallDTO productSmallDTO) {
        Product product = new Product(
                productSmallDTO.name,
                productSmallDTO.brand,
                productSmallDTO.image,
                productSmallDTO.price,
                shop,
                productSmallDTO.types
        );
        shop.addProduct(product);
        return product;
    }

    private Product modifyProduct(Shop shop, ProductSmallDTO productSmallDTO) {
        Optional<Product> result = shop.getProducts().stream().filter(product1 -> product1.getId().equals(productSmallDTO.id)).findFirst();
        if (result.isPresent()){
            Product product = result.get();
            product.setName(productSmallDTO.name);
            product.setBrand(productSmallDTO.brand);
            product.setImage(productSmallDTO.image);
            product.setPrice(productSmallDTO.price);
            return product;
        } else {
            throw new ProductDoesntExistException(productSmallDTO.id);
        }
    }
}
