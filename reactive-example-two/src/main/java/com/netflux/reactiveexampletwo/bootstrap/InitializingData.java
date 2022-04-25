package com.netflux.reactiveexampletwo.bootstrap;

import com.netflux.reactiveexampletwo.model.Movie;
import com.netflux.reactiveexampletwo.repo.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

// Here we are initializing data using CommandLineRunner interface

@AllArgsConstructor
@Component
public class InitializingData implements CommandLineRunner {
    private final MovieRepository movieRepository;


    @Override
    public void run(String... args) throws Exception {
        movieRepository.deleteAll()
                .thenMany(Flux.just("Thor","RRR","KGF","Caption America")
                .map(title -> Movie.builder().title(title).build())
                .flatMap(movieRepository::save)).subscribe(null ,null, () -> movieRepository.findAll().subscribe(System.out::println));
    }
}
