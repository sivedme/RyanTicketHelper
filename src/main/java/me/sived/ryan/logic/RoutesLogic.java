package me.sived.ryan.logic;

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

    final static HttpClient httpClient = HttpClient.newHttpClient();

    public List<Route> from(String airportCode) {

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("https://services-api.ryanair.com/locate/3/routes/" + airportCode))
                .build();

        try {
            Route[] routes = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create().fromJson(httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body(), Route[].class);
            return Arrays.stream(routes).parallel().filter(route -> route.getConnectingAirport() == null).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}