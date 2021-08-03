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
        if (dest.length() == 3 && orig.length() == 3) return check(orig, dest);
        List<String> origList, destList;
        final List<String> UKRAINE_LIST = List.of("KBP", "HRK", "ODS", "LWO", "KHE");
        final List<String> SPAIN_LIST = List.of("ALC", "LEI", "BCN", "GRO", "REU", "BIO", "VLC", "CDT", "FUE", "GCA", "IBZ", "XRY", "ACE", "MAD",
                "AGP", "MAH", "RMU", "PMI", "SDR", "SVQ", "TFN", "TFN", "SCQ", "VLC", "VLL", "VGO", "VIT", "ZAZ");
        final List<String> AUSTRIA_LIST = List.of("VIE", "SZG");
        final List<String> BELGIUM_LIST = List.of("CRL", "BRU");
        final List<String> BOSNIA_LIST = List.of("BNX", "TZL");
        final List<String> BULGARIA_LIST = List.of("BOJ", "PDV", "SOF", "VAR");
        final List<String> CROATIA_LIST = List.of("");
        final List<String> CYPRUS_LIST = List.of("LCA", "PFO");
        final List<String> CZECHIA_LIST = List.of("");
        final List<String> DENMARK_LIST = List.of("");
        final List<String> ESTONIA_LIST = List.of("TLL");
        final List<String> FINLAND_LIST = List.of("");
        final List<String> FRANCE_LIST = List.of("");
        final List<String> GERMANY_LIST = List.of("");
        final List<String> GREECE_LIST = List.of("");
        final List<String> HUNGARY_LIST = List.of("BUD");
        final List<String> IRELAND_LIST = List.of("");
        final List<String> ISRAEL_LIST = List.of("TLV");
        final List<String> ITALY_LIST = List.of("");
        final List<String> JORDAN_LIST = List.of("");
        final List<String> LATVIA_LIST = List.of("RIX");
        final List<String> LITHUANIA_LIST = List.of("VNO", "KUN", "PLQ");
        final List<String> LUXEMBOURG_LIST = List.of("LUX");
        final List<String> MALTA_LIST = List.of("MLA");
        final List<String> MONTENEGRO_LIST = List.of("");
        final List<String> MOROCCO_LIST = List.of("");
        final List<String> NETHERLANDS_LIST = List.of("");
        final List<String> NORWAY_LIST = List.of("OSL", "TRP");
        final List<String> POLAND_LIST = List.of("");
        final List<String> PORTUGAL_LIST = List.of("");
        final List<String> ROMANIA_LIST = List.of("");
        final List<String> SERBIA_LIST = List.of("NIS");
        final List<String> SLOVAKIA_LIST = List.of("BTS", "KSC");
        final List<String> SWEDEN_LIST = List.of("");
        final List<String> SWITZERLAND_LIST = List.of("BSL", "GVA", "ZRH");
        final List<String> TURKEY_LIST = List.of("BJV", "DLM");
        final List<String> UK_LIST = List.of("");

        switch (orig) {
            case "UA" -> origList = UKRAINE_LIST;
            case "ES" -> origList = SPAIN_LIST;
            default -> origList = new ArrayList<>(List.of(orig));
        }
        switch (dest) {
            case "UA" -> destList = UKRAINE_LIST;
            case "ES" -> destList = SPAIN_LIST;
            default -> destList = new ArrayList<>(List.of(dest));
        }

        return check(origList, destList);

    }

    public Map<RouteFare[], Double> check(String orig, String dest) {
        Map<RouteFare[], Double> map = new HashMap<>();

        //logger.info("initializing takeMeTo with " + orig + " " + dest);
        List<String> connectingAirports = checkConnectingAirports(orig, dest);
        //logger.info("found connecting airports: " + connectingAirports.toString());
        for (String ap : connectingAirports) {
            logger.info("loop " + ap);
            map.putAll(doTeleport(getFares(orig, ap), dest));
        }

        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public Map<RouteFare[], Double> check(List<String> orig, List<String> dest) {
        Map<RouteFare[], Double> map = new HashMap<>();

        orig.forEach(value -> dest.stream().map(s -> check(value, s)).forEach(map::putAll));

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
        //logger.info("getFares " + orig + " " + dest);
        List<RouteFare> list = new FaresLogic().allFaresForRoute(orig, dest).getOutbound().getListOfFaresWithoutEmptyValues();
        //logger.info(list.toString());

        return list;
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
//                    logger.info(routeFare.getDepartureDate().toString());
//                    logger.info(routeFare.getArrivalDate().toString());
//                    logger.info(routeFare2.getDepartureDate().toString());
//                    logger.info(String.valueOf(connectionTimeMinutes));
//                    logger.info((int) connectionTimeMinutes/60 + "h " + connectionTimeMinutes%60 + "m");
//                    logger.info(String.valueOf(routeFare.getPrice().eqInEuros() + routeFare2.getPrice().eqInEuros()));
//                    logger.info("---------");
                }
            }

        }));
        logger.info("Returning resMap " + resMap.keySet().size());
        return resMap;
    }
}
