package dao;


import beans.Category;
import beans.Coupon;
import exceptions.CouponsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CouponsDAO {
    void addCoupon(Coupon coupon) throws CouponsException;

    void updateCoupon(Coupon coupon) throws CouponsException;

    void deleteCoupon(int couponId) throws CouponsException;

    ArrayList<Coupon> getAllCoupons() throws CouponsException;

    ArrayList<Coupon> getCouponsByCompany(int companyId) throws CouponsException;

    ArrayList<Coupon> getCouponsByCustomer(int customerId) throws CouponsException;

    Optional<Coupon> getOneCoupon(int couponId) throws CouponsException;

    boolean isExistCouponByTitle(int companyId, String title) throws CouponsException;

    void addCouponPurchase(int customerId, int couponId) throws CouponsException;

    void deleteCouponPurchase(int customerId, int couponId) throws CouponsException;

    ArrayList<Coupon> getCouponsByCategory(int companyId, Category category) throws CouponsException;

    ArrayList<Coupon> getCouponsByMaxPrice(int companyId, Double maxPrice) throws CouponsException;

    ArrayList<Coupon> getCouponsByCustomerAndCategory(int customerId, Category category) throws CouponsException;

    ArrayList<Coupon> getCustomerCouponsByMaxPrice(int customerId, double maxPrice) throws CouponsException;

    ArrayList<Coupon> getAllExpiredCoupons() throws CouponsException;

    void deleteCouponCustomerById(int couponId) throws CouponsException;

    ArrayList<Coupon> getAllNonExpiredCoupons() throws CouponsException;

}
