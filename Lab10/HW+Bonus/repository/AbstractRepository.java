package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.util.PersistenceUtil;
import org.example.util.LoggerUtil;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractRepository<T, ID> {
    private final Class<T> entityClass;
    private static final Logger logger = LoggerUtil.getLogger();

    public AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void create(T entity) {
        long startTime = System.currentTimeMillis();
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating entity: " + entity, e);
        } finally {
            em.close();
            long endTime = System.currentTimeMillis();
            logger.log(Level.INFO, "Execution time for create operation: " + (endTime - startTime) + " ms");
        }
    }

    public T findById(ID id) {
        long startTime = System.currentTimeMillis();
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(entityClass, id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error finding entity by ID: " + id, e);
            return null;
        } finally {
            em.close();
            long endTime = System.currentTimeMillis();
            logger.log(Level.INFO, "Execution time for findById operation: " + (endTime - startTime) + " ms");
        }
    }

    public List<T> findByName(String pattern, String namedQuery) {
        long startTime = System.currentTimeMillis();
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createNamedQuery(namedQuery, entityClass)
                    .setParameter("name", pattern)
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error finding entity by name: " + pattern, e);
            return null;
        } finally {
            em.close();
            long endTime = System.currentTimeMillis();
            logger.log(Level.INFO, "Execution time for findByName operation: " + (endTime - startTime) + " ms");
        }
    }
}
