package tcp.json_based_movie_service.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import tcp.json_based_movie_service.model.Movie;
import tcp.json_based_movie_service.network.TCPNetworkLayer;
import tcp.json_based_movie_service.server.MovieUtilities;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

public class JsonTCPMovieClient {
    private final static Scanner input = new Scanner(System.in);

    public static int getInt(String prompt){
        boolean valid = false;
        int num = 0;
        while(!valid){
            System.out.println(prompt);
            if(input.hasNextInt()){
                num = input.nextInt();
                valid = true;
            }else{
                System.out.println("Invalid data entered. Please enter an integer.");
            }
        }

        input.nextLine();
        return num;
    }

    public static void displayMenu(){
        System.out.println("0) Exit");
        System.out.println("1) Add a movie");
        System.out.println("2) Remove a movie");
        System.out.println("3) Get a specific movie");
        System.out.println("4) View all movies");
    }

    public static void displayMovies(List<Movie> movies){
        for(Movie m: movies) {
            System.out.println(" - " + m.format());
        }
    }

    public static void main(String[] args) {
        try {
            TCPNetworkLayer networkService = new TCPNetworkLayer(MovieUtilities.HOSTNAME, MovieUtilities.PORT);
            networkService.connect();

            Gson gson = new Gson();
            boolean keepRunning = true;

            while(keepRunning) {
                System.out.println("Please choose from the following options:");
                displayMenu();
                String choice = input.nextLine();
                JsonObject requestJson = null;
                switch(choice){
                    case "0":
                        keepRunning = false;
                        requestJson = new JsonObject();
                        requestJson.addProperty("action", MovieUtilities.EXIT);
                        break;
                    case "1":
                        requestJson = addMovie();
                        break;
                    case "4":
                        requestJson = new JsonObject();
                        requestJson.addProperty("action", MovieUtilities.LIST);
                        break;
                    case "3":
                    case "2":
                    default:
                        System.out.println("Invalid option selected. Please try again.");
                        continue;
                }

                // Send a message
                String request = gson.toJson(requestJson);
                networkService.send(request);

                // Receive a message
                String response = networkService.receive();

                JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);

                if(!jsonResponse.has("status")){
                    System.out.println("No status field found");
                    System.out.println("Bad response detected.");
                    System.out.println("Response text: " + response);
                    continue;
                }

                switch(choice){
                    case "1":
                        handleAddResponse(jsonResponse, response);
                        break;
                    case "4":
                        handleListResponse(jsonResponse, gson);
                        break;
                }
            }
            // Shut down communication & shut down socket
            networkService.disconnect();
        }catch(IOException e){
            System.out.println("Could not connect to server");
        }
    }

    private static void handleListResponse(JsonObject jsonResponse, Gson gson) {
        if(jsonResponse.has("movies")) {
            JsonArray jsonMovieArray = jsonResponse.get("movies").getAsJsonArray();
            Type listType = new TypeToken<List<Movie>>() {}.getType();
            List<Movie> movies = gson.fromJson(jsonMovieArray, listType);
            displayMovies(movies);
        }else{
            System.out.println("No movies found.");
        }
    }

    private static void handleAddResponse(JsonObject jsonResponse, String response) {
        if(jsonResponse.has("message")) {
            String result = jsonResponse.get("message").getAsString();
            System.out.println(result);
        }else{
            System.out.println("No message field found");
            System.out.println("Bad response detected.");
            System.out.println("Response text: " + response);
        }
    }

    private static JsonObject addMovie(){
        System.out.println("Movie name: ");
        String name = input.nextLine();
        int year = getInt("Year of release");
        System.out.println("Genre: ");
        String genre = input.nextLine();

        // Create the request payload
        JsonObject payload = new JsonObject();
        payload.addProperty("name", name);
        payload.addProperty("year", year);
        payload.addProperty("genre", genre);

        // Create the overall request object
        JsonObject requestJson = new JsonObject();
        // Add the request type/action and payload
        requestJson.addProperty("action", "ADD");
        requestJson.add("payload", payload);

        return requestJson;
    }
}
