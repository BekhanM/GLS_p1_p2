package app.daos;

import app.entities.Location;
import app.entities.Package;
import app.persistence.HibernateConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LocationDAO {
    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();



    public Location getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Location.class,id);
        }
    }


    public Set<Location> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Location> query = em.createQuery("SELECT l FROM Location l", Location.class);
            List<Location> packageList = query.getResultList();
            return packageList.stream().collect(Collectors.toSet());
        }
    }




    public void create(Location location) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(location);
            em.getTransaction().commit();
        }
    }


    public void update(Location location) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(location);
            em.getTransaction().commit();
        }
    }


    public void delete(Location location) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Location locationIWishToDestroy = em.find(Location.class,location.getId());
            em.remove(locationIWishToDestroy);
            em.getTransaction().commit();
        }
    }
}
