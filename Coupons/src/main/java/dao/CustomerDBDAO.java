package dao;

import beans.Customer;
import db.JDBCUtils;
import db.ResultsUtils;
import exceptions.CouponsException;

import java.util.*;


public class CustomerDBDAO implements CustomerDAO {

    private static final String QUERY_INSERT = "INSERT INTO `Coupons_Project`.`customers` (`id`, `first_Name`, `last_Name`, `email`, `password`) VALUES (?, ?, ?, ?, ?);";
    private static final String QUERY_UPDATE = "UPDATE `Coupons_Project`.`customers` SET `id` = ?, `firstName` = ?, `lastName` = ?, `email` = ?, `password` = ? WHERE (`id` = ?);\n";
    private static final String QUERY_DELETE = "DELETE FROM `Coupons_Project`.`customers` WHERE (`id` = ?);";
    private static final String QUERY_GET_ALL = "SELECT * FROM `Coupons_Project`.`customers`;";
    private static final String QUERY_GET_ONE = "SELECT * FROM `Coupons_Project`.`customers` WHERE (`id` = ?);";
    private static final String QUERY_IS_EXISTS = "select exists (SELECT * FROM `Coupons_Project`.`customers` where id=?) as res;";
    private static final String QUERY_IS_EXISTS_EMAIL_PASSWORD = "select exists (SELECT * FROM `Coupons_Project`.`customers` where EMAIL = ? AND PASSWORD = ?) as res;";
    private static final String QUERY_GET_CUSTOMER_BY_COUPON = "SELECT * FROM `Coupons_Project`.`customers` WHERE (`COUPON_ID` = ?);";
    private static final String QUERY_GET_CUSTOMER_BY_EMAIL = "SELECT * FROM `Coupons_Project`.`customers` WHERE (`EMAIL` = ?);";
    private static final String QUERY_IS_PURCHASE_EXISTS = "SELECT exists (SELECT * FROM `Coupons_Project`.`customers_coupons` WHERE (CUSTOMER_ID = ?) AND (COUPON_ID = ?)) as res;";

    @Override
    public boolean isCustomerExists(String email, String password) throws CouponsException {
        boolean result = false;
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, email);
        params.put(2, password);
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_IS_EXISTS_EMAIL_PASSWORD, params);
        result = ResultsUtils.booleanFromRow((HashMap<String, Object>) rows.get(0));
        return result;
    }

    @Override
    public void addCustomer(Customer customer) throws CouponsException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, customer.getId());
        params.put(2, customer.getFirstName());
        params.put(3, customer.getLastName());
        params.put(4, customer.getEmail());
        params.put(5, customer.getPassword());
        JDBCUtils.executeQuery(QUERY_INSERT, params);
    }

    @Override
    public void updateCustomer(Customer customer) throws CouponsException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, customer.getId());
        params.put(2, customer.getFirstName());
        params.put(3, customer.getLastName());
        params.put(4, customer.getEmail());
        params.put(5, customer.getPassword());
        params.put(6, customer.getId());
        JDBCUtils.executeQuery(QUERY_UPDATE, params);
    }

    @Override
    public void deleteCustomer(int customerId) throws CouponsException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, customerId);
        JDBCUtils.executeQuery(QUERY_DELETE, params);
    }

    @Override
    public ArrayList<Customer> getAllCustomers() throws CouponsException {
        ArrayList<Customer> customers = new ArrayList<>();

        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_ALL);
        for (Object object : rows) {
            customers.add(ResultsUtils.customerFromRow((HashMap<String, Object>) object));
        }

        return customers;
    }

    @Override
    public ArrayList<Customer> getCustomersByCoupon(int couponId) throws CouponsException {
        ArrayList<Customer> results = new ArrayList<>();
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, couponId);
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_CUSTOMER_BY_COUPON, params);
        for (Object object : rows) {
            results.add(ResultsUtils.customerFromRow((HashMap<String, Object>) object));
        }
        return results;
    }

    @Override
    public Optional<Customer> getOneCustomer(int customerId) throws CouponsException {
        Customer result = null;
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, customerId);
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_ONE, params);
        if (rows.isEmpty()) {
            result = null;
        } else {
            result = ResultsUtils.customerFromRow((HashMap<String, Object>) (rows.get(0)));
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Customer getCustomerByCoupon(int couponId) throws CouponsException {
        Customer result = null;
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, couponId);
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_CUSTOMER_BY_COUPON, params);
        if (rows.isEmpty()) {
            result = null;
        } else {
            result = ResultsUtils.customerFromRow((HashMap<String, Object>) (rows.get(0)));
        }
        return result;
    }

    @Override
    public Customer getCustomerByEmail(String email) throws CouponsException {
        Customer result = null;
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, email);
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_CUSTOMER_BY_EMAIL, params);
        if (rows.isEmpty()) {
            result = null;
        } else {
            result = ResultsUtils.customerFromRow((HashMap<String, Object>) (rows.get(0)));
        }
        return result;
    }

    @Override
    public boolean isAlreadyPurchase(int customerId, int couponId) throws CouponsException {
        boolean result = false;
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, customerId);
        params.put(2, couponId);
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_IS_PURCHASE_EXISTS, params);
        result = ResultsUtils.booleanFromRow((HashMap<String, Object>) rows.get(0));
        return result;
    }

}
