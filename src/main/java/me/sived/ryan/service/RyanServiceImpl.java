package me.sived.ryan.service;

import me.sived.ryan.logic.*;
import me.sived.ryan.models.Result;
import me.sived.ryan.models.Route;
import me.sived.ryan.models.RouteFare;
import me.sived.ryan.models.RouteFareJson;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RyanServiceImpl implements RyanService {

    private final FaresLogic logic = new FaresLogic();

    @Override
    public Result getCheapestFaresFrom(String airport) {
        return logic.cheapestFaresFrom(airport.toUpperCase());
    }

    @Override
    public List<Route> getRoutesFrom(String airport) {
        return new RoutesLogic().from(airport.toUpperCase());
    }

    @Override
    public RouteFareJson getAllFaresForRoute(String depAirport, String arrAirport) {
        return logic.allFaresForRoute(depAirport.toUpperCase(), arrAirport.toUpperCase());
    }

    @Override
    public RouteFareJson getAllFaresForRouteAndDates(String depAirport, String arrAirport, String dateFrom, String dateTo) {
        return logic.allFaresForRouteAndDates(depAirport.toUpperCase(), arrAirport.toUpperCase(), dateFrom, dateTo);
    }

    @Override
    public Map<RouteFare[], Double> teleport(String depAirport, String arrAirport) {
        return new Teleport().takeMeTo(depAirport.toUpperCase(), arrAirport.toUpperCase());
    }

    @Override
    public Map<RouteFare[], Double> teleport(String depAirport, String arrAirport, String dateFrom, String dateTo) {
        return new Teleport().takeMeTo(depAirport.toUpperCase(), arrAirport.toUpperCase(), dateFrom, dateTo);
    }

    @Override
    public Map<RouteFare[], Double> teleportAndBack(String depAirport, String arrAirport, String length) {
        return null;
    }

    @Override
    public Map<RouteFare[], Double> teleportAndBack(String depAirport, String arrAirport, String dateFrom, String dateTo, String length) {
        return new Teleport().takeMeToAndBack(depAirport, arrAirport, dateFrom, dateTo, length);
    }


}