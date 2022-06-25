import beans.*;
import dao.CompaniesDAO;
import dao.CompaniesDBDAO;
import db.ConnectionPool;
import db.DatabaseManager;
import exceptions.CouponsException;
import facade.AdminFacade;
import facade.CompanyFacade;
import facade.CustomerFacade;
import jobs.DailyDeleteExpirationCoupons;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Test {


    public static void main(String[] args) throws CouponsException {
        testAll();
    }

    private static void testAll() throws CouponsException {
        try {

            System.out.println("START");
            /// Drop Create of the entire DB ///
            DatabaseManager.getInstance().dropCreateStrategy();
            /// Admin Facade - Create the Companies & Customers ///
            AdminFacade admin = new AdminFacade("admin@admin.com", "admin");
            Company tnuva = new Company(1, "Tnuva", "tnuva@tnuva.com", "tnuva");
            Company globalIt = new Company(2, "GlobalIT", "GlobalIT@it.com", "glbit");
            admin.addCompany(tnuva);
            admin.addCompany(globalIt);
            Customer moshe = new Customer(1, "Moshe", "Cohen", "mosheCohen@gmail.com", "moshe");
            Customer david = new Customer(2, "David", "Levi", "davidLevi@gmail.com", "david");
            admin.addCustomer(moshe);
            admin.addCustomer(david);
            /// Company Facade - Create the Coupons
            CompanyFacade tnuvaCompany = new CompanyFacade(tnuva.getEmail(), tnuva.getPassword(), tnuva.getId());
            Coupon computerCoupon = new Coupon(
                    1,
                    1,
                    Category.getValue(Category.Food),
                    "Ice Cream Coupon",
                    "Two Ice cream in 1 shekel",
                    Date.valueOf(LocalDate.of(2022, 06, 1)),
                    Date.valueOf(LocalDate.of(2022, 12, 31)),
                    5,
                    25.0,
                    "url://tnuva/IceCreams");
            tnuvaCompany.addCoupon(computerCoupon);
            Coupon computerCoupon2 = new Coupon(
                    2,
                    1,
                    Category.getValue(Category.Food),
                    "Chocolate Coupon",
                    "Two Chocolate in 5 Shekels",
                    Date.valueOf(LocalDate.of(2022, 06, 1)),
                    Date.valueOf(LocalDate.of(2022, 12, 31)),
                    12,
                    10.0,
                    "url://tnuva/Chocolates");
            tnuvaCompany.addCoupon(computerCoupon2);
            computerCoupon2.setAmount(6);
            tnuvaCompany.updateCoupon(2, computerCoupon2);
            /// Switch to other Company to and create Coupons ///
            CompanyFacade globalItCompany = new CompanyFacade(globalIt.getEmail(), globalIt.getPassword(), globalIt.getId());
            Coupon computerCoupon3 = new Coupon(
                    3,
                    2,
                    Category.getValue(Category.Electricity),
                    "PC Screen Coupon",
                    "17 inc LED Screen",
                    Date.valueOf(LocalDate.of(2022, 06, 1)),
                    Date.valueOf(LocalDate.of(2022, 12, 31)),
                    15,
                    300.0,
                    "url://globalIt/Screens");
            globalItCompany.addCoupon(computerCoupon3);
            Coupon computerCoupon4 = new Coupon(
                    4,
                    2,
                    Category.getValue(Category.Electricity),
                    "Laptop Coupon",
                    "Laptop I7 dual core 8th gen",
                    Date.valueOf(LocalDate.of(2022, 01, 1)),
                    Date.valueOf(LocalDate.of(2022, 05, 31)),
                    10,
                    400.5,
                    "url://globalIt/Computers");
            globalItCompany.addCoupon(computerCoupon4);
            /// Customer Facade - Buy Coupons ///
            CustomerFacade mosheCustomer = new CustomerFacade(moshe.getEmail(), moshe.getPassword(), moshe.getId());
            mosheCustomer.purchaseCoupon(computerCoupon);
            mosheCustomer.purchaseCoupon(computerCoupon2);
            /// Customer Facade - Switch Customer and Buy Coupons ///
            CustomerFacade davidCustomer = new CustomerFacade(david.getEmail(), david.getPassword(), david.getId());
            davidCustomer.purchaseCoupon(computerCoupon3);
            davidCustomer.purchaseCoupon(computerCoupon4);
            /// Print all Coupons ///
            System.out.println("Coupons");
            System.out.println("-------");
            Print.print(admin.getCouponDao().getAllCoupons());

            Thread thread = new DailyDeleteExpirationCoupons();
            thread.start();
            System.out.println("Expired Coupons");
            System.out.println("---------------");
            Print.print(admin.getCouponDao().getAllExpiredCoupons());
            System.out.println("After removal of expired coupons");
            System.out.println("--------------------------------");
            Print.print(admin.getCouponDao().getAllNonExpiredCoupons());
            System.out.println("All Companies");
            System.out.println("-------------");
            Print.print(admin.getCompanyDao().getAllCompanies());
            System.out.println("All Customers");
            System.out.println("-------------");
            Print.print(admin.getCustomerDao().getAllCustomers());
            ((DailyDeleteExpirationCoupons) thread).endJob();
            try {
                ConnectionPool.getInstance().closeAllConnections();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("END");
        } catch (CouponsException e) {
            System.out.println(e);
        }

    }
}
