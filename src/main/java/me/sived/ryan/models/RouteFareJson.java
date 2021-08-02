package me.sived.ryan.models;

import java.util.ArrayList;
import java.util.TreeMap;

public class RouteFareJson {
    Outbound outbound;

    public Outbound getOutbound() {
        return outbound;
    }

    public TreeMap<Price, ArrayList<RouteFare>> getRouteFareMap() {
        return getOutbound().clean();
    }


    @Override
    public String toString() {
        return "RouteFareJson{" +
                "outbound=" + outbound +
                '}';
    }
}
