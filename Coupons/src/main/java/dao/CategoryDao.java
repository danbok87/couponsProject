package dao;

import exceptions.CouponsException;

import java.sql.SQLException;

public interface CategoryDao {
    void insert(String name) throws SQLException, InterruptedException, CouponsException;
}
