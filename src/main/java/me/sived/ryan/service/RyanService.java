package me.sived.ryan.service;

import me.sived.ryan.models.Result;
import me.sived.ryan.models.Route;
import me.sived.ryan.models.RouteFare;
import me.sived.ryan.models.RouteFareJson;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RyanService {
    Result getCheapestFaresFrom(String airport);
    List<Route> getRoutesFrom(String airport);
    List getAllFaresFrom(String airport);
    RouteFareJson getAllFaresForRoute(String depAirport, String arrAirport);
    RouteFareJson getAllFaresForRouteAndDates(String depAirport, String arrAirport, String dateFrom, String dateTo);

    Map<RouteFare[], Double> teleport(String depAirport, String arrAirport);
    Map<RouteFare[], Double> teleport(String depAirport, String arrAirport, String dateFrom, String dateTo);
    Map<RouteFare[], Double> teleportAndBack(String depAirport, String arrAirport, String length);
    Map<RouteFare[], Double> teleportAndBack(String depAirport, String arrAirport, String dateFrom, String dateTo, String length);
}
