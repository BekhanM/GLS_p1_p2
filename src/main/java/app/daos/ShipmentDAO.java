package app.daos;

import app.entities.Location;
import app.entities.Shipment;
import app.persistence.HibernateConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ShipmentDAO {
    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();


    public Shipment getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Shipment.class,id);
        }
    }


    public Set<Shipment> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Shipment> query = em.createQuery("SELECT s FROM Shipment s", Shipment.class);
            List<Shipment> shipmentList = query.getResultList();
            return shipmentList.stream().collect(Collectors.toSet());
        }
    }


    public void create(Shipment shipment) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(shipment);
            em.getTransaction().commit();
        }
    }


    public void update(Shipment shipment) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(shipment);
            em.getTransaction().commit();
        }
    }


    public void delete(Shipment shipment) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Shipment shipmentIWishToDestroy = em.find(Shipment.class, shipment.getId());
            em.remove(shipmentIWishToDestroy);
            em.getTransaction().commit();
        }
    }
}
