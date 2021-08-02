package me.sived.ryan.logic;

import me.sived.ryan.models.Route;
import me.sived.ryan.models.RouteFareJson;
import me.sived.ryan.service.RyanService;
import me.sived.ryan.service.RyanServiceImpl;
import me.sived.ryan.models.Outbound;
import me.sived.ryan.models.Outbound.RouteFare;
import me.sived.ryan.models.Outbound.RouteFare.Price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class Teleport {

    RyanService ryanService = new RyanServiceImpl();
    Logger logger = LoggerFactory.getLogger(Teleport.class);

    public Map<Double, Route[]> takeMeTo(String orig, String dest) {
        Map<Double, Route[]> map = new TreeMap<>();

        logger.info("initializing takeMeTo with " + orig + " " + dest);
        List<String> connectingAirports = checkConnectingAirports(orig, dest);
        logger.info("found connecting airports: " + connectingAirports.toString());
        for (String ap : connectingAirports) {
            logger.info("loop " + ap);
            map.putAll(doSomething(getFares(orig, ap), dest));
        }

        return map;
    }


    List<String> checkConnectingAirports(String orig, String dest) {
        List<Route> routesDest = ryanService.getRoutesFrom(dest);  //ATH -> ACE, AGP...
        List<Route> routesOrig = ryanService.getRoutesFrom(orig); //KHE -> KRK
        ArrayList<String> list = new ArrayList<>();

        for (Route destination : routesDest) {
            for (Route origin : routesOrig) {
//                logger.info(destination.getAirportTo() + " /|" + origin.getAirportTo());
                if (origin.getAirportTo().equals(destination.getAirportTo())) {
                    list.add(destination.getAirportTo());
                }

            }
        }

        return list;
    }

    Map<Price, ArrayList<RouteFare>> getFares(String orig, String dest) {
        logger.info("getFares " + orig + " " + dest);
        Map<Price, ArrayList<RouteFare>> map = new FaresLogic().allFaresForRoute(orig, dest).getOutbound().clean();
        logger.info(map.values().toString());

        return map;
    }

    Map<Double, Route[]> doSomething(Map<Price, ArrayList<RouteFare>> listOne, String destAirport) {
        logger.info("doSomething  " + listOne.values() + " " + destAirport);
        TreeMap<Double, Route[]> resMap = new TreeMap<>();

        listOne.forEach((key, value) -> {
            logger.info("initializing list forEach " + key.getValue() + " " + value);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            RouteFare fare = value.get(0);
            logger.info("fare = " + fare.getPrice());
            Date date = fare.getArrivalDate();
            Date secDate = new Date(date.getTime()+172800000);
            logger.info("dates: " + format.format(date) + " " + format.format(secDate));
            RouteFareJson fj = new FaresLogic().allFaresForRouteAndDates(fare.getRoute().getAirportTo(), destAirport, format.format(date), format.format(secDate));
            logger.info("fj values = " + fare.getRoute().getAirportTo() + " " + destAirport + " " + format.format(date) + " " + format.format(secDate));
            if (fj == null) return;
            Map<RouteFare.Price, ArrayList<RouteFare>> tempMap = fj.getRouteFareMap();
            logger.info("tempMap " + tempMap.toString());
            tempMap.forEach((keyTwo, valueTwo) -> {
                logger.info("> initializing tempMap forEach " + keyTwo + " " + valueTwo);
                resMap.put(key.getValue() + keyTwo.getValue(), new Route[]{fare.getRoute(), new Route(fare.getRoute().getAirportTo(), valueTwo.get(0).getRoute().getAirportTo(), null)});
                logger.info("ResMap = " + resMap);
            });
        });
        logger.info("Returning resMap " + resMap.values());
        return resMap;
    }
}
