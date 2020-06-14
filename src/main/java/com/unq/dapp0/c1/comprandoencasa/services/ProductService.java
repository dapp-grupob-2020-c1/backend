package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;
import com.unq.dapp0.c1.comprandoencasa.repositories.LocationRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.ProductRepository;

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
    private LocationRepository locationRepository;

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
        Optional<Location> location = locationRepository.findById(locationId);
        if (location.isPresent()){
            Location loc = location.get();
            Sort sort = determineSort(order);
            Pageable pageable = PageRequest.of(page, size, sort);
            List<ProductType> cat = (categories.size() > 0) ? categories : Arrays.stream(ProductType.values()).collect(Collectors.toCollection(ArrayList::new));
            return productRepository.searchBy(keyword, cat, loc.getLatitude(), loc.getLongitude(), pageable);
        } else {
            throw new LocationDoesNotExistException(locationId);
        }
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
}
