package app.daos;

import app.entities.Package;
import app.persistence.HibernateConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PackageDAO implements IDAO<Package> {
    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public Package getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Package aPackage = em.find(Package.class, id);
            System.out.println(aPackage);
            return aPackage;
        }
    }

    public Set<Package> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Package> query = em.createQuery("SELECT p FROM Package p", Package.class);
            List<Package> packageList = query.getResultList();
            return packageList.stream().collect(Collectors.toSet());
        }
    }

    public void create(Package aPackage) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(aPackage);
            em.getTransaction().commit();
        }
    }

    public void update(Package aPackage) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(aPackage);
            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(Package aPackage) {

    }

    public Package getByTrackingNumber(String trackingNumber) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Package> query = em.createQuery("SELECT p FROM Package p WHERE p.trackingNr = :trackingNumber", Package.class);
            query.setParameter("trackingNumber", trackingNumber);
            System.out.println(query.getSingleResult());
            return query.getSingleResult();
        }
    }
/*
    public void updatePackageStatus(Integer pkgID, Package.deliveryStatus updatedStatus) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Package aPackage = em.find(Package.class,pkgID);
            aPackage.setStatus(updatedStatus);
            em.merge(aPackage);
            em.getTransaction().commit();
            System.out.println("Pakkens status med id " + pkgID + " er ændret til:  " + updatedStatus);
        }
    }
 */

    public void updatePackageStatus(String trackingNumber, Package.deliveryStatus updatedStatus) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Package aPackage = em.createQuery("SELECT p FROM Package p WHERE p.trackingNr = :trackingNumber", Package.class)
                    .setParameter("trackingNumber", trackingNumber)
                    .getSingleResult();
            aPackage.setStatus(updatedStatus);
            em.merge(aPackage);
            em.getTransaction().commit();
            System.out.println("Pakkens status med tracknr " + trackingNumber + " er ændret til:  " + updatedStatus);
        }
    }

    public void delete(String trackingNumber) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Package aPackage = em.createQuery("SELECT p FROM Package p WHERE p.trackingNr = :trackingNumber", Package.class)
                    .setParameter("trackingNumber", trackingNumber)
                    .getSingleResult();
            em.remove(aPackage);
            em.getTransaction().commit();
            System.out.println("pakken med tracknr " + trackingNumber + " er blevet destrueret");
        }
    }

}
