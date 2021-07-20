package me.sived.ryan.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.sived.ryan.models.Route;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RoutesLogic {

    static HttpClient httpClient = HttpClient.newHttpClient();

    public Route[] from(String airportCode) {

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("https://services-api.ryanair.com/locate/3/routes/" + airportCode.toUpperCase()))
                .build();

        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();
            return gson.fromJson(httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body(), Route[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
