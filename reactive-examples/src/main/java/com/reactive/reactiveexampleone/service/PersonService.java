package com.reactive.reactiveexampleone.service;

import com.reactive.reactiveexampleone.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonService {
    Mono<Person> getById(Integer id);

    Flux<Person> findAll();
}
