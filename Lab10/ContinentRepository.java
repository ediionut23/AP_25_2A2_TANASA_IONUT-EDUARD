package org.example.repository;

import org.example.model.Continent;
import org.example.util.PersistenceUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ContinentRepository {

    public void create(Continent continent) {
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(continent);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Continent findById(Long id) {
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Continent.class, id);
        } finally {
            em.close();
        }
    }

    public List<Continent> findByName(String namePattern) {
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createNamedQuery("Continent.findByName", Continent.class)
                    .setParameter("name", namePattern)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
