package facade;

import beans.Category;
import beans.ClientType;
import beans.Coupon;
import beans.Customer;
import dao.CustomerDAO;
import dao.CustomerDBDAO;
import exceptions.CouponsException;
import exceptions.EnumException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class CustomerFacade extends ClientFacade {

    private int customerId;

    public CustomerFacade(String email, String password, int customerId) throws CouponsException {
        LoginManager loginManager = LoginManager.getInstance();
        this.CustomerFacade(((CustomerFacade) loginManager.login(email, password, ClientType.Customer)), customerId);
    }

    private void CustomerFacade(CustomerFacade customerFacade, int customerId) throws CouponsException {
        try {
            this.companiesDAO = customerFacade.companiesDAO;
            this.customerDAO = customerFacade.customerDAO;
            this.couponsDAO = customerFacade.couponsDAO;
            this.customerId = customerId;
        } catch (Exception e) {
            System.out.println(e);
            throw new CouponsException(EnumException.NOT_AUTHORIZED);
        }
    }

    public CustomerFacade() {

    }

    @Override
    public boolean login(String email, String password) throws CouponsException {
        CustomerDAO checkCustomerLogin = new CustomerDBDAO();
        return checkCustomerLogin.isCustomerExists(email, password);

    }

    public void purchaseCoupon(Coupon coupon) throws CouponsException {
        Optional<Coupon> exist = Optional.ofNullable(couponsDAO.getOneCoupon(coupon.getId())
                .orElseThrow(() -> new CouponsException(EnumException.COUPON_DO_NOT_EXIST)));
        if (checkPurchase(exist.get().getId(), this.customerId))
            System.out.println(EnumException.COUPON_ALREADY_PURCHASE.getMsg());
        else if (exist.get().getAmount() < 1)
            System.out.println(EnumException.COUPON_RUN_OFF.getMsg());
        else if (exist.get().getEndDate().before(new Date(System.currentTimeMillis())))
            System.out.println(EnumException.COUPON_EXPIRED.getMsg());
        else {
            this.couponsDAO.addCouponPurchase(this.customerId, coupon.getId());
            coupon.setAmount(coupon.getAmount() - 1);
            couponsDAO.updateCoupon(coupon);
        }
    }

    private boolean checkPurchase(int couponId, int customerId) throws CouponsException {
        return this.customerDAO.isAlreadyPurchase(customerId, couponId);
    }

    public ArrayList<Coupon> getCustomerCoupons() throws CouponsException {
        return this.couponsDAO.getCouponsByCustomer(this.customerId);
    }

    public ArrayList<Coupon> getCustomerCoupons(Category category) throws CouponsException {
        return this.couponsDAO.getCouponsByCustomerAndCategory(this.customerId, category);
    }

    public ArrayList<Coupon> getCustomerCoupons(double maxPrice) throws CouponsException {
        return this.couponsDAO.getCustomerCouponsByMaxPrice(this.customerId, maxPrice);
    }

    public Optional<Customer> getCustomerDetails() throws CouponsException {
        return this.customerDAO.getOneCustomer(this.customerId);
    }

}
