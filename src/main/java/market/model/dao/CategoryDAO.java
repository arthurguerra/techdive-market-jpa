package market.model.dao;

import market.model.persistence.Category;
import market.model.persistence.Product;

import javax.persistence.EntityManager;

public class CategoryDAO {

    private EntityManager entityManager;

    public CategoryDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Category findByName(String name) {
        String jpql = "SELECT c FROM Category as c WHERE c.name =:name";
        return (Category) this.entityManager.createQuery(jpql, Category.class)
                .setParameter("name", name)
                .getSingleResult();
    }

}
