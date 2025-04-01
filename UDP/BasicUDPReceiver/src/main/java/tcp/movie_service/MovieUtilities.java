package tcp.movie_service;

public class MovieUtilities {
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 11000;

    // REQUESTS
    public static final String LOGIN = "LOGIN";
    public static final String ADD = "ADD";
    public static final String LIST = "LIST";
    public static final String EXIT = "EXIT";

    // DELIMITERS
    public static final String DELIMITER = "%%";
    public static final String MOVIE_DELIMITER = "~~";

    // RESPONSES
    public static final String SUCCESS = "LOGIN_SUCCESS";
    public static final String FAILED = "LOGIN_FAILED";
    public static final String NOT_LOGGED_IN = "NOT_LOGGED_IN";
    public static final String ADDED = "ADDED";
    public static final String NON_NUMERIC = "NON_NUMERIC_DATA";
    public static final String INVALID_YEAR = "INVALID_YEAR";
    public static final String NO_MOVIES_FOUND = "NO_MOVIES_FOUND";
    public static final String ACK = "GOODBYE";

    // GENERAL MALFORMED RESPONSE:
    public static final String INVALID = "INVALID";
}
