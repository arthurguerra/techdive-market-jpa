package market.services;

import market.model.dao.ProductDAO;
import market.model.persistence.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;

public class ProductService {

    private final Logger LOG = LogManager.getLogger(ProductService.class);

    private EntityManager entityManager;

    private ProductDAO productDAO;

    public ProductService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.productDAO = new ProductDAO(entityManager);
    }

    public void create(Product product) {
        this.LOG.info("Preparando para a Criação de um Produto");
        if (product == null) {
            this.LOG.error("O produto informado está nulo!");
            throw new RuntimeException("O Produto Está nulo!");
        }
        try {
            product.setName(product.getName().toLowerCase());
            product.setDescription(product.getDescription().toLowerCase());
            product.getCategory().setName(product.getCategory().getName().toLowerCase());

            getBeginTransaction();
            this.productDAO.create(product);
            commitAndCloseTransaction();
        } catch (Exception e) {
            this.LOG.error("Erro ao criar um produto: "+e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Produto foi criado com sucesso!");
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
