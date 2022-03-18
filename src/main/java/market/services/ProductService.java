package market.services;

import market.model.dao.ProductDAO;
import market.model.persistence.Category;
import market.model.persistence.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private final Logger LOG = LogManager.getLogger(ProductService.class);

    private EntityManager entityManager;

    private ProductDAO productDAO;

    private CategoryService categoryService;

    public ProductService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.productDAO = new ProductDAO(entityManager);
        this.categoryService = new CategoryService(entityManager);
    }

    public void create(Product product) {
        this.LOG.info("Preparando para a Criação de um Produto");
        if (product == null) {
            this.LOG.error("O produto informado está nulo!");
            throw new RuntimeException("Product Null!");
        }
        String categoryName = product.getCategory().getName();
        this.LOG.info("Buscando se já existe a Categoria: " + categoryName);
        Category category = this.categoryService.findByName(categoryName);

        if (category != null) {
            this.LOG.info("Categoria '" + categoryName + "' encontrada no banco!");
            product.setCategory(category);
        }

        try {
            getBeginTransaction();
            this.productDAO.create(product);
            commitAndCloseTransaction();
        } catch (Exception e) {
            this.LOG.error("Erro ao criar um produto: " + e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Produto foi criado com sucesso!");
    }

    public void delete(Long id) {
        this.LOG.info("Preparando para buscar o Produto");
        if (id == null) {
            this.LOG.error("O ID do Produto informado está nulo!");
            throw new RuntimeException("The ID is Null");
        }
        Product product = this.productDAO.getById(id);
        validateProductIsNull(product);
        this.LOG.info("Produto encontrado com sucesso!");
        getBeginTransaction();
        this.productDAO.delete(product);
        commitAndCloseTransaction();
        this.LOG.info("Produto deletado com sucesso!");
    }

    public void update(Product newProduct, Long productId) {
        this.LOG.info("Preparando para atualizar o Produto");
        if (newProduct == null || productId == null) {
            this.LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("The parameter is null!");
        }

        Product product = this.productDAO.getById(productId);

        validateProductIsNull(product);
        this.LOG.info("Produto encontrado no banco");

        getBeginTransaction();

        product.setName(newProduct.getName());
        product.setDescription(newProduct.getDescription());
        product.setPrice(newProduct.getPrice());
        product.setCategory(this.categoryService.findByName(newProduct.getCategory().getName()));

        commitAndCloseTransaction();
        this.LOG.info("Produto atualizado com sucesso!");
    }

    public List<Product> listAll() {
        this.LOG.info("Preparando para listar Produtos");
        List<Product> products = this.productDAO.listAll();

        if (products == null) {
            this.LOG.info("Não foram encontrados Produtos");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados "+ products.size() + " Produtos.");
        return products;
    }

    public List<Product> listByName(String name) {
        if(name == null || name.isEmpty()) {
            this.LOG.info("O parâmetro nome está vazio!");
            throw new RuntimeException("The parameter name is null");
        }

        this.LOG.info("Preparando para buscar Produtos com o nome: " +name);
        List<Product> products = this.productDAO.listByName(name);

        if (products == null) {
            this.LOG.info("Não foram encontrados Produtos");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados "+ products.size() + " Produtos com o nome "+name);
        return products;
    }

    private void validateProductIsNull(Product product) {
        if (product == null) {
            this.LOG.error("O Produto não Existe!");
            throw new EntityNotFoundException("Product not found!");
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
