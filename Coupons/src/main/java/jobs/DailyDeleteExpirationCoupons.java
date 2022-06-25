package jobs;

import beans.Coupon;
import dao.CouponsDAO;
import dao.CouponsDBDAO;
import exceptions.CouponsException;
import exceptions.EnumException;

import java.util.List;

public class DailyDeleteExpirationCoupons extends Thread {

    private CouponsDAO couponsDAO = new CouponsDBDAO();
    private static volatile boolean quit;

    @Override
    public void run() {
        while (!quit) {

            try {
                List<Coupon> expiredCoupons = this.couponsDAO.getAllExpiredCoupons();
                for (Coupon coupon : expiredCoupons) {
                    this.couponsDAO.deleteCouponCustomerById(coupon.getId());
                    this.couponsDAO.deleteCoupon(coupon.getId());
                }
            } catch (CouponsException e) {
                try {
                    throw new CouponsException(EnumException.SQL_ERROR);
                } catch (CouponsException ex) {
                    ex.printStackTrace();
                }
            }

            try {
                Thread.sleep(1000 * 60 * 60 * 24);
            } catch (InterruptedException e) {
                try {
                    throw new CouponsException(EnumException.THREAD_INTERRUPTED);
                } catch (CouponsException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void endJob() {
        quit = true;
    }

}
