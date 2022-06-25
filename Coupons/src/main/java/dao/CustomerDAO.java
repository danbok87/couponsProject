package dao;

import beans.Customer;
import exceptions.CouponsException;

import java.util.ArrayList;
import java.util.Optional;


public interface CustomerDAO {
    boolean isCustomerExists(String email, String password) throws CouponsException;

    void addCustomer(Customer customer) throws CouponsException;

    void updateCustomer(Customer customer) throws CouponsException;

    void deleteCustomer(int customerId) throws CouponsException;

    ArrayList<Customer> getAllCustomers() throws CouponsException;

    ArrayList<Customer> getCustomersByCoupon(int couponId) throws CouponsException;

    Optional<Customer> getOneCustomer(int customerId) throws CouponsException;

    Customer getCustomerByCoupon(int couponId) throws CouponsException;

    Customer getCustomerByEmail(String email) throws CouponsException;

    boolean isAlreadyPurchase(int customerId, int couponId) throws CouponsException;
}
