package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import datatype.Marker;

public class MarkerRepository {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("MapPersistenceUnit");

    public Marker save(Marker marker) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(marker);
        em.getTransaction().commit();
        em.close();
        return marker;
    }

    public List<Marker> findByMapId(String mapId) {
        EntityManager em = emf.createEntityManager();
        List<Marker> markers = em.createQuery("SELECT m FROM Marker m WHERE m.mapId = :mapId", Marker.class)
                .setParameter("mapId", mapId)
                .getResultList();
        em.close();
        return markers;
    }

    public void delete(Marker marker) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Marker managedMarker = em.find(Marker.class, marker.getId());
        if (managedMarker != null) {
            em.remove(managedMarker);
        }
        em.getTransaction().commit();
        em.close();
    }

    public Marker findById(Long id) {
        EntityManager em = emf.createEntityManager();
        Marker marker = em.find(Marker.class, id);
        em.close();
        return marker;
    }
}