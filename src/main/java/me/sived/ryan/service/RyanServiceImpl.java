package me.sived.ryan.service;

import me.sived.ryan.logic.*;
import me.sived.ryan.models.Result;
import me.sived.ryan.models.Route;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RyanServiceImpl implements RyanService {

    @Override
    public Result getCheapestFaresFrom(String airport) {
        return new CheapestFaresLogic().from(airport);
    }

    @Override
    public List<Route> getRoutesFrom(String airport) {
        return new RoutesLogic().from(airport);
    }

    @Override
    public List getAllFaresFrom(String airport) {
        return new AllFaresLogic().from(airport);
    }

    @Override
    public AllFaresForRouteLogic.RouteFareJson getAllFaresForRoute(String depAirport, String arrAirport) {
        return new AllFaresForRouteLogic().fromAndTo(depAirport, arrAirport);
    }

    @Override
    public Map<Double, Route[]> teleport(String depAirport, String arrAirport) {
        return new Teleport().takeMeTo(depAirport, arrAirport);
    }


}