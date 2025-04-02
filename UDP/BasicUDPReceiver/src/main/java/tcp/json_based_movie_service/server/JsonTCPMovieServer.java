package tcp.json_based_movie_service.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import tcp.json_based_movie_service.model.IMovieManager;
import tcp.json_based_movie_service.model.Movie;
import tcp.json_based_movie_service.model.MovieManager;
import tcp.movie_service.TCPNetworkLayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;

public class JsonTCPMovieServer {
    private final IMovieManager movieManager;
    private final Gson gson = new Gson();

    public JsonTCPMovieServer(IMovieManager movieManager) {
        this.movieManager = movieManager;
    }

    public static void main(String[] args) {
        IMovieManager movieManager = new MovieManager();
        JsonTCPMovieServer server = new JsonTCPMovieServer(movieManager);
        server.run();
    }

    public void run() {
        try(ServerSocket connectionSocket = new ServerSocket(MovieUtilities.PORT)) {
            boolean validServerSession = true;
            while(validServerSession){
                Socket clientDataSocket = connectionSocket.accept();

                TCPNetworkLayer networkService = new TCPNetworkLayer(clientDataSocket);

                boolean activeClientSession = true;
                while(activeClientSession) {
                    String request = networkService.receive();
                    System.out.println("Request: " + request);

                    JsonObject jsonResponse = null;

                    // Cast the request to a JsonObject
                    JsonObject jsonRequest = gson.fromJson(request, JsonObject.class);
                    // Extract the action from the json
                    if (jsonRequest.has("action")) {
                        String action = jsonRequest.get("action").getAsString();
                        switch (action) {
                            case MovieUtilities.ADD:
                                jsonResponse = handleAddAction(jsonRequest);
                                break;
                            case MovieUtilities.LIST:
                                jsonResponse = handleListAction(jsonRequest);
                                break;
                            case MovieUtilities.EXIT:
                                jsonResponse = createStatusResponse(MovieUtilities.GOODBYE, "Connection terminated");
                                activeClientSession = false;
                                break;
                        }
                    }
                    // If no response was set, set the default invalid response
                    if (jsonResponse == null) {
                        jsonResponse = createStatusResponse(MovieUtilities.INVALID, "Unknown action");
                    }

                    // Convert json response to text for transmission
                    String response = gson.toJson(jsonResponse);
                    // Send response
                    networkService.send(response);
                }
                // Shut down communication & close socket
                networkService.disconnect();
            }
        }catch (IOException e){
            System.out.println("Connection socket cannot be established");
        }
        System.out.println("Server shutting down.");
    }

    private JsonObject handleListAction(JsonObject jsonRequest) {
        JsonObject jsonResponse = null;
        if (jsonRequest.size() == 1) {
            List<Movie> movies = movieManager.getAllMovies();
            if (movies.isEmpty()) {
                jsonResponse =
                        createStatusResponse(MovieUtilities.NO_MOVIES_FOUND
                                , "No movies currently stored");
            } else {
                jsonResponse = new JsonObject();
                jsonResponse.addProperty("status", MovieUtilities.SUCCESS);
                jsonResponse.add("movies", gson.toJsonTree(movies));
            }
        }
        return jsonResponse;
    }

    private JsonObject handleAddAction(JsonObject jsonRequest) {
        JsonObject jsonResponse = null;
        JsonObject payload = (JsonObject) jsonRequest.get("payload");
        if (payload.size() == 3) {
            try {
                String name = payload.get("name").getAsString();

                int year = payload.get("year").getAsInt();
                if (year > LocalDate.now().getYear()) {
                    jsonResponse = createStatusResponse(MovieUtilities.INVALID_YEAR, "Illegal " +
                            "year " +
                            "supplied");
                    return jsonResponse;
                }

                String genre = payload.get("genre").getAsString();

                movieManager.add(name, year, genre);
                jsonResponse = createStatusResponse(MovieUtilities.ADDED, "Movie added " +
                        "successfully");
            } catch (NumberFormatException e) {
                jsonResponse = createStatusResponse(MovieUtilities.NON_NUMERIC, "Text supplied in" +
                        " " +
                        "place of number for year");
            }
        }
        return jsonResponse;
    }

    private JsonObject createStatusResponse(String status, String message){
        JsonObject invalidResponse = new JsonObject();
        invalidResponse.addProperty("status", status);
        invalidResponse.addProperty("message", message);
        return invalidResponse;
    }
}
