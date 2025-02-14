package ru.tele2.govorova.otus.java.pro.hibernate.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.tele2.govorova.otus.java.pro.hibernate.dao.CustomerDAO;
import ru.tele2.govorova.otus.java.pro.hibernate.dao.ProductDAO;
import ru.tele2.govorova.otus.java.pro.hibernate.entity.Customer;
import ru.tele2.govorova.otus.java.pro.hibernate.entity.Product;
import ru.tele2.govorova.otus.java.pro.hibernate.entity.Purchase;


import java.util.HashSet;
import java.util.Set;

public class PurchaseService {
    private final SessionFactory sessionFactory;
    private final CustomerDAO customerDAO;
    private final ProductDAO productDAO;

    public PurchaseService(SessionFactory sessionFactory, CustomerDAO customerDAO, ProductDAO productDAO) {
        this.sessionFactory = sessionFactory;
        this.customerDAO = customerDAO;
        this.productDAO = productDAO;
    }

    public void viewCustomerPurchases(Long customerId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Customer customer = session.get(Customer.class, customerId);
            if (customer != null) {
                for (Purchase purchase : customer.getPurchases()) {
                    System.out.println("Товары пользователя " + customer.getName() + ":");
                    System.out.println(purchase.getProduct().getName() + " - " + purchase.getPurchasePrice());
                }
            } else {
                System.out.println("Клиент не найден");
            }
            transaction.commit();
        }
    }

    public Set<Customer> findCustomersByProduct(Long productId) {
        Set<Customer> customers;
        try (Session session = sessionFactory.openSession()) {
            Product product = session.get(Product.class, productId);
            if (product == null) {
                return Set.of();
            }

            customers = new HashSet<>();

            for (Purchase purchase : product.getPurchaseList()) {
                customers.add(purchase.getCustomer());
            }
        }
        return customers;
    }
}
