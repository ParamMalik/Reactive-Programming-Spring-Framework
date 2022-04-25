package com.netflux.reactiveexampletwo.repo;

import com.netflux.reactiveexampletwo.model.Movie;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MovieRepository extends ReactiveMongoRepository<Movie, String> {
}
