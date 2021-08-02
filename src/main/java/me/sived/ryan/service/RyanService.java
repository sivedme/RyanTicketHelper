package me.sived.ryan.service;

import me.sived.ryan.models.Result;
import me.sived.ryan.models.Route;
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

    Map<Double, Route[]> teleport(String depAirport, String arrAirport);
}
