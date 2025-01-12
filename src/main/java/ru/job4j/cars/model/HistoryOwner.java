package ru.job4j.cars.model;

import lombok.*;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "history_owner")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HistoryOwner {


    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "car_id", "carId",
            "owner_id", "ownerId"
    );
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private int carId;
    @Getter
    @Setter
    private int ownerId;
}