package ru.tele2.govorova.otus.java.pro.spring_context;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class Cart {
    private static final Logger logger = LogManager.getLogger(Cart.class);
    private final ProductRepository productRepository;
    private final List<Product> productsInCart = new ArrayList<>();

    @Autowired
    public Cart(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProductToCart(int id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            logger.error("Product with id {} doesn't exist", id);
        } else {
            productsInCart.add(product.get());
        }
    }

    public void removeProductFromCart(int id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            logger.error("Product with id {} doesn't exist", id);
        } else {
            if (productsInCart.contains(product.get())){
                productsInCart.remove(product.get());
                logger.info("Product with id {} was deleted from the cart", id);
            } else {
                logger.info("Product cart doesn't contain product with id {}", id);
            }
        }
    }
}
