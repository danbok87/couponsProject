package exceptions;

public enum EnumException {
    NOT_AUTHORIZED("Incorrect email or password, access denied"),
    SQL_ERROR("An error occurred during sql activity"),
    THREAD_INTERRUPTED("This thread interrupted"),
    COMPANY_DO_NOT_EXIST("Can't update, company doesn't exist"),
    EXIST_NAME_EMAIL("Name or email already exist"),
    EXIST_CUSTOMER_EMAIL("This email already exist for another customer"),
    CUSTOMER_DO_NOT_EXIST("Can't update, customer doesn't exist"),
    ADDING_COUPON_NOT_SAME_COMPANY("Adding coupon of another company is not allowed"),
    EXIST_TITLE_FOR_COMPANY("already exist title for this company"),
    COUPON_DO_NOT_EXIST("Can't update, coupon doesn't exist"),
    UPDATE_COMPANY_ID_OR_COUPON_ID("Updating company id or coupon id is not allowed"),
    COUPON_ALREADY_PURCHASE("This coupon already purchase by this customer"),
    COUPON_RUN_OFF("There are no coupons left to purchase"),
    COUPON_EXPIRED("The coupon has expired");

    private final String msg;

    EnumException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
