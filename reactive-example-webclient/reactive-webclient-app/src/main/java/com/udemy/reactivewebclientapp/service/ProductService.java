package com.udemy.reactivewebclientapp.service;

import com.udemy.reactivewebclientapp.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Mono<Product> getProduct(String id);

    Flux<Product> getAllProducts();

    Mono<Void> deleteProduct();

    Mono<Product> saveProduct();
}
