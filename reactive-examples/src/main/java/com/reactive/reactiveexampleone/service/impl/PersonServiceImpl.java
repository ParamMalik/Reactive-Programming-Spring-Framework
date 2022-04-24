package com.reactive.reactiveexampleone.service.impl;

import com.reactive.reactiveexampleone.model.Person;
import com.reactive.reactiveexampleone.service.PersonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonServiceImpl implements PersonService {

    Person sara = new Person(1, "sara", "happy");
    Person saraDup = new Person(5, "sara", "singh");
    Person rahul = new Person(2, "rahul", "happy");
    Person shivam = new Person(3, "shivam", "happy");
    Person john = new Person(4, "john", "happy");


    @Override
    public Mono<Person> getById(Integer id) {
        return Mono.just(sara);
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(sara, rahul, shivam, john);
    }

    // just method to add elements in the data streams (Mono and Flux)
}
