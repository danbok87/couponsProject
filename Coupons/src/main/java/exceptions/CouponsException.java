package exceptions;

public class CouponsException extends Exception {

    public CouponsException(EnumException exception) {
        super(exception.getMsg());
    }

}
