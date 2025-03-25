package udp_movie_service;

public class MovieUtilities {
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 11000;

    // REQUESTS
    public static final String ADD = "ADD";
    
    // DELIMITER
    public static final String DELIMITER = "%%";
    
    // RESPONSES
    public static final String ADDED = "ADDED";
    public static final String NON_NUMERIC = "NON_NUMERIC_DATA";
    public static final String INVALID_YEAR = "INVALID_YEAR";
    
    // GENERAL MALFORMED RESPONSE:
    public static final String INVALID = "INVALID";
}
