package com.netflux.reactiveexampletwo.service;

import com.netflux.reactiveexampletwo.events.MovieEvent;
import com.netflux.reactiveexampletwo.model.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieService {
    Mono<Movie> getMovieById(String id);

    Flux<Movie> getAllMovies();

    Flux<MovieEvent> streamMovieEvents(String id);

}
