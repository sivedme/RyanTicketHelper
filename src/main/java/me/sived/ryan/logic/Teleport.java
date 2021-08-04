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
    CountryList countryList = new CountryList();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public Map<RouteFare[], Double> takeMeTo(String orig, String dest, String dateFrom, String dateTo) {
        if (dest.length() == 3 && orig.length() == 3) return check(orig, dest, dateFrom, dateTo);

        return check(countryList.getFor(orig), countryList.getFor(dest), dateFrom, dateTo);
    }

    public Map<RouteFare[], Double> takeMeTo(String orig, String dest) {
        return takeMeTo(orig, dest, format.format(new Date()), "2022-12-31");
    }

    public Map<RouteFare[], Double> takeMeToAndBack(String orig, String dest, String dateFrom, String dateTo, String length) {
        Map<RouteFare[], Double> owResultMap = takeMeTo(orig, dest, dateFrom, dateTo);
        Map<RouteFare[], Double> rtResultMap = new LinkedHashMap<>();


        owResultMap.entrySet().parallelStream().forEach(entry -> {
            Date ndFrom = new Date(entry.getKey()[1].getArrivalDate().getTime() + (86_400_000L * Integer.parseInt(length.split(",")[0])));
            Date ndTo = new Date(entry.getKey()[1].getArrivalDate().getTime() + (86_400_000L * Integer.parseInt(length.split(",")[1])));
            Map<RouteFare[], Double> temp = takeMeTo(entry.getKey()[1].getRoute().getAirportTo(), entry.getKey()[0].getRoute().getAirportFrom(), format.format(ndFrom), format.format(ndTo));
            if (temp.isEmpty()) return;
            for (Map.Entry<RouteFare[], Double> entry1 :
                    temp.entrySet()) {
                rtResultMap.put(new RouteFare[]{entry.getKey()[0], entry.getKey()[1], entry1.getKey()[0], entry1.getKey()[1]}, entry.getValue() + entry1.getValue());
            }
        });

//        owResultMap.forEach((key, value) -> {
//            Date ndFrom = new Date(key[1].getArrivalDate().getTime() + (86_400_000L * Integer.parseInt(length.split(",")[0])));
//            Date ndTo = new Date(key[1].getArrivalDate().getTime() + (86_400_000L * Integer.parseInt(length.split(",")[1])));
//            Map<RouteFare[], Double> temp = takeMeTo(key[1].getRoute().getAirportTo(), key[0].getRoute().getAirportFrom(), format.format(ndFrom), format.format(ndTo));
//            if (temp.isEmpty()) return;
//            for (Map.Entry<RouteFare[], Double> entry1 :
//                    temp.entrySet()) {
//                rtResultMap.put(new RouteFare[]{key[0], key[1], entry1.getKey()[0], entry1.getKey()[1]}, value + entry1.getValue());
//            }
//        });
        return rtResultMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public Map<RouteFare[], Double> check(String orig, String dest, String dateFrom, String dateTo) {
        Map<RouteFare[], Double> map = new HashMap<>();

        //logger.info("initializing takeMeTo with " + orig + " " + dest);
        List<String> connectingAirports = checkConnectingAirports(orig, dest);
        //logger.info("found connecting airports: " + connectingAirports.toString());
        for (String ap : connectingAirports) {
            logger.info("loop " + ap);
            map.putAll(doTeleport(getFares(orig, ap, dateFrom, dateTo), dest));
        }

        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public Map<RouteFare[], Double> check(List<String> orig, List<String> dest, String dateFrom, String dateTo) {
        Map<RouteFare[], Double> map = new HashMap<>();

        orig.forEach(value -> dest.stream().map(s -> check(value, s, dateFrom, dateTo)).forEach(map::putAll));

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

    List<RouteFare> getFares(String orig, String dest, String dateFrom, String dateTo) {
        return new FaresLogic().allFaresForRouteAndDates(orig, dest, dateFrom, dateTo).getOutbound().getListOfFaresWithoutEmptyValues();
    }

    Map<RouteFare[], Double> doTeleport(List<RouteFare> listOne, String destAirport) {
        logger.info("doTeleport  " + listOne + " " + destAirport);
        Map<RouteFare[], Double> resMap = new HashMap<>();
        listOne.parallelStream().forEach((routeFare -> {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            //logger.info("initializing list forEach " + routeFare.getRoute().toString() + " " + routeFare.getPrice().getStringValue(true));
            Date date = routeFare.getArrivalDate();
            Date secDate = new Date(routeFare.getDepartureDate().getTime() + 86_400_000); //24h
            RouteFareJson fj = new FaresLogic().allFaresForRouteAndDates(routeFare.getRoute().getAirportTo(), destAirport, format.format(date), format.format(secDate));
            //logger.info("fj values = " + routeFare.getRoute().getAirportTo() + " " + destAirport + " " + format.format(date) + " " + format.format(secDate));
            if (fj == null) return;
            RouteFare[] fares = fj.getOutbound().getFares();
            //logger.info("fares " + Arrays.toString(fares));
            for (RouteFare routeFare2 : fares) {
                if (routeFare.toString() == null) continue;
                if (routeFare2.toString() == null) continue;
                if (routeFare2.getDepartureDate().after(new Date(routeFare.getArrivalDate().getTime()+(55*60000)))) {
                    resMap.put(new RouteFare[]{routeFare, routeFare2}, routeFare.getPrice().eqInEuros() + routeFare2.getPrice().eqInEuros());
                    long connectionTimeMinutes = (routeFare2.getDepartureDate().getTime()-routeFare.getArrivalDate().getTime())/60000;
                    routeFare2.setLabel((int) connectionTimeMinutes/60 + "h " + connectionTimeMinutes%60 + "m");
                }
            }

        }));
        logger.info("Returning resMap " + resMap.keySet().size());
        return resMap;
    }
}
