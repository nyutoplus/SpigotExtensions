package si.f5.invisiblerabbit.exception;

public class IllegalArrayLengthException extends RuntimeException {

    static final long serialVersionUID = 0x01L;

    public IllegalArrayLengthException() {
	super();
    }

    public IllegalArrayLengthException(String msg) {
	super(msg);
    }

}
