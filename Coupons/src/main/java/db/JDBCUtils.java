package db;

import exceptions.CouponsException;
import exceptions.EnumException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JDBCUtils {


    private static ResultSet createResultSet(PreparedStatement statement) throws CouponsException {
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            throw new CouponsException(EnumException.SQL_ERROR);
        }
    }

    public static PreparedStatement createStatement(Connection conn, String sql) throws CouponsException {
        try {
            return conn.prepareStatement(sql);
        } catch (SQLException e) {
            throw new CouponsException(EnumException.SQL_ERROR);
        }
    }

    private static void closeResources(Connection conn, PreparedStatement statement) throws CouponsException {
        try {
            ConnectionPool.getInstance().returnConnection(conn);
            statement.close();
        } catch (SQLException e) {
            throw new CouponsException(EnumException.SQL_ERROR);
        }
    }


    private static void closeResources(Connection conn, PreparedStatement statement, ResultSet resultSet) throws CouponsException {
        try {
            ConnectionPool.getInstance().returnConnection(conn);
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new CouponsException(EnumException.SQL_ERROR);
        }
    }

    public static void executeQuery(String sql) throws CouponsException {

        try {
            //Step 1 - Not relevant anyMore since we're using JDBC Drive Type 4

            //Step 2 - get Resources
            Connection conn = ConnectionPool.getInstance().getConnection();

            //Step 3 - Prepare your Statement & execute it
            PreparedStatement statement = JDBCUtils.createStatement(conn, sql);
            statement.execute();

            //Step 4 -  Not relevant it's not DQL - Result Set

            //Step 5 - Close Resources
            closeResources(conn, statement);
        } catch (SQLException e) {
            throw new CouponsException(EnumException.SQL_ERROR);
        } catch (InterruptedException e) {
            throw new CouponsException(EnumException.THREAD_INTERRUPTED);
        }

    }


    public static void executeQuery(String sql, Map<Integer, Object> params) throws CouponsException {
        try {//Step 1 - Not relevant anyMore since we're using JDBC Drive Type 4

            //Step 2 - get Resources
            Connection conn = ConnectionPool.getInstance().getConnection();

            //Step 3 - Prepare your Statement & execute it
            PreparedStatement statement = JDBCUtils.createStatement(conn, sql);
            addParams(statement, params);
            statement.execute();

            //Step 4 -  Not relevant it's not DQL - Result Set

            //Step 5 - Close Resources
            closeResources(conn, statement);
        } catch (SQLException e) {
            throw new CouponsException(EnumException.SQL_ERROR);
        } catch (InterruptedException e) {
            throw new CouponsException(EnumException.THREAD_INTERRUPTED);
        }
    }


    public static List<?> executeQueryWithResults(String sql) throws CouponsException {

        try {
            List<?> rows = new ArrayList<>();
            //Step 1 - Not relevant anyMore since we're using JDBC Drive Type 4

            //Step 2 - get Resources
            Connection conn = ConnectionPool.getInstance().getConnection();

            //Step 3 - Prepare your Statement & execute it
            PreparedStatement statement = JDBCUtils.createStatement(conn, sql);

            //Step 4 -  Now It's  relevant since it's a DQL
            ResultSet resultSet = JDBCUtils.createResultSet(statement);
            rows = getRows(resultSet);

            //Step 5 - Close Resources
            closeResources(conn, statement, resultSet);
            return rows;
        } catch (InterruptedException e) {
            throw new CouponsException(EnumException.THREAD_INTERRUPTED);
        }

    }


    public static List<?> executeQueryWithResults(String sql, Map<Integer, Object> params) throws CouponsException {

        try {
            List<?> rows = new ArrayList<>();
            //Step 1 - Not relevant anyMore since we're using JDBC Drive Type 4

            //Step 2 - get Resources
            Connection conn = ConnectionPool.getInstance().getConnection();

            //Step 3 - Prepare your Statement & execute it
            PreparedStatement statement = JDBCUtils.createStatement(conn, sql);
            addParams(statement, params);

            ResultSet resultSet = JDBCUtils.createResultSet(statement);

            //Step 4 -  Now It's  relevant since it's a DQL
            rows = getRows(resultSet);

            //Step 5 - Close Resources
            closeResources(conn, statement, resultSet);

            return rows;
        } catch (SQLException e) {
            throw new CouponsException(EnumException.SQL_ERROR);
        } catch (InterruptedException e) {
            throw new CouponsException(EnumException.THREAD_INTERRUPTED);
        }

    }


    private static PreparedStatement addParams(PreparedStatement statement, Map<Integer, Object> params) throws SQLException, CouponsException {

        try {
            for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                int key = entry.getKey();
                Object val = entry.getValue();
                if (val instanceof Integer) {
                    statement.setInt(key, (Integer) val);
                    continue;
                }
                if (val instanceof String) {
                    statement.setString(key, (String) val);
                    continue;
                }
                if (val instanceof Date) {
                    statement.setDate(key, (Date) val);
                    continue;
                }
                if (val instanceof Double) {
                    statement.setDouble(key, (Double) val);
                    continue;
                }
                if (val instanceof Boolean) {
                    statement.setBoolean(key, (Boolean) val);
                    continue;
                }

            }

            return statement;
        } catch (SQLException e) {
            throw new CouponsException(EnumException.SQL_ERROR);
        }

    }


    private static List<?> getRows(ResultSet resultSet) throws CouponsException {

        try {
            ResultSetMetaData md = resultSet.getMetaData();
            int columns = md.getColumnCount();
            List<Map<String, Object>> results = new ArrayList<>();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>(columns);
                for (int i = 1; i < columns + 1; i++) {
                    row.put(md.getColumnName(i), resultSet.getObject(i));
                }
                results.add(row);
            }

            return results;
        } catch (SQLException e) {
            throw new CouponsException(EnumException.SQL_ERROR);
        }

    }


}
