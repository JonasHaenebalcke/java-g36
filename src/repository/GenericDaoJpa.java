package repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import domein.GebruikerController;

import javax.persistence.*;
import java.util.*;

public class GenericDaoJpa<T> implements GenericDao<T> {
    private static final String PU_NAME = "ItLabTest";
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
    protected static final EntityManager em = emf.createEntityManager();
    private final Class<T> type;
    private static GebruikerController gc = new GebruikerController();
    
    public GenericDaoJpa(Class<T> type) {
        this.type = type;		
    }
    public static void closePersistency() {
        em.close();
        emf.close();
    }
    public static void startTransaction() {
        em.getTransaction().begin();
        gc.startTransaction();
    }
    public static void commitTransaction() {
        em.getTransaction().commit();
        gc.comittTransaction();
    }
    public static void rollbackTransaction() {
        em.getTransaction().rollback();
    }

    @Override
    public List<T> findAll() {
        //return em.createNamedQuery(type.getName()+".findAll", type).getResultList();
        return em.createQuery("select entity from " + type.getName() + " entity", type).getResultList();
    }

    @Override
    public <U> T get(U id) {
        T entity = em.find(type, id);
        return entity;
    }

    @Override
    public T update(T object) {
        return em.merge(object);
    }

    @Override
    public void delete(T object) {
        em.remove(em.merge(object));
    }

    @Override
    public void insert(T object) {
        em.persist(object);
    }

    @Override
    public <U> boolean exists(U id) {
        T entity = em.find(type, id);
        return entity != null;
    } 
    
}
