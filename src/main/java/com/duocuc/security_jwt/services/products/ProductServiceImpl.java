package com.duocuc.security_jwt.services.products;

import com.duocuc.security_jwt.models.products.Product;
import com.duocuc.security_jwt.repositories.products.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return this.productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("El producto con id " + id + " no fue encontrado")
        );
    }

    @Override
    public Product getProductByName(String name) {
        return this.productRepository.findByName(name).orElseThrow(
                () -> new RuntimeException("El producto con nombre " + name + " no fue encontrado")
        );
    }

    @Transactional
    @Override
    public Product saveProduct(Product product) {
        return this.productRepository.save(product);
    }
}
