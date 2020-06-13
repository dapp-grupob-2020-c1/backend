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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

    public Product findById(Long id) {
        Optional<Product> result = this.productRepository.findById(id);
        return result.orElse(null);
    }

    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Transactional
    public List<Product> searchBy(String keyword, List<ProductType> categories, Long locationId, Integer page, Integer size) {
        Optional<Location> location = locationRepository.findById(locationId);
        if (location.isPresent()){
            Location loc = location.get();
            Pageable pageable = PageRequest.of(page, size, Sort.by("price").descending());
            return productRepository.searchBy(keyword, categories, loc.getLatitude(), loc.getLongitude(), pageable);
        } else {
            return new ArrayList<>();
        }
    }
}
