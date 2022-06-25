package facade;

import dao.CompaniesDAO;
import dao.CouponsDAO;
import dao.CustomerDAO;
import exceptions.CouponsException;


public abstract class ClientFacade {
    protected CompaniesDAO companiesDAO;
    protected CustomerDAO customerDAO;
    protected CouponsDAO couponsDAO;

    public abstract boolean login(String email, String password) throws CouponsException;
}
