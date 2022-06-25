package dao;

import beans.Category;
import beans.Coupon;
import db.JDBCUtils;
import db.ResultsUtils;
import exceptions.CouponsException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class CouponsDBDAO implements CouponsDAO {

    private static final String QUERY_INSERT_COUPONS = "INSERT INTO `Coupons_Project`.`coupons` (`id`, `company_Id`, `category_Id`, `title` , `description`, `start_Date`, `end_Date` , `amount`, `price`, `image`) VALUES (?, ?, ?, ? , ? ,?, ?, ?, ? , ?);";
    private static final String QUERY_UPDATE = "UPDATE `Coupons_Project`.`coupons` SET `id` = ?, `company_Id` = ?, `category_Id` = ?, `title` = ? , `description` = ?, `start_Date` = ?, `end_Date` = ?, `amount` = ? , `price` = ?, `image` = ? WHERE (`id` = ?);\n";
    private static final String QUERY_DELETE_COUPONS = "DELETE FROM `Coupons_Project`.`coupons` WHERE (`id` = ?);";
    private static final String QUERY_GET_ALL = "SELECT * FROM `Coupons_Project`.`coupons`;";
    private static final String QUERY_GET_ONE = "SELECT * FROM `Coupons_Project`.`coupons` WHERE (`id` = ?);";
    private static final String QUERY_INSERT_CUSTOMERS_COUPONS = "INSERT INTO `Coupons_Project`.`customers_coupons` (`CUSTOMER_ID`, `COUPON_ID`) VALUES (?, ?);";
    private static final String QUERY_DELETE_CUSTOMERS_COUPONS = "DELETE FROM `Coupons_Project`.`customers_coupons` WHERE (`customer_Id` = ? ) AND (`coupons_Id` = ? );";
    private static final String QUERY_GET_COUPONS_BY_COMPANY = "SELECT * FROM `Coupons_Project`.`coupons` WHERE (`COMPANY_ID` = ?);";
    private static final String QUERY_GET_COUPONS_BY_CUSTOMER = "SELECT * FROM coupons_project.coupons\n" +
            "where id in(\n" +
            "\tselect coupon_id from coupons_project.customers_coupons\n" +
            "    where CUSTOMER_ID = ?);";
    private static final String QUERY_GET_COUPON_BY_TITLE = "SELECT * FROM `Coupons_Project`.`coupons` WHERE (`Company_ID` = ?) AND ('TITLE' = ?);";
    private static final String QUERY_GET_COUPONS_BY_CATEGORY = "SELECT * FROM coupons_project.coupons where (CATEGORY_ID = ?) and (COMPANY_ID = ?);";
    private static final String QUERY_GET_COUPONS_BY_MAX_PRICE = "SELECT * FROM `Coupons_Project`.`coupons` WHERE (COMPANY_ID = ?) AND (PRICE  <= ?);";
    private static final String QUERY_GET_COUPONS_BY_CUSTOMER_MAX_PRICE = "SELECT * FROM coupons_project.coupons\n" +
            "            where PRICE <= ? and id in(\n" +
            "            select coupon_id from coupons_project.customers_coupons\n" +
            "                where CUSTOMER_ID = ?);";
    private static final String QUERY_GET_COUPONS_BY_CUSTOMER_CATEGORY = "SELECT * FROM coupons_project.coupons\n" +
            "            where CATEGORY_ID = ? and id in(\n" +
            "            select coupon_id from coupons_project.customers_coupons\n" +
            "                where CUSTOMER_ID = ?);";
    private static final String QUERY_IS_EXISTS_COMPANY_TITLE =
            "select exists (SELECT * FROM `Coupons_Project`.`coupons` where company_id = ? and title = ?) as res;";
    private static final String QUERY_GET_EXPIRED_COUPONS = "SELECT * FROM `Coupons_Project`.`coupons` WHERE (`END_DATE` < ?);";
    private static final String QUERY_DELETE_JOIN_BY_COUPON = "DELETE FROM `Coupons_Project`.`customers_coupons` WHERE (`coupon_id` = ?);";
    private static final String QUERY_GET_UNEXPIRED_COUPONS = "SELECT * FROM `Coupons_Project`.`coupons` WHERE (`END_DATE` >= ?);";

    @Override
    public void addCoupon(Coupon coupon) throws CouponsException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, coupon.getId());
        params.put(2, coupon.getCompanyId());
        params.put(3, Category.getValue(coupon.getCategory()));
        params.put(4, coupon.getTitle());
        params.put(5, coupon.getDescription());
        params.put(6, coupon.getStartDate());
        params.put(7, coupon.getEndDate());
        params.put(8, coupon.getAmount());
        params.put(9, coupon.getPrice());
        params.put(10, coupon.getImage());
        JDBCUtils.executeQuery(QUERY_INSERT_COUPONS, params);
    }

    @Override
    public void updateCoupon(Coupon coupon) throws CouponsException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, coupon.getId());
        params.put(2, coupon.getCompanyId());
        params.put(3, Category.getValue(coupon.getCategory()));
        params.put(4, coupon.getTitle());
        params.put(5, coupon.getDescription());
        params.put(6, coupon.getStartDate());
        params.put(7, coupon.getEndDate());
        params.put(8, coupon.getAmount());
        params.put(9, coupon.getPrice());
        params.put(10, coupon.getImage());
        params.put(11, coupon.getId());
        JDBCUtils.executeQuery(QUERY_UPDATE, params);
    }

    @Override
    public void deleteCoupon(int couponId) throws CouponsException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, couponId);
        JDBCUtils.executeQuery(QUERY_DELETE_COUPONS, params);
    }

    @Override
    public ArrayList<Coupon> getAllCoupons() throws CouponsException {
        ArrayList<Coupon> results = new ArrayList<>();

        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_ALL);
        for (Object object : rows) {
            results.add(ResultsUtils.couponFromRow((HashMap<String, Object>) object));
        }
        return results;
    }

    @Override
    public ArrayList<Coupon> getCouponsByCompany(int companyId) throws CouponsException {
        ArrayList<Coupon> results = new ArrayList<>();
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, companyId);
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_COUPONS_BY_COMPANY, params);
        for (Object object : rows) {
            results.add(ResultsUtils.couponFromRow((HashMap<String, Object>) object));
        }
        return results;
    }

    @Override
    public ArrayList<Coupon> getCouponsByCustomer(int customerId) throws CouponsException {
        ArrayList<Coupon> results = new ArrayList<>();
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, customerId);
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_COUPONS_BY_CUSTOMER, params);
        for (Object object : rows) {
            results.add(ResultsUtils.couponFromRow((HashMap<String, Object>) object));
        }
        return results;
    }

    @Override
    public Optional<Coupon> getOneCoupon(int couponId) throws CouponsException {
        Coupon result = null;
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, couponId);
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_ONE, params);
        if (rows.isEmpty()) {
            result = null;
        } else {
            result = ResultsUtils.couponFromRow((HashMap<String, Object>) (rows.get(0)));
        }
        return Optional.ofNullable(result);
    }

    @Override
    public boolean isExistCouponByTitle(int companyId, String title) throws CouponsException {
        boolean result = false;
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, companyId);
        params.put(2, title);
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_IS_EXISTS_COMPANY_TITLE, params);
        result = ResultsUtils.booleanFromRow((HashMap<String, Object>) rows.get(0));
        return result;
    }

    @Override
    public void addCouponPurchase(int customerId, int couponId) throws CouponsException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, customerId);
        params.put(2, couponId);
        JDBCUtils.executeQuery(QUERY_INSERT_CUSTOMERS_COUPONS, params);

    }

    @Override
    public void deleteCouponPurchase(int customerId, int couponId) throws CouponsException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, customerId);
        params.put(2, couponId);
        JDBCUtils.executeQuery(QUERY_DELETE_CUSTOMERS_COUPONS, params);
    }

    @Override
    public ArrayList<Coupon> getCouponsByCategory(int companyId, Category category) throws CouponsException {
        ArrayList<Coupon> results = new ArrayList<>();
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, companyId);
        params.put(2, Category.getValue(category));
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_COUPONS_BY_CATEGORY, params);
        for (Object object : rows) {
            results.add(ResultsUtils.couponFromRow((HashMap<String, Object>) object));
        }
        return results;
    }

    @Override
    public ArrayList<Coupon> getCouponsByMaxPrice(int companyId, Double maxPrice) throws CouponsException {
        ArrayList<Coupon> results = new ArrayList<>();
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, companyId);
        params.put(2, maxPrice);
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_COUPONS_BY_MAX_PRICE, params);
        for (Object object : rows) {
            results.add(ResultsUtils.couponFromRow((HashMap<String, Object>) object));
        }
        return results;
    }

    @Override
    public ArrayList<Coupon> getCouponsByCustomerAndCategory(int customerId, Category category) throws CouponsException {
        ArrayList<Coupon> results = new ArrayList<>();
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, customerId);
        params.put(2, Category.getValue(category));
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_COUPONS_BY_CUSTOMER_CATEGORY, params);
        for (Object object : rows) {
            results.add(ResultsUtils.couponFromRow((HashMap<String, Object>) object));
        }
        return results;
    }

    @Override
    public ArrayList<Coupon> getCustomerCouponsByMaxPrice(int customerId, double maxPrice) throws CouponsException {
        ArrayList<Coupon> results = new ArrayList<>();
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, maxPrice);
        params.put(2, customerId);
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_COUPONS_BY_CUSTOMER_MAX_PRICE, params);
        for (Object object : rows) {
            results.add(ResultsUtils.couponFromRow((HashMap<String, Object>) object));
        }
        return results;
    }

    @Override
    public ArrayList<Coupon> getAllExpiredCoupons() throws CouponsException {
        ArrayList<Coupon> results = new ArrayList<>();
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, Date.valueOf(LocalDate.now()));
        ;
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_EXPIRED_COUPONS, params);
        for (Object object : rows) {
            results.add(ResultsUtils.couponFromRow((HashMap<String, Object>) object));
        }
        return results;
    }

    @Override
    public void deleteCouponCustomerById(int couponId) throws CouponsException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, couponId);
        JDBCUtils.executeQuery(QUERY_DELETE_JOIN_BY_COUPON, params);
    }

    @Override
    public ArrayList<Coupon> getAllNonExpiredCoupons() throws CouponsException {
        ArrayList<Coupon> results = new ArrayList<>();
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, Date.valueOf(LocalDate.now()));
        ;
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_UNEXPIRED_COUPONS, params);
        for (Object object : rows) {
            results.add(ResultsUtils.couponFromRow((HashMap<String, Object>) object));
        }
        return results;

    }


}
