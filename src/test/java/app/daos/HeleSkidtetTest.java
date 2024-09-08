package app.daos;

import app.entities.Location;
import app.entities.Package;
import app.entities.Shipment;
import app.persistence.HibernateConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HeleSkidtetTest {

    private EntityManagerFactory emf;
    private LocationDAO locationDAO;
    private PackageDAO packageDAO;
    private ShipmentDAO shipmentDAO;

    @BeforeEach
    public void setUp() {
        emf = HibernateConfig.getEntityManagerFactory();
        locationDAO = new LocationDAO();
        packageDAO = new PackageDAO();
        shipmentDAO = new ShipmentDAO();
        wipeDatabase();
    }

    private void wipeDatabase() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Shipment").executeUpdate();
            em.createQuery("DELETE FROM Package").executeUpdate();
            em.createQuery("DELETE FROM Location").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testCreatePackage() {
        Package pkg = Package.builder()
                .trackingNr("1234")
                .senderName("Tom Cruise")
                .receiverName("Bekhan Mahauri")
                .build();

        packageDAO.create(pkg);

        Package foundPKG = packageDAO.getByTrackingNumber("1234");

        assertEquals("1234", foundPKG.getTrackingNr());
        assertEquals("Tom Cruise", foundPKG.getSenderName());
        assertEquals("Bekhan Mahauri", foundPKG.getReceiverName());
    }

    @Test
    public void testUpdatePackageStatus() {
        Package pkg = Package.builder()
                .trackingNr("1234")
                .senderName("Tom Cruise")
                .receiverName("Bekhan Mahauri")
                .build();
        packageDAO.create(pkg);

        packageDAO.updatePackageStatus("1234", Package.deliveryStatus.IN_TRANSIT);

        assertEquals(Package.deliveryStatus.IN_TRANSIT, packageDAO.getByTrackingNumber("1234").getStatus());
    }

    @Test
    public void testDeletePackage() {
        Package pkg = Package.builder()
                .trackingNr("1234")
                .senderName("Tom Cruise")
                .receiverName("Bekhan Mahauri")
                .build();
        packageDAO.create(pkg);

        packageDAO.delete("1234");

        assertNull(packageDAO.getById(pkg.getId()));
    }

    @Test
    public void testCreateLocation() {
        Location fromLocation = Location.builder()
                .latitude(40.7128)
                .longitude(-74.0060)
                .address("New York, NY")
                .build();
        locationDAO.create(fromLocation);

        Location toLocation = Location.builder()
                .latitude(55.9279)
                .longitude(12.3008)
                .address("Hillerød, Denmark")
                .build();
        locationDAO.create(toLocation);

        assertEquals("New York, NY", locationDAO.getById(fromLocation.getId()).getAddress());
        assertEquals("Hillerød, Denmark", locationDAO.getById(toLocation.getId()).getAddress());
    }

    @Test
    public void testUpdateLocation() {
        Location location = Location.builder()
                .latitude(40.7128)
                .longitude(-74.0060)
                .address("New York, NY")
                .build();
        locationDAO.create(location);

        location.setAddress("Hillerød, Denmark");
        locationDAO.update(location);

        assertEquals("Hillerød, Denmark", locationDAO.getById(location.getId()).getAddress());
    }

    @Test
    public void testDeleteLocation() {
        Location location = Location.builder()
                .latitude(40.7128)
                .longitude(-74.0060)
                .address("New York, NY")
                .build();
        locationDAO.create(location);

        locationDAO.delete(location);
        assertNull(locationDAO.getById(location.getId()));
    }

    @Test
    public void testCreateShipment() {
        Location fromLocation = Location.builder()
                .latitude(40.7128)
                .longitude(-74.0060)
                .address("New York, NY")
                .build();
        locationDAO.create(fromLocation);

        Location toLocation = Location.builder()
                .latitude(55.9279)
                .longitude(12.3008)
                .address("Hillerød, Denmark")
                .build();
        locationDAO.create(toLocation);

        Package pkg = Package.builder()
                .trackingNr("1234")
                .senderName("Tom Cruise")
                .receiverName("Bekhan Mahauri")
                .build();
        packageDAO.create(pkg);

        Shipment shipment = Shipment.builder()
                .thePackage(pkg)
                .fromLocation(fromLocation)
                .toLocation(toLocation)
                .shipmentdatetime(LocalDateTime.now())
                .build();
        shipmentDAO.create(shipment);

        Shipment foundShipment = shipmentDAO.getById(shipment.getId());
        assertEquals(pkg.getTrackingNr(), foundShipment.getThePackage().getTrackingNr());
        assertEquals(fromLocation.getAddress(), foundShipment.getFromLocation().getAddress());
        assertEquals(toLocation.getAddress(), foundShipment.getToLocation().getAddress());
    }

    @Test
    public void testUpdateShipment() {
        Location fromLocation = Location.builder()
                .latitude(40.7128)
                .longitude(-74.0060)
                .address("New York, NY")
                .build();
        locationDAO.create(fromLocation);

        Location toLocation = Location.builder()
                .latitude(55.9279)
                .longitude(12.3008)
                .address("Hillerød, Denmark")
                .build();
        locationDAO.create(toLocation);

        Package pkg = Package.builder()
                .trackingNr("1234")
                .senderName("Tom Cruise")
                .receiverName("Bekhan Mahauri")
                .build();
        packageDAO.create(pkg);

        Shipment shipment = Shipment.builder()
                .thePackage(pkg)
                .fromLocation(fromLocation)
                .toLocation(toLocation)
                .shipmentdatetime(LocalDateTime.now())
                .build();
        shipmentDAO.create(shipment);

        Location newToLocation = Location.builder()
                .latitude(55.6761)
                .longitude(12.5683)
                .address("Copenhagen, Denmark")
                .build();
        locationDAO.create(newToLocation);

        shipment.setToLocation(newToLocation);
        shipmentDAO.update(shipment);

        assertEquals(newToLocation.getAddress(), shipmentDAO.getById(shipment.getId()).getToLocation().getAddress());
    }

    @Test
    public void testDeleteShipment() {
        Location fromLocation = Location.builder()
                .latitude(40.7128)
                .longitude(-74.0060)
                .address("New York, NY")
                .build();
        locationDAO.create(fromLocation);

        Location toLocation = Location.builder()
                .latitude(55.9279)
                .longitude(12.3008)
                .address("Hillerød, Denmark")
                .build();
        locationDAO.create(toLocation);

        Package pkg = Package.builder()
                .trackingNr("1234")
                .senderName("Tom Cruise")
                .receiverName("Bekhan Mahauri")
                .build();
        packageDAO.create(pkg);

        Shipment shipment = Shipment.builder()
                .thePackage(pkg)
                .fromLocation(fromLocation)
                .toLocation(toLocation)
                .shipmentdatetime(LocalDateTime.now())
                .build();
        shipmentDAO.create(shipment);

        shipmentDAO.delete(shipment);
        assertNull(shipmentDAO.getById(shipment.getId()));
    }
}
