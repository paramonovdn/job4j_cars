package ru.job4j.cars.model;


import lombok.*;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "participates")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Participate {

    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "user_id", "userId",
            "post_id", "postId"
    );

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private int userId;

    @Getter
    @Setter
    private int postId;


}
