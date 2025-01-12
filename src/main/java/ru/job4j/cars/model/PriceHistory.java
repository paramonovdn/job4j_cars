package ru.job4j.cars.model;


import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_history")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private BigInteger before;

    @Getter
    @Setter
    private BigInteger after;

    @Getter
    @Setter
    private Timestamp created = Timestamp.valueOf(LocalDateTime.now());

}
