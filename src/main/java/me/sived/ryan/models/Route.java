package me.sived.ryan.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Route {
        String airportFrom, airportTo;

        public Route(String airportFrom, String airportTo) {
            this.airportFrom = airportFrom;
            this.airportTo = airportTo;
        }

        public String getAirportFrom() {
            return airportFrom;
        }

        public String getAirportTo() {
            return airportTo;
        }

        @Override
        public String toString() {
            return airportFrom + "-" + airportTo;
        }
}