package me.sived.ryan.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class Outbound {
    RouteFare[] fares;
    TreeMap<Price, ArrayList<RouteFare>> map = new TreeMap<>();
    Logger logger = LoggerFactory.getLogger(Outbound.class);

    public RouteFare[] getFares() {
        return fares;
    }

    public TreeMap<Price, ArrayList<RouteFare>> getMap() {
        logger.info("returning map with size " + map.size());
        return map;
    }

    public List<RouteFare> getListOfFaresWithoutEmptyValues() {
        List<RouteFare> list = new ArrayList<>();
        Arrays.stream(fares).forEach(routeFare -> {
            if (routeFare.toString() != null) list.add(routeFare);
        });
        return list;
    }

    public TreeMap<Price, ArrayList<RouteFare>> clean() {

        for (RouteFare fare : fares) {
            if (fare.toString() != null) {

                if (map.containsKey(fare.price)) {
                    map.get(fare.price).add(fare);
                } else {
                    ArrayList<RouteFare> temp = new ArrayList<>();
                    temp.add(fare);
                    map.put(fare.price, temp);
                }
            }
        }

        return getMap();
    }



    @Override
    public String toString() {
        return "Outbound{" +
                "fares=" + Arrays.toString(fares) +
                ", map=" + map +
                '}';
    }
}