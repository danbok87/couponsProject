package facade;


import beans.ClientType;
import dao.CompaniesDBDAO;
import dao.CouponsDBDAO;
import dao.CustomerDBDAO;
import exceptions.CouponsException;

public class LoginManager {
    private static LoginManager instance = null;

    private LoginManager() {

    }

    public static LoginManager getInstance() {
        if (instance == null) {
            synchronized (LoginManager.class) {
                if (instance == null) {
                    instance = new LoginManager();
                }
            }
        }
        return instance;
    }

    public ClientFacade login(String email, String password, ClientType clientType) throws CouponsException {
        switch (clientType) {
            case Administrator: {
                AdminFacade administrator = new AdminFacade();
                if (administrator.login(email, password)) {
                    administrator.companiesDAO = new CompaniesDBDAO();
                    administrator.couponsDAO = new CouponsDBDAO();
                    administrator.customerDAO = new CustomerDBDAO();
                    return administrator;
                }
                break;
            }
            case Company: {
                CompanyFacade company = new CompanyFacade();
                if (company.login(email, password)) {
                    company.companiesDAO = new CompaniesDBDAO();
                    company.couponsDAO = new CouponsDBDAO();
                    company.customerDAO = new CustomerDBDAO();
                    return company;
                }
                break;
            }
            case Customer: {
                CustomerFacade customer = new CustomerFacade();
                if (customer.login(email, password)) {
                    customer.companiesDAO = new CompaniesDBDAO();
                    customer.couponsDAO = new CouponsDBDAO();
                    customer.customerDAO = new CustomerDBDAO();
                    return customer;
                }
                break;
            }
        }
        return null;
    }
}
