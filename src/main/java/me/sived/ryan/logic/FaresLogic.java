package me.sived.ryan.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.sived.ryan.models.Result;
import me.sived.ryan.models.Route;
import me.sived.ryan.models.RouteFareJson;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FaresLogic {

    static HttpClient httpClient = HttpClient.newHttpClient();
    Logger logger = LoggerFactory.getLogger(FaresLogic.class);

    // AllFaresLogic
    public List allFaresFrom(String airportCode) {

        ArrayList<RouteFareJson> fares = new ArrayList<>();

        new RoutesLogic().from(airportCode).forEach(iterator ->
        {
            try {
                RouteFareJson temp = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create().fromJson(
                        httpClient.send(HttpRequest.newBuilder().GET().uri(URI.create("https://www.ryanair.com/api/farfnd/3/oneWayFares/" + iterator.getAirportFrom() + "/" + iterator.getAirportTo() + "/cheapestPerDay?outboundDateFrom=2021-07-01&outboundDateTo=2021-12-31")).build(),
                                HttpResponse.BodyHandlers.ofString()).body(), RouteFareJson.class);
                fares.add(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return fares;
    }


    //AllFaresForRoute


    public RouteFareJson allFaresForRoute(String dep, String arr) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("https://www.ryanair.com/api/farfnd/3/oneWayFares/" + dep + "/" + arr + "/cheapestPerDay?outboundDateFrom=2021-07-21&outboundDateTo=2023-02-01")).build();
        return getRouteFareJson(dep, arr, request);
    }

    public RouteFareJson allFaresForRouteAndDates(String dep, String arr, String dateFrom, String dateTo) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("https://www.ryanair.com/api/farfnd/3/oneWayFares/" + dep + "/" + arr + "/cheapestPerDay?outboundDateFrom=" + dateFrom + "&outboundDateTo=" + dateTo)).build();
        return getRouteFareJson(dep, arr, request);
    }

    private RouteFareJson getRouteFareJson(String dep, String arr, HttpRequest request) {
        try {
            RouteFareJson fareJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create().fromJson(
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body(), RouteFareJson.class);
            if (fareJson.getOutbound() == null) return null;
            Arrays.stream(fareJson.getOutbound().getFares()).forEach(routeFare -> routeFare.setRoute(new Route(dep, arr, null)));
            return fareJson;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //CheapestFares

    public Result cheapestFaresFrom(String airportCode) {

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("https://services-api.ryanair.com/farfnd/3/oneWayFares?&departureAirportIataCode=" + airportCode.toUpperCase() + "&language=en&outboundDepartureDateFrom=2020-01-01&outboundDepartureDateTo=2023-07-31"))
                .build();

        try {
            return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create().fromJson(httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body(), Result.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
