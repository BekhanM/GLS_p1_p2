package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "thepackages")

public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String trackingNr;

    private String senderName;

    private String receiverName;

    @Enumerated(EnumType.STRING)
    private deliveryStatus status;

    @OneToMany(mappedBy = "thePackage", cascade = CascadeType.ALL)
    private Set <Shipment> shipments = new HashSet<>();


    public enum deliveryStatus {
        PENDING, IN_TRANSIT, DELIVERED
    }
}
