package me.sived.ryan.logic;

import com.google.gson.GsonBuilder;
import me.sived.ryan.models.Result;
import me.sived.ryan.models.Route;
import me.sived.ryan.models.RouteFareJson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class FaresLogic {

    static final HttpClient httpClient = HttpClient.newHttpClient();
    final String BASE_LINK = "https://www.ryanair.com/api/farfnd/3/oneWayFares/";
    final String BASE_LINK_CHEAPEST = "https://services-api.ryanair.com/farfnd/3/oneWayFares?&departureAirportIataCode=";
    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


    public RouteFareJson allFaresForRoute(String dep, String arr) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        return allFaresForRouteAndDates(dep, arr, format.format(new Date()), format.format(calendar.getTime()));
    }

    public RouteFareJson allFaresForRouteAndDates(String dep, String arr, String dateFrom, String dateTo) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(BASE_LINK + dep + "/" + arr + "/cheapestPerDay?outboundDateFrom=" + dateFrom + "&outboundDateTo=" + dateTo)).build();
        return getRouteFareJson(dep, arr, request);
    }

    private RouteFareJson getRouteFareJson(String dep, String arr, HttpRequest request) {
        try {
            RouteFareJson fareJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create().fromJson(
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body(), RouteFareJson.class);
            if (fareJson.getOutbound() == null) return null;
            Arrays.stream(fareJson.getOutbound().getFares()).parallel().forEach(routeFare -> routeFare.setRoute(new Route(dep, arr, null)));
            return fareJson;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Result cheapestFaresFrom(String airportCode) {

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(BASE_LINK_CHEAPEST + airportCode.toUpperCase() + "&language=en&outboundDepartureDateFrom=2020-01-01&outboundDepartureDateTo=2023-07-31"))
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
