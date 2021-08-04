package me.sived.ryan.models;

public class Route {
        String airportFrom, airportTo;
        String connectingAirport;

        public Route(String airportFrom, String airportTo, String connectingAirport) {
            this.airportFrom = airportFrom;
            this.airportTo = airportTo;
            this.connectingAirport = connectingAirport;
        }

        public String getAirportFrom() {
            return airportFrom;
        }

        public String getAirportTo() {
            return airportTo;
        }

    public String getConnectingAirport() {
        return connectingAirport;
    }

    @Override
        public String toString() {
            return airportFrom + "-" + airportTo;
        }
}