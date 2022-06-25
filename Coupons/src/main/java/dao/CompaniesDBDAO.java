package dao;

import beans.Company;
import db.JDBCUtils;
import db.ResultsUtils;
import exceptions.CouponsException;

import java.util.*;


public class CompaniesDBDAO implements CompaniesDAO {

    private static final String QUERY_INSERT = "INSERT INTO `Coupons_Project`.`companies` (`id`, `name`, `email`, `password`) VALUES (?, ?, ?, ?);";
    private static final String QUERY_UPDATE = "UPDATE `Coupons_Project`.`companies` SET `id` = ?, `name` = ?, `email` = ?, `password` = ? WHERE (`id` = ?);\n";
    private static final String QUERY_DELETE = "DELETE FROM `Coupons_Project`.`companies` WHERE (`id` = ?);";
    private static final String QUERY_GET_ALL = "SELECT * FROM `Coupons_Project`.`companies`;";
    private static final String QUERY_GET_ONE = "SELECT * FROM `Coupons_Project`.`companies` WHERE (`id` = ?);";
    private static final String QUERY_IS_EXISTS_EMAIL_PASSWORD =
            "select exists (SELECT * FROM `Coupons_Project`.`companies` where email = ? and password = ?) as res;";
    private static final String QUERY_IS_EXISTS_EMAIL_NAME =
            "select exists (SELECT * FROM `Coupons_Project`.`companies` where email = ? or name = ?) as res;";

    @Override
    public boolean isCompanyExists(String email, String password) throws CouponsException {
        boolean result = false;
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, email);
        params.put(2, password);
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_IS_EXISTS_EMAIL_PASSWORD, params);
        result = ResultsUtils.booleanFromRow((HashMap<String, Object>) rows.get(0));
        return result;
    }

    @Override
    public boolean isCompanyExistsEmailName(String email, String name) throws CouponsException {
        boolean result = false;
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, email);
        params.put(2, name);
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_IS_EXISTS_EMAIL_NAME, params);
        result = ResultsUtils.booleanFromRow((HashMap<String, Object>) rows.get(0));
        return result;
    }

    @Override
    public void addCompany(Company company) throws CouponsException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, company.getId());
        params.put(2, company.getName());
        params.put(3, company.getEmail());
        params.put(4, company.getPassword());
        JDBCUtils.executeQuery(QUERY_INSERT, params);
    }

    @Override
    public void updateCompany(Company company) throws CouponsException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, company.getId());
        params.put(2, company.getName());
        params.put(3, company.getEmail());
        params.put(4, company.getPassword());
        params.put(5, company.getId());
        JDBCUtils.executeQuery(QUERY_UPDATE, params);
    }

    @Override
    public void deleteCompany(int companyId) throws CouponsException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, companyId);
        JDBCUtils.executeQuery(QUERY_DELETE, params);
    }

    @Override
    public ArrayList<Company> getAllCompanies() throws CouponsException {
        ArrayList<Company> results = new ArrayList<>();

        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_ALL);
        for (Object object : rows) {
            results.add(ResultsUtils.companyFromRow((HashMap<String, Object>) object));
        }
        return results;
    }

    @Override
    public Optional<Company> getOneCompany(int companyId) throws CouponsException {
        Company result = null;
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, companyId);
        List<?> rows = JDBCUtils.executeQueryWithResults(QUERY_GET_ONE, params);
        if (rows.isEmpty()) {
            result = null;
        } else {
            result = ResultsUtils.companyFromRow((HashMap<String, Object>) (rows.get(0)));
        }
        return Optional.ofNullable(result);
    }
}
