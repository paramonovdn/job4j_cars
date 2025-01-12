package ru.job4j.cars.model;


import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "history")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private Timestamp startAt = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));

    @Getter
    @Setter
    private Timestamp endAt = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));


}
