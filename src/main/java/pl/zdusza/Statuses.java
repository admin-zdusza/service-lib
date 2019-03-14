package pl.zdusza;

public final class Statuses {

    private Statuses() {
    }

    public static final int SUCCESS = 200;

    public static final int BAD_REQUEST = 400;
    public static final String BAD_REQUEST_MSG = "Bad Request";

    public static final int UNAUTHORIZED = 401;
    public static final String UNAUTHORIZED_MSG = "Unauthorized";

    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final String INTERNAL_SERVER_ERROR_MSG = "Internal Server Error";
}
