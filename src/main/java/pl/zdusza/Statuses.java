package pl.zdusza;

public final class Statuses {

    private Statuses() {
    }

    public static final int SUCCESS = 200;

    public static final int MOVED_PERMANENTLY = 301;
    public static final String MOVED_PERMANENTLY_MSG = "Moved Permanently";

    public static final int FOUND = 302;
    public static final String FOUND_MSG = "Found";

    public static final int BAD_REQUEST = 400;
    public static final String BAD_REQUEST_MSG = "Bad Request";

    public static final int UNAUTHORIZED = 401;
    public static final String UNAUTHORIZED_MSG = "Unauthorized";

    public static final int PRECONDITION_FAILED = 412;
    public static final String PRECONDITION_FAILED_MSG = "Precondition Failed";

    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final String INTERNAL_SERVER_ERROR_MSG = "Internal Server Error";
}
