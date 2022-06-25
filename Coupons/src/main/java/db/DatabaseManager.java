package db;

import beans.Category;
import dao.CategoryDBDAO;
import exceptions.CouponsException;

public class DatabaseManager {

    private static final DatabaseManager instance = new DatabaseManager();
    private CategoryDBDAO categoryDBDAO = new CategoryDBDAO();

    private DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    private static final String QUERY_CREATE_SCHEMA = "CREATE SCHEMA `coupons_project` ;";
    private static final String QUERY_DROP_SCHEMA = "DROP DATABASE `coupons_project`;";
    private static final String QUERY_CREATE_TABLE_COMPANIES = "CREATE TABLE `coupons_project`.`companies` (\n" +
            "  `ID` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `NAME` VARCHAR(45) NOT NULL,\n" +
            "  `EMAIL` VARCHAR(45) NOT NULL,\n" +
            "  `PASSWORD` VARCHAR(45) NOT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);";

    private static final String QUERY_CREATE_TABLE_CUSTOMERS = "CREATE TABLE `coupons_project`.`customers` (\n" +
            "  `ID` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `FIRST_NAME` VARCHAR(45) NOT NULL,\n" +
            "  `LAST_NAME` VARCHAR(45) NOT NULL,\n" +
            "  `EMAIL` VARCHAR(45) NOT NULL,\n" +
            "  `PASSWORD` VARCHAR(45) NOT NULL,\n" +
            "  PRIMARY KEY (`ID`),\n" +
            "  UNIQUE INDEX `id_UNIQUE` (`ID` ASC) VISIBLE);";

    private static final String QUERY_CREATE_TABLE_CATEGORIES = "CREATE TABLE `coupons_project`.`categories` (\n" +
            "  `ID` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `NAME` VARCHAR(45) NOT NULL,\n" +
            "  PRIMARY KEY (`ID`),\n" +
            "  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC) VISIBLE);";

    private static final String QUERY_CREATE_TABLE_COUPONS = "CREATE TABLE `coupons_project`.`coupons` (\n" +
            "  `ID` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `COMPANY_ID` INT NOT NULL,\n" +
            "  `CATEGORY_ID` INT NOT NULL,\n" +
            "  `TITLE` VARCHAR(45) NOT NULL,\n" +
            "  `DESCRIPTION` VARCHAR(45) NOT NULL,\n" +
            "  `START_DATE` DATE NOT NULL,\n" +
            "  `END_DATE` DATE NOT NULL,\n" +
            "  `AMOUNT` INT NOT NULL,\n" +
            "  `PRICE` DOUBLE NOT NULL,\n" +
            "  `IMAGE` VARCHAR(45) NOT NULL,\n" +
            "  PRIMARY KEY (`ID`),\n" +
            "  UNIQUE INDEX `ID_UNIQUE` (`ID` ASC) VISIBLE,\n" +
            "  INDEX `ID_idx` (`COMPANY_ID` ASC) VISIBLE,\n" +
            "  INDEX `FK_CATEGORIES_ID_idx` (`CATEGORY_ID` ASC) VISIBLE,\n" +
            "  CONSTRAINT `FK_COMPANIES_ID`\n" +
            "    FOREIGN KEY (`COMPANY_ID`)\n" +
            "    REFERENCES `coupons_project`.`companies` (`ID`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION,\n" +
            "  CONSTRAINT `FK_CATEGORIES_ID`\n" +
            "    FOREIGN KEY (`CATEGORY_ID`)\n" +
            "    REFERENCES `coupons_project`.`categories` (`ID`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);\n";

    private static final String QUERY_CREATE_TABLE_CUSTOMERS_VS_COUPONS = "CREATE TABLE `coupons_project`.`customers_coupons` (\n" +
            "  `CUSTOMER_ID` INT NOT NULL,\n" +
            "  `COUPON_ID` INT NOT NULL,\n" +
            "  PRIMARY KEY (`CUSTOMER_ID`, `COUPON_ID`),\n" +
            "  CONSTRAINT `FK_CUSTOMERS_ID`\n" +
            "    FOREIGN KEY (`CUSTOMER_ID`)\n" +
            "    REFERENCES `coupons_project`.`customers` (`ID`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION,\n" +
            "  CONSTRAINT `FK_COUPONS_ID`\n" +
            "    FOREIGN KEY (`COUPON_ID`)\n" +
            "    REFERENCES `coupons_project`.`coupons` (`ID`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);\n ";

    public void dropCreateStrategy() throws CouponsException {

        JDBCUtils.executeQuery(QUERY_DROP_SCHEMA);

        JDBCUtils.executeQuery(QUERY_CREATE_SCHEMA);
        JDBCUtils.executeQuery(QUERY_CREATE_TABLE_COMPANIES);
        JDBCUtils.executeQuery(QUERY_CREATE_TABLE_CUSTOMERS);
        JDBCUtils.executeQuery(QUERY_CREATE_TABLE_CATEGORIES);
        JDBCUtils.executeQuery(QUERY_CREATE_TABLE_COUPONS);
        JDBCUtils.executeQuery(QUERY_CREATE_TABLE_CUSTOMERS_VS_COUPONS);
        insertCategories();


    }

    public void insertCategories() throws CouponsException {
        for (Category c : Category.values()) {
            String name = c.name();
            categoryDBDAO.insert(name);
        }
    }


}
