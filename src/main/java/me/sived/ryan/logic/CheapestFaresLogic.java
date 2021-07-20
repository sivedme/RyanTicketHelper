package me.sived.ryan.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.sived.ryan.models.Result;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CheapestFaresLogic {

    static HttpClient httpClient = HttpClient.newHttpClient();

    public Result from(String airportCode) {

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("https://services-api.ryanair.com/farfnd/3/oneWayFares?&departureAirportIataCode=" + airportCode.toUpperCase() + "&language=en&outboundDepartureDateFrom=2020-01-01&outboundDepartureDateTo=2023-07-31"))
                .build();

        try {

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            Result result = gson
                    .fromJson(httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body(), Result.class);

            System.out.println(result);
            System.out.println("success");
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Fail");
        return null;
    }
}