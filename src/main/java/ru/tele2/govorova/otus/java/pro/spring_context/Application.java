package ru.tele2.govorova.otus.java.pro.spring_context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    private static final Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ContextConfig.class)) {
            ProductRepository products = context.getBean(ProductRepository.class);
            Cart firstCart = context.getBean(Cart.class);
            Cart secondCart = context.getBean(Cart.class);

            firstCart.addProductToCart(999);
            firstCart.addProductToCart(1);
            firstCart.addProductToCart(3);
            firstCart.addProductToCart(2);
            logger.info("First cart content: {}", firstCart);


            secondCart.addProductToCart(1);
            secondCart.addProductToCart(3);
            secondCart.addProductToCart(5);
            secondCart.removeProductFromCart(5);
            logger.info("Second cart content: {}", secondCart);



        }
    }
}
