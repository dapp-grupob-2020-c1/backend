package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;
import com.unq.dapp0.c1.comprandoencasa.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Product save(Product model) {
        return this.repository.save(model);
    }

    public Product findById(Long id) {
        return this.repository.findById(id).get();
    }

    public List<Product> findAll() {
        return this.repository.findAll();
    }

    public List<Product> searchBy(String keyword, List<ProductType> categories) {
        return null;
    }
}
