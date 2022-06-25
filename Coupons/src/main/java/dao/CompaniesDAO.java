package dao;

import beans.Company;
import exceptions.CouponsException;

import java.util.ArrayList;
import java.util.Optional;


public interface CompaniesDAO {

    boolean isCompanyExists(String email, String password) throws CouponsException;

    boolean isCompanyExistsEmailName(String email, String name) throws CouponsException;

    void addCompany(Company company) throws CouponsException;

    void updateCompany(Company company) throws CouponsException;

    void deleteCompany(int companyId) throws CouponsException;

    ArrayList<Company> getAllCompanies() throws CouponsException;

    Optional<Company> getOneCompany(int companyId) throws CouponsException;

}
