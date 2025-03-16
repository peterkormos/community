package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import datatype.Map;

public class MapRepository {

	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("MapPersistenceUnit");

	public Map save(Map map) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(map);
		em.getTransaction().commit();
		em.close();
		return map;
	}

	public Map findByMapId(String mapId) {
		EntityManager em = emf.createEntityManager();
		Map map = em.createQuery("SELECT m FROM Map m WHERE m.mapId = :mapId", Map.class).setParameter("mapId", mapId)
				.getSingleResult();
		em.close();
		return map;
	}

	public void delete(Map map) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.remove(map);
		em.getTransaction().commit();
		em.close();
	}

	public List<Map> findAll() {
		EntityManager em = emf.createEntityManager();
		List<Map> maps = em.createQuery("SELECT m FROM Map m", Map.class).getResultList();
		em.close();
		return maps;
	}

	public List<Map> findByEmailAddress(String emailAddress) {
		EntityManager em = emf.createEntityManager();
		List<Map> maps = em.createQuery("SELECT m FROM Map m WHERE m.emailAddress = :emailAddress", Map.class)
				.setParameter("emailAddress", emailAddress).getResultList();
		em.close();
		return maps;
	}
}