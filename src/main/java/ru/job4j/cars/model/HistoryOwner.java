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
            "owner_id", "owner",
            "history_id", "history"
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
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "history_id")
    private History history;
}
