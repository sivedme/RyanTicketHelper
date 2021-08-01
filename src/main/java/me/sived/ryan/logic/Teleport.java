package me.sived.ryan.logic;

import me.sived.ryan.models.Route;
import me.sived.ryan.service.RyanService;
import me.sived.ryan.service.RyanServiceImpl;
import me.sived.ryan.logic.AllFaresForRouteLogic.RouteFareJson.Outbound.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class Teleport {

    RyanService ryanService = new RyanServiceImpl();
    Logger logger = LoggerFactory.getLogger(Teleport.class);

    public Map<Double, Route[]> takeMeTo(String orig, String dest) {
        logger.info("initializing takeMeTo with " + orig + " " + dest);
        List<String> connectingAirports = checkConnectingAirports(orig, dest);
        logger.info("found connecting airports: " + connectingAirports.toString());
        for (String ap : connectingAirports) {
            logger.info("loop " + ap);
            return doSomething(getFares(orig, ap), dest);
        }

        return null;
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

    Map<RouteFare.Price, ArrayList<RouteFare>> getFares(String orig, String dest) {
        logger.info("getFares " + orig + " " + dest);
        Map<RouteFare.Price, ArrayList<RouteFare>> map = new AllFaresForRouteLogic().fromAndTo(orig, dest).getOutbound().clean();
        logger.info(map.values().toString());

        return map;
    }

    Map<Double, Route[]> doSomething(Map<RouteFare.Price, ArrayList<RouteFare>> listOne, String destAirport) {
        logger.info("doSomething  " + listOne.values() + " " + destAirport);
        TreeMap<Double, Route[]> resMap = new TreeMap<>();

        listOne.forEach((key, value) -> {
            logger.info("initializing list forEach " + key.value + " " + value);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            RouteFare fare = value.get(0);
            logger.info("fare = " + fare);
            Date date = fare.getArrivalDate();
            Date secDate = new Date(date.getTime()+172800000);
            logger.info("dates: " + date + " " + secDate);
            AllFaresForRouteLogic.RouteFareJson fj = new AllFaresForRouteLogic().fromAndToWithDates(fare.getArrAirport(), destAirport, format.format(date), format.format(secDate));
            logger.info("fj values = " + fare.getArrAirport() + " " + destAirport + " " + format.format(date) + " " + format.format(secDate));
            if (fj == null) return;
            Map<RouteFare.Price, ArrayList<RouteFare>> tempMap = fj.getRouteFareMap();
            logger.info("tempMap " + tempMap.toString());
            tempMap.forEach((keyTwo, valueTwo) -> {
                logger.info("> initializing tempMap forEach " + keyTwo + " " + valueTwo);
                resMap.put(key.getValue() + keyTwo.getValue(), new Route[]{new Route(fare.getDepAirport(), fare.getArrAirport(), null), new Route(fare.getArrAirport(), valueTwo.get(0).getArrAirport(), null)});
                logger.info("ResMap = " + resMap.toString());
            });
        });
        logger.info("Returning resMap " + resMap.values());
        return resMap;
    }
}
