package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.objects.*;
import com.unq.dapp0.c1.comprandoencasa.repositories.DeliveryRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.ProductRepository;

import com.unq.dapp0.c1.comprandoencasa.services.exceptions.ProductDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.ProductIsInDiscountException;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ProductBatchDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ProductSmallDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopDoesntExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private DeliveryRepository deliveryRepository;

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
        Shop shop = getShopFromUser(user, productSmallDTO.shopId);
        Product product = createModifyForShop(shop, productSmallDTO);
        this.productRepository.save(product);
        this.shopService.save(shop);
        return product;
    }

    private Shop getShopFromUser(User user, Long shopId) {
        Optional<Shop> result = user.getShops().stream().filter(shop1 -> shop1.getId().equals(shopId)).findFirst();
        if (result.isPresent()){
            return result.get();
        } else {
            throw new ShopDoesntExistException(shopId);
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
        Product product = getProductFromShop(shop, productSmallDTO.id);
        product.setName(productSmallDTO.name);
        product.setBrand(productSmallDTO.brand);
        product.setImage(productSmallDTO.image);
        product.setPrice(productSmallDTO.price);
        return product;
    }

    private Product getProductFromShop(Shop shop, Long productId) {
        Optional<Product> result = shop.getProducts().stream().filter(product1 -> product1.getId().equals(productId)).findFirst();
        if (result.isPresent()){
            return result.get();
        } else {
            throw new ProductDoesntExistException(productId);
        }
    }

    @Transactional
    public Product deleteProduct(Long userId, long shopId, long productId) {
        User user = this.userService.findUserById(userId);
        Shop shop = getShopFromUser(user, shopId);
        Product product = getProductFromShop(shop, productId);
        checkIfProductIsInDiscount(shop, product);
        return deleteProduct(shop, product);
    }

    private void checkIfProductIsInDiscount(Shop shop, Product product) {
        List<Discount> discounts = shop.getDiscounts();
        boolean isPresent = discounts.stream().anyMatch(discount ->
                (discount.isTypeSingle()
                        && ((DiscountBySingle) discount).getProduct().getId().equals(product.getId()))
                || (discount.isTypeMultiple()
                        && ((DiscountByMultiple) discount).getProducts().stream()
                        .anyMatch(prod -> prod.getId().equals(product.getId())))
        );
        if (isPresent){
            throw new ProductIsInDiscountException(product);
        }

    }

    @Transactional
    public List<Product> createModifyProductList(Long userId, ProductBatchDTO productBatchDTO) {
        User user = this.userService.findUserById(userId);
        Shop shop = this.getShopFromUser(user, productBatchDTO.shopId);
        for (ProductSmallDTO productSmallDTO : productBatchDTO.productList){
            Product product = createModifyForShop(shop, productSmallDTO);
            this.productRepository.save(product);
        }
        this.shopService.save(shop);
        return shop.getProducts();
    }

    @Transactional
    public Product deleteProduct(Shop shop, Product product) {
        List<ShopDelivery> historicDeliveries = shop.getHistoricDeliveries();
        for (ShopDelivery delivery : historicDeliveries){
            List<Product> deliveryProducts = delivery.getProducts().stream()
                    .filter(prod -> !prod.getId().equals(product.getId())).collect(Collectors.toList());
            delivery.setProducts(deliveryProducts);
            User deliveryUser = delivery.getUser();
            this.userService.removeProductFromShoppingLists(deliveryUser, product);
        }
        this.deliveryRepository.saveAll(historicDeliveries);
        shop.removeProduct(product);
        this.shopService.save(shop);
        this.productRepository.delete(product);
        return product;
    }
}
