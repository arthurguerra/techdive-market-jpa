package market.model.dao;

import market.model.persistence.Product;

import javax.persistence.EntityManager;

public class ProductDAO {

    private EntityManager entityManager;

    public ProductDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Product product) {
        this.entityManager.persist(product);
    }
}
