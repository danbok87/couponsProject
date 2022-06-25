package facade;

import beans.ClientType;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import dao.CompaniesDAO;
import dao.CouponsDAO;
import dao.CustomerDAO;
import exceptions.CouponsException;
import exceptions.EnumException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminFacade extends ClientFacade {

    public AdminFacade(String email, String password) throws CouponsException {
        LoginManager loginManager = LoginManager.getInstance();
        this.AdminFacade((AdminFacade) loginManager.login(email, password, ClientType.Administrator));
    }

    private void AdminFacade(AdminFacade adminFacade) throws CouponsException {
        try {
            this.companiesDAO = adminFacade.companiesDAO;
            this.customerDAO = adminFacade.customerDAO;
            this.couponsDAO = adminFacade.couponsDAO;
        } catch (Exception e) {
            System.out.println(e);
            throw new CouponsException(EnumException.NOT_AUTHORIZED);
        }
    }

    public AdminFacade() {

    }

    @Override
    public boolean login(String email, String password) {
        return ((email.equals("admin@admin.com")) && (password.equals("admin")));
    }

    public void addCompany(Company company) throws CouponsException {
        if (companiesDAO.isCompanyExistsEmailName(company.getEmail(), company.getName()))
            throw new CouponsException(EnumException.EXIST_NAME_EMAIL);
        else
            companiesDAO.addCompany(company);

    }

    public void updateCompany(int companyId, Company company) throws CouponsException {
        Company exist = companiesDAO.getOneCompany(companyId)
                .orElseThrow(() -> new CouponsException(EnumException.COMPANY_DO_NOT_EXIST));
        if (company.getId() != companyId || !company.getName().equals(exist.getName()))
            throw new CouponsException(EnumException.SQL_ERROR);
        else
            companiesDAO.updateCompany(company);


    }

    public void deleteCompany(int companyId) throws CouponsException {
        Company exist = companiesDAO.getOneCompany(
                        companyId)
                .orElseThrow(() -> new CouponsException(EnumException.COMPANY_DO_NOT_EXIST));
        List<Coupon> companyCoupons = couponsDAO.getCouponsByCompany(companyId);
        for (Coupon coupon : companyCoupons) {
            int customerId = customerDAO.getCustomerByCoupon(coupon.getId()).getId();
            couponsDAO.deleteCouponPurchase(customerId, coupon.getId());
        }
        companiesDAO.deleteCompany(companyId);


    }

    public ArrayList<Company> getAllCompanies() throws CouponsException {
        return companiesDAO.getAllCompanies();
    }

    public Optional<Company> getOneCompany(int companyId) throws CouponsException {
        return Optional.ofNullable(
                companiesDAO.getOneCompany(companyId)
                        .orElseThrow(() -> new CouponsException(EnumException.COMPANY_DO_NOT_EXIST)));
    }

    public void addCustomer(Customer customer) throws CouponsException {
        Customer exist = customerDAO.getCustomerByEmail(customer.getEmail());
        if (exist != null)
            throw new CouponsException(EnumException.EXIST_CUSTOMER_EMAIL);
        else
            customerDAO.addCustomer(customer);
    }

    public void updateCustomer(int customerId, Customer customer) throws CouponsException {
        Customer exist = customerDAO.getOneCustomer(customerId)
                .orElseThrow(() -> new CouponsException(EnumException.CUSTOMER_DO_NOT_EXIST));
        if (customer.getId() != customerId)
            throw new CouponsException(EnumException.SQL_ERROR);
        else
            customerDAO.updateCustomer(customer);
    }

    public void deleteCustomer(Customer customer) throws CouponsException {
        List<Coupon> customerCoupons = couponsDAO.getCouponsByCustomer(customer.getId());
        for (Coupon coupon : customerCoupons) {
            couponsDAO.deleteCouponPurchase(coupon.getId(), customer.getId());
        }
    }

    public ArrayList<Customer> getAllCustomers() throws CouponsException {
        return customerDAO.getAllCustomers();
    }

    public Optional<Customer> getOneCustomer(int customerId) throws CouponsException {
        return customerDAO.getOneCustomer(customerId);
    }

    public CouponsDAO getCouponDao(){
        return this.couponsDAO;
    }

    public CustomerDAO getCustomerDao(){
        return this.customerDAO;
    }

    public CompaniesDAO getCompanyDao(){
        return this.companiesDAO;
    }

}
