package tcp.json_based_movie_service.server;

public class MovieUtilities {
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 11000;

    // REQUESTS
    public static final String ADD = "ADD";
    public static final String REMOVE = "REMOVE";
    public static final String SEARCH = "SEARCH";
    public static final String LIST = "LIST";

    // RESPONSES
    public static final String ADDED = "ADDED";
    public static final String NON_NUMERIC = "NON_NUMERIC_DATA";
    public static final String INVALID_YEAR = "INVALID_YEAR";
    public static final String NO_MOVIES_FOUND = "NO_MOVIES_FOUND";

    public static final String REMOVED = "REMOVED";

    public static final String SUCCESS = "SUCCESS";

    public static final String NO_MATCH = "NO_MATCH_FOUND";

    // GENERAL MALFORMED RESPONSE:
    public static final String INVALID = "INVALID";

    // TERMINATION:
    public static final String EXIT = "EXIT";
    public static final String GOODBYE = "GOODBYE";
}
