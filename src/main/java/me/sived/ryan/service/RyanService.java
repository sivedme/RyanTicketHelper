package me.sived.ryan.service;

import me.sived.ryan.logic.AllFaresForRouteLogic;
import me.sived.ryan.models.Result;
import me.sived.ryan.models.Route;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RyanService {
    Result getCheapestFaresFrom(String airport);
    Route[] getRoutesFrom(String airport);
    List getAllFaresFrom(String airport);
    AllFaresForRouteLogic.RouteFareJson getAllFaresForRoute(String depAirport, String arrAirport);
}
