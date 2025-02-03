package ru.tele2.govorova.otus.java.pro.spring_context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private static final Logger logger = LogManager.getLogger(ProductRepository.class);
    private final List<Product> products = new ArrayList<>();

    public ProductRepository() {
        for (int i = 0; i < 10; i++) {
            this.products.add(new Product(i, "Product " + i, BigDecimal.valueOf(i * 10)));
        }
        logger.info("Product repository was initialized, products were added.");
    }

    public Optional<Product> findById(int id) {
        return products.stream().filter(product -> product.id() == id).findFirst();
    }

    public List<Product> findAll() {
        return products;
    }
}
