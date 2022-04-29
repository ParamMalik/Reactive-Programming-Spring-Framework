package com.webclient.reactivebeerclient.client;

import com.webclient.reactivebeerclient.config.WebClientConfig;
import com.webclient.reactivebeerclient.model.BeerDto;
import com.webclient.reactivebeerclient.model.BeerPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeerClientImplTest {

    BeerClientImpl beerClient;

    @BeforeEach
    void setUp() {
        beerClient = new BeerClientImpl(new WebClientConfig().webClient());
    }

    @Test
    void getBeerById() {

        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList beerList = beerPagedListMono.block();
        UUID id = beerList.getContent().get(0).getId();

        Mono<BeerDto> beerById = beerClient.getBeerById(id, false);
        BeerDto beerDTO = beerById.block();

        assertThat(beerDTO.getId()).isEqualTo(id);
        assertThat(beerDTO.getQuantityOnHand()).isNotNull();

    }

    @Test
    void listBeers() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);

        BeerPagedList beerList = beerPagedListMono.block();

        assertThat(beerList).isNotNull();
        assertThat(beerList.getContent().size()).isNotNull();
        System.out.println(beerList.toList());
    }

    @Test
    void createBeer() {
        BeerDto newBeer = BeerDto.builder()
                .beerName("Sherif is someone")
                .beerStyle("NATO")
                .upc("2132483")
                .price(new BigDecimal("10.03"))
                .build();

        Mono<ResponseEntity<Void>> myBeer = beerClient.createBeer(newBeer);

        ResponseEntity<Void> responseEntity = myBeer.block();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void updateBeer() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList beerList = beerPagedListMono.block();
        BeerDto beerDto = beerList.getContent().get(0);

        BeerDto updatedBeer = BeerDto.builder()
                .beerName("Hello")
                .beerStyle(beerDto.getBeerStyle())
                .price(beerDto.getPrice())
                .upc(beerDto.getUpc())
                .build();

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.updateBeer(beerDto.getId(), updatedBeer);
        ResponseEntity<Void> responseEntity = responseEntityMono.block();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);


    }

    // Handling Exception

    @Test
    void testDeleteBeerHandleException() {
        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeerById(UUID.randomUUID());
        ResponseEntity<Void> responseEntity = responseEntityMono.onErrorResume(throwable -> {
            if (throwable instanceof WebClientException) {
                WebClientResponseException exception = (WebClientResponseException) throwable;
                return Mono.just(ResponseEntity.status(exception.getStatusCode()).build());
            } else {
                throw new RuntimeException(throwable);
            }
        }).block();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void deleteBeerByIdNotFound() {

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeerById(UUID.randomUUID());
        assertThrows(WebClientException.class, () -> {
            ResponseEntity<Void> responseEntity = responseEntityMono.block();
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        });

    }

    @Test
    void deleteBeerById() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList beerList = beerPagedListMono.block();

        BeerDto beerDto = beerList.getContent().get(0);

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeerById(beerDto.getId());
        ResponseEntity<Void> responseEntity = responseEntityMono.block();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);


    }

    @Test
    void functionTestGetBeerById() {
        beerClient.listBeers(null, null, null, null, null)
                .map(beerDtos -> beerDtos.getContent().get(0).getId())
                .map(beerId -> beerClient.getBeerById(beerId, false))
                .flatMap(mono -> mono)
                .subscribe(beerDto -> System.out.println(beerDto.getBeerName()));
    }


}