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

    private FaresLogic logic = new FaresLogic();

    @Override
    public Result getCheapestFaresFrom(String airport) {
        return logic.cheapestFaresFrom(airport);
    }

    @Override
    public List<Route> getRoutesFrom(String airport) {
        return new RoutesLogic().from(airport);
    }

    @Override
    public List getAllFaresFrom(String airport) {
        return logic.allFaresFrom(airport);
    }

    @Override
    public RouteFareJson getAllFaresForRoute(String depAirport, String arrAirport) {
        return logic.allFaresForRoute(depAirport, arrAirport);
    }

    @Override
    public Map<RouteFare[], Double> teleport(String depAirport, String arrAirport) {
        return new Teleport().takeMeTo(depAirport, arrAirport);
    }


}