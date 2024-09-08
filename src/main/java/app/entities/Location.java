package app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "thelocations")


public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private double latitude;
    private double longitude;
    private String address;
}
