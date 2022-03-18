package market.model.dao;

import market.model.persistence.Category;
import market.model.persistence.Product;

import javax.persistence.EntityManager;
import java.util.List;

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

    public void delete(Category category) {
        entityManager.remove(category);
    }

    public Category getById(Long id) {
        return this.entityManager.find(Category.class, id);
    }

    public Category update(Category category) {
        return convertToMerge(category);
    }

    @SuppressWarnings("unchecked")
    public List<Category> listAll() {
        String sql = "SELECT * FROM Category";
        return this.entityManager.createNativeQuery(sql, Category.class)
                .getResultList();
    }

    private Category convertToMerge(Category category) {
        return this.entityManager.merge(category);
    }
}
