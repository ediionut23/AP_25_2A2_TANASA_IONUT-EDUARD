package org.example.repository;

import org.example.model.City;
import jakarta.persistence.EntityManager;
import org.example.util.PersistenceUtil;

import java.util.List;

public class JpaCityRepository implements Repository<City, Long> {
    private EntityManager em;

    public JpaCityRepository() {
        this.em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
    }

    @Override
    public void create(City entity) {
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public City findById(Long id) {
        return em.find(City.class, id);
    }

    @Override
    public List<City> findByName(String pattern) {
        return em.createNamedQuery("City.findByName", City.class)
                .setParameter("name", pattern)
                .getResultList();
    }
}
