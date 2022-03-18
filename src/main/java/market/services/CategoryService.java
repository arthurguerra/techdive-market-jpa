package market.services;

import market.model.dao.CategoryDAO;
import market.model.persistence.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

public class CategoryService {

    private final Logger LOG = LogManager.getLogger(ProductService.class);

    private EntityManager entityManager;

    private CategoryDAO categoryDAO;

    public CategoryService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.categoryDAO = new CategoryDAO(entityManager);
    }

    public Category findByName(String name) {
        if (name == null || name.isEmpty()) {
            this.LOG.error("O Name não pode ser nulo!");
            throw new RuntimeException("The name is null!");
        }
        try {
            return this.categoryDAO.findByName(name);
        } catch (NoResultException e) {
            return null;
        }
    }

    public void delete(Long id) {

    }

    public Category getById(Long id) {
        if (id == null) {
            this.LOG.error("O ID está nulo");
            throw new RuntimeException("The Parameter is null!");
        }
        Category category= this.categoryDAO.getById(id);
        if (category == null) {
            this.LOG.error("Não foi encontrado a categoria de ID: " + id);
            throw new EntityNotFoundException("Category not found!");
        }
        return category;
    }

    private void getBeginTransaction() {
        this.LOG.info("Abrindo Transação com o banco de dados");
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        this.LOG.info("Commitando e Fechando transação com o banco de dados");
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
