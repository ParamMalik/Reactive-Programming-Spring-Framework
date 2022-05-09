package com.udemy.reactivewebclientapp.service;

import com.udemy.reactivewebclientapp.config.WebClientConfig;
import com.udemy.reactivewebclientapp.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final WebClient webClient;


    @Override
    public Mono<Product> getProduct(String id) {
        return webClient.get()
                .uri("{id}", id)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Product.class));
    }

    @Override
    public Flux<Product> getAllProducts() {
        return webClient.get()
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Product.class));
    }

    @Override
    public Mono<Void> deleteProduct() {
        return null;
    }

    @Override
    public Mono<Product> saveProduct() {
        return null;
    }
}
