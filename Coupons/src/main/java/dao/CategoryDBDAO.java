package dao;

import beans.Category;
import beans.Company;
import db.JDBCUtils;
import db.ResultsUtils;
import exceptions.CouponsException;
import exceptions.EnumException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDBDAO implements CategoryDao {
    private static final String QUERY_INSERT = "INSERT INTO `Coupons_Project`.`categories` (`name`) VALUES (?);";

    @Override
    public void insert(String name) throws CouponsException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, name);
        JDBCUtils.executeQuery(QUERY_INSERT, params);
    }

}
