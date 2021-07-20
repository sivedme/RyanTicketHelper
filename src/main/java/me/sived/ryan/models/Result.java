package me.sived.ryan.models;

import java.text.SimpleDateFormat;
import java.util.*;

public class Result {
    public Flight[] fares;

    private static class Flight {
        public Outbound outbound;

        private static class Outbound {
            Airport departureAirport, arrivalAirport;
            Date departureDate, arrivalDate;
            FlightPrice price;

            private static class Airport {
                public String countryName, iataCode, name;
            }

        }

        public String getFlightPriceString() {
            FlightPrice price = outbound.price;
            return price.currencySymbol + (int) Math.ceil(price.value);
        }

        public String getArrivalAirportString() {
            Outbound.Airport airport = outbound.arrivalAirport;
            return airport.name + ", " + airport.countryName + " (" + airport.iataCode + ")";
        }

        public String getSimpleDateString() {
            return new SimpleDateFormat("dd.MM HH:mm").format(outbound.departureDate);
        }


        private static class FlightPrice {
            double value;
            String currencySymbol;
        }

        public Outbound.Airport getDepartureAirport() {
            return outbound.departureAirport;
        }

        public Outbound.Airport getArrivalAirport() {
            return outbound.arrivalAirport;
        }

        public String getTableInfo() {
            return String.format("%-7s %50s %30s", getFlightPriceString(), getArrivalAirportString(), getSimpleDateString() + "\n");
        }
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        for (Flight fare : fares) {
            builder.append(fare.getTableInfo());
        }
        return builder.toString();
    }

    public List<Flight> toList() {
        List<Flight> list = new ArrayList<>();
        Collections.addAll(list, fares);
        return list;
    }
}

