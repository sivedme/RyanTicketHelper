package me.sived.ryan.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.sived.ryan.models.Route;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RoutesLogic {

    static HttpClient httpClient = HttpClient.newHttpClient();

    public List<Route> from(String airportCode) {

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("https://services-api.ryanair.com/locate/3/routes/" + airportCode.toUpperCase()))
                .build();

        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();
            Route[] routes = gson.fromJson(httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body(), Route[].class);
            List<Route> copy = Arrays.stream(routes).parallel().filter(route -> route.getConnectingAirport() == null).collect(Collectors.toList());
            return copy;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}