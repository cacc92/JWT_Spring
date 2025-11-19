package com.duocuc.security_jwt.services.products;

import com.duocuc.security_jwt.models.products.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product getProductByName(String name);
    Product saveProduct(Product product);
}
