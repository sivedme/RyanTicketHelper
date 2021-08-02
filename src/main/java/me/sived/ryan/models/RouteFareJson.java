package me.sived.ryan.models;

import java.util.ArrayList;
import java.util.TreeMap;

import me.sived.ryan.models.Outbound;
import me.sived.ryan.models.Outbound.RouteFare;
import me.sived.ryan.models.Outbound.RouteFare.Price;

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
