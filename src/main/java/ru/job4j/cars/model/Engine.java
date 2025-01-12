package ru.job4j.cars.model;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "engine")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Engine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;
}
