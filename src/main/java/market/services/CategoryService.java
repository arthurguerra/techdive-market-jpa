package market.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;

public class CategoryService {

    private final Logger LOG = LogManager.getLogger(ProductService.class);

    private EntityManager entityManager;

    public CategoryService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
