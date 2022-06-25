package facade;

import beans.*;
import dao.CompaniesDAO;
import dao.CompaniesDBDAO;
import exceptions.CouponsException;
import exceptions.EnumException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyFacade extends ClientFacade {
    private int companyId;

    public CompanyFacade(String email, String password, int companyId) throws CouponsException {
        LoginManager loginManager = LoginManager.getInstance();
        this.CompanyFacade(((CompanyFacade) loginManager.login(email, password, ClientType.Company)), companyId);
    }

    private void CompanyFacade(CompanyFacade companyFacade, int companyId) throws CouponsException {
        try {
            this.companiesDAO = companyFacade.companiesDAO;
            this.customerDAO = companyFacade.customerDAO;
            this.couponsDAO = companyFacade.couponsDAO;
            this.companyId = companyId;
        } catch (Exception e) {
            System.out.println(e);
            throw new CouponsException(EnumException.NOT_AUTHORIZED);
        }
    }

    public CompanyFacade() {

    }

    @Override
    public boolean login(String email, String password) throws CouponsException {
        CompaniesDAO checkCompanyLogin = new CompaniesDBDAO();
        return (checkCompanyLogin.isCompanyExists(email, password));
    }

    public void addCoupon(Coupon coupon) throws CouponsException {
        if (coupon.getCompanyId() != this.companyId) {
            throw new CouponsException(EnumException.ADDING_COUPON_NOT_SAME_COMPANY);
        } else if (this.couponsDAO.isExistCouponByTitle(coupon.getCompanyId(), coupon.getTitle()))
            throw new CouponsException(EnumException.EXIST_TITLE_FOR_COMPANY);
        else
            this.couponsDAO.addCoupon(coupon);

    }

    public void updateCoupon(int couponId, Coupon coupon) throws CouponsException {
        Coupon exist = this.couponsDAO.getOneCoupon(couponId)
                .orElseThrow(() -> new CouponsException(EnumException.COUPON_DO_NOT_EXIST));
        if (coupon.getId() != couponId || coupon.getCompanyId() != exist.getCompanyId())
            throw new CouponsException(EnumException.UPDATE_COMPANY_ID_OR_COUPON_ID);
        else
            this.couponsDAO.updateCoupon(coupon);
    }

    public void deleteCoupon(int couponId) throws CouponsException {
        Coupon exist = this.couponsDAO.getOneCoupon(couponId)
                .orElseThrow(() -> new CouponsException(EnumException.COUPON_DO_NOT_EXIST));
        List<Customer> customersCoupons = this.customerDAO.getCustomersByCoupon(couponId);
        for (Customer customer : customersCoupons) {
            this.couponsDAO.deleteCouponPurchase(customer.getId(), couponId);
        }
    }

    public ArrayList<Coupon> getCompanyCoupons() throws CouponsException {
        return this.couponsDAO.getCouponsByCompany(this.companyId);
    }

    public ArrayList<Coupon> getCompanyCoupons(Category category) throws CouponsException {
        return this.couponsDAO.getCouponsByCategory(this.companyId, category);
    }

    public ArrayList<Coupon> getCompanyCoupons(Double maxPrice) throws CouponsException {
        return this.couponsDAO.getCouponsByMaxPrice(this.companyId, maxPrice);
    }

    public Optional<Company> getCompanyDetails() throws CouponsException {
        return this.companiesDAO.getOneCompany(this.companyId);
    }


}
