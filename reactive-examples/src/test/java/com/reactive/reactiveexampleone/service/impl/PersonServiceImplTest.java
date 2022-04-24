package com.reactive.reactiveexampleone.service.impl;

import com.reactive.reactiveexampleone.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

class PersonServiceImplTest {

    PersonServiceImpl personService;

    @BeforeEach
    void setUp() {
        personService = new PersonServiceImpl();
    }

    @Test
    void getByIdBlock() {
        Mono<Person> gettingPersonById = personService.getById(2);
        Person blockPerson = gettingPersonById.block();
        // Blocking is not preferred
        System.out.println(blockPerson.toString());
    }

    // Subscriber is preferred
    @Test
    void getBySubscriberId() {

        Mono<Person> getPersonById = personService.getById(3);
        StepVerifier.create(getPersonById).expectNextCount(1).verifyComplete();

        getPersonById.subscribe(System.out::println);

    }


    @Test
    void getByIdUsingMapFunction() {
        // So we are going to take a person object in this stream and map it down to the string of firstName
        // then putting back pressure to put map in action using subscription.

        Mono<Person> getPersonById = personService.getById(1);
        getPersonById.map(person -> {
            // map does not work until we apply back pressure on it . we use subscriber to do that
            // subscriber is asking for the firstName here

            System.out.println("firstName in the map : " + person.getFirstName());
            return person.getFirstName();
        }).subscribe(firstName -> {
            System.out.println("first name From map : " + firstName);
        });
    }

    @Test
    void fluxTestBlockFirst() {
        Flux<Person> allPerson = personService.findAll();
        // blockFirst gives first element and block last gives us the last element as allPerson is a flux
        Person person = allPerson.blockLast();
        System.out.println(person.toString());

    }

    @Test
    void fluxTestUsingSubscribe() {
        Flux<Person> allPerson = personService.findAll();
        // This is testing our repo methods
        StepVerifier.create(allPerson).expectNextCount(4).verifyComplete();
        allPerson.subscribe(person -> {
            System.out.println(person.toString());
        });

    }


    @Test
    void testFluxToListMono() {
        Flux<Person> personFlux = personService.findAll();
        StepVerifier.create(personFlux).expectNextCount(4).verifyComplete();
        Mono<List<Person>> personList = personFlux.collectList();
        personList.subscribe(list -> {
            list.forEach(person -> System.out.println(person.toString()));
        });
    }


    @Test
    void testFindByFluxAndAddingFilter() {
        Flux<Person> gettingAllPerson = personService.findAll();
        StepVerifier.create(gettingAllPerson).expectNextCount(4).verifyComplete();
        Mono<Person> personByFirstNameFilter = gettingAllPerson.filter(person -> person.getLastName().equals("happy")).next();
        personByFirstNameFilter.subscribe(System.out::println);
    }


    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personService.findAll();

        final int id = 8;

        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).next();
        // This means we are not expecting anything as we are not providing expectNextCount
        StepVerifier.create(personMono).verifyComplete();
        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFindPersonByIdNotFoundWithException() {
        Flux<Person> personFlux = personService.findAll();

        final int id = 8;

        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).single();

        personMono.doOnError(throwable -> {
                    System.out.println("Error Occurred. ");
                }).onErrorReturn(Person.builder().id(id).build())      // returning the empty object of Person on error
                .subscribe(person -> {
                    System.out.println(person.toString());
                });
    }


}