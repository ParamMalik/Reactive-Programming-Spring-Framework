package com.udemy.reactivewebclientapp.service;

import com.udemy.reactivewebclientapp.config.WebClientConfig;
import com.udemy.reactivewebclientapp.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplTest {

    ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(new WebClientConfig().webClient());
    }
    @Test
    void getAllProducts() {
        Flux<Product> allProducts = productService.getAllProducts();

        Product product = allProducts.blockFirst();
        System.out.println(product.getId());

        assertThat(product).isNotNull();
        assertThat(product.getId()).isNotNull();
    }

    @Test
    void getProduct() {
        Flux<Product> allProducts = productService.getAllProducts();
        Product product = allProducts.blockFirst();

        String id = product.getId();

        Mono<Product> product1 = productService.getProduct(id);
        Product productR = product1.block();
        assertThat(product).isNotNull();
        assertThat(productR.equals(product)).isTrue();

    }


    @Test
    void deleteProduct() {
    }

    @Test
    void saveProduct() {
    }
}