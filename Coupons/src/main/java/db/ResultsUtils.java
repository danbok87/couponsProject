package db;

import beans.*;

import java.sql.Date;
import java.util.HashMap;


public class ResultsUtils {

    public static Company companyFromRow(HashMap<String, Object> map) {
        int id = (int) map.get("ID");
        String name = (String) map.get("NAME");
        String email = (String) map.get("EMAIL");
        String password = (String) map.get("PASSWORD");
        return new Company(id, name, email, password);

    }

    public static String categoryFromRow(HashMap<String, Object> map) {
        String name = (String) map.get("NAME");

        return name;
    }

    public static Customer customerFromRow(HashMap<String, Object> map) {
        int id = (int) map.get("ID");
        String firstName = (String) map.get("FIRST_NAME");
        String lastName = (String) map.get("LAST_NAME");
        String email = (String) map.get("EMAIL");
        String password = (String) map.get("PASSWORD");

        return new Customer(id, firstName, lastName, email, password);
    }

    public static Coupon couponFromRow(HashMap<String, Object> map) {
        int id = (int) map.get("ID");
        int companyId = (int) map.get("COMPANY_ID");
        Category category = Category.getValue((int) map.get("CATEGORY_ID"));
        String title = (String) map.get("TITLE");
        String description = (String) map.get("DESCRIPTION");
        Date startDate = (Date) map.get("START_DATE");
        Date endDate = (Date) map.get("END_DATE");
        int amount = (int) map.get("AMOUNT");
        double price = (double) map.get("PRICE");
        String image = (String) map.get("IMAGE");

        return new Coupon(id, companyId, Category.getValue(category), title, description, startDate, endDate, amount, price, image);
    }


    public static boolean booleanFromRow(HashMap<String, Object> map) {
        long val = (long) map.get("res");
        return val == 1;
    }


}
