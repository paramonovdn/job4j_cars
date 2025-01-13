package ru.job4j.cars.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "auto_post")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Post {

    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "description", "description",
            "created", "created",
            "auto_user_id", "autoUserId",
            "car_id", "carId"
    );
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private Timestamp created = Timestamp.valueOf(LocalDateTime.now());

    @Getter
    @Setter
    private int autoUserId;

    @Getter
    @Setter
    private int carId;


    @Getter
    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "price_history_id")
    private List<PriceHistory> messengers = new ArrayList<>();

}
