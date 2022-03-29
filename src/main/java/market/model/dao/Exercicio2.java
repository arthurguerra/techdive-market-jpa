package market.model.dao;

import javax.persistence.EntityManager;

public class Exercicio2 {

    private EntityManager entityManager;

    public Exercicio2(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Object object) {
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(object);
        this.entityManager.getTransaction().commit();
        this.entityManager.close();
    }
}
