package market.application;

import market.connection.JpaConnectionFactory;
import market.model.persistence.Category;
import market.model.persistence.Product;
import market.services.CategoryService;
import market.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class Program {

    private static final Logger LOG = LogManager.getLogger(Program.class);

    public static void main(String[] args) {

        EntityManager entityManager = new JpaConnectionFactory().getEntityManager();
        ProductService productService = new ProductService(entityManager);
        CategoryService categoryService = new CategoryService(entityManager);

        Product product = new Product("Cheetos", "Cheddar 90g"
                , new BigDecimal("6.99")
                , new Category("Alimento"));

//        productService.create(product);
//        productService.delete(6L);
//        productService.delete(7L);
//        Category category = categoryService.findByName("Alimentos");
//        System.out.println(category);
//        productService.update(product, 8L);
        List<Product> products = productService.listByName("Cheetos");
        products.forEach(System.out::println);
    }
}
