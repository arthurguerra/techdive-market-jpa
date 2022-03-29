package market.services;

import market.model.dao.CategoryDAO;
import market.model.persistence.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    private final Logger LOG = LogManager.getLogger(CategoryService.class);

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

    public void update(Category newCategory, Long categoryId) {
        this.LOG.info("Preparando para atualizar Categoria");
        if (newCategory == null || categoryId == null) {
            this.LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("The parameter is null!");
        }

        Category category = this.categoryDAO.getById(categoryId);

        validateCategoryIsNull(category);
        this.LOG.info("Categoria encontrada no banco");

        getBeginTransaction();

        category.setName(newCategory.getName());

        commitAndCloseTransaction();
        this.LOG.info("Categoria atualizada com sucesso!");
    }

    public List<Category> listAll() {
        this.LOG.info("Preparando para listar Categorias");
        List<Category> categories = this.categoryDAO.listAll();

        if (categories == null) {
            this.LOG.info("Não foram encontradas Categorias");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontradas " + categories.size() + " Categorias.");
        return categories;
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

    private void validateCategoryIsNull(Category category) {
        if (category == null) {
            this.LOG.error("A Categoria não existe!");
            throw new EntityNotFoundException("Category Not Found!");
        }
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
