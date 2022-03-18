package market.model.dao;

import market.model.persistence.Product;

import javax.persistence.EntityManager;
import java.util.List;

public class ProductDAO {

    private EntityManager entityManager;

    public ProductDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Product product) {
        this.entityManager.persist(product);
    }

    public void delete(Product product) {
        this.entityManager.remove(convertToMerge(product));
    }

    public Product getById(Long id) {
        return this.entityManager.find(Product.class, id);
    }

    public Product update(Product product) {
        return convertToMerge(product);
    }

    public List<Product> listAll() {
        String sql = "SELECT * FROM Product";
        return this.entityManager.createNativeQuery(sql, Product.class)
                .getResultList();
    }

    private Product convertToMerge(Product product) {
        return this.entityManager.merge(product);
    }

}
