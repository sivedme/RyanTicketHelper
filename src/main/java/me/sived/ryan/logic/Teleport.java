package me.sived.ryan.logic;

import me.sived.ryan.models.*;
import me.sived.ryan.service.RyanService;
import me.sived.ryan.service.RyanServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Teleport {

    RyanService ryanService = new RyanServiceImpl();
    Logger logger = LoggerFactory.getLogger(Teleport.class);

    public Map<RouteFare[], Double> takeMeTo(String orig, String dest) {
        Map<RouteFare[], Double> map = new HashMap<>();

        logger.info("initializing takeMeTo with " + orig + " " + dest);
        List<String> connectingAirports = checkConnectingAirports(orig, dest);
        logger.info("found connecting airports: " + connectingAirports.toString());
        for (String ap : connectingAirports) {
            logger.info("loop " + ap);
            map.putAll(doTeleport(getFares(orig, ap), dest));
        }

        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }


    List<String> checkConnectingAirports(String orig, String dest) {
        List<Route> routesDest = ryanService.getRoutesFrom(dest);  //ATH -> ACE, AGP...
        List<Route> routesOrig = ryanService.getRoutesFrom(orig); //KHE -> KRK
        ArrayList<String> list = new ArrayList<>();

        for (Route destination : routesDest) {
            for (Route origin : routesOrig) {
                if (origin.getAirportTo().equals(destination.getAirportTo())) {
                    list.add(destination.getAirportTo());
                }

            }
        }

        return list;
    }

    List<RouteFare> getFares(String orig, String dest) {
        logger.info("getFares " + orig + " " + dest);
        List<RouteFare> list = new FaresLogic().allFaresForRoute(orig, dest).getOutbound().getListOfFaresWithoutEmptyValues();
        logger.info(list.toString());

        return list;
    }

    Map<RouteFare[], Double> doTeleport(List<RouteFare> listOne, String destAirport) {
        logger.info("doTeleport  " + listOne + " " + destAirport);
        Map<RouteFare[], Double> resMap = new HashMap<>();
        listOne.parallelStream().forEach((routeFare -> {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            logger.info("initializing list forEach " + routeFare.getRoute().toString() + " " + routeFare.getPrice().getStringValue(true));
            Date date = routeFare.getArrivalDate();
            Date secDate = new Date(date.getTime() + 86_400_000); //24h
            RouteFareJson fj = new FaresLogic().allFaresForRouteAndDates(routeFare.getRoute().getAirportTo(), destAirport, format.format(date), format.format(secDate));
            logger.info("fj values = " + routeFare.getRoute().getAirportTo() + " " + destAirport + " " + format.format(date) + " " + format.format(secDate));
            if (fj == null) return;
            RouteFare[] fares = fj.getOutbound().getFares();
            logger.info("fares " + Arrays.toString(fares));
            for (RouteFare routeFare2 : fares) {
                if (routeFare.toString() == null) continue;
                if (routeFare2.toString() == null) continue;
                if (routeFare2.getDepartureDate().after(new Date(routeFare.getArrivalDate().getTime()+(40*60000)))) {
                    resMap.put(new RouteFare[]{routeFare, routeFare2}, routeFare.getPrice().eqInEuros() + routeFare2.getPrice().eqInEuros());
                }
            }

        }));
        logger.info("Returning resMap " + resMap.keySet().size());
        return resMap;
    }
}
