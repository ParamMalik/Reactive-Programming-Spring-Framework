package com.reactive.reactiveexampleone.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Person {
    private Integer id;
    private String firstName;
    private String lastName;
}
