package me.sived.ryan.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TreeMap;

public class Outbound {
    RouteFare[] fares;
    TreeMap<RouteFare.Price, ArrayList<RouteFare>> map = new TreeMap<>();
    Logger logger = LoggerFactory.getLogger(Outbound.class);

    public class RouteFare {
        String day;
        Date arrivalDate, departureDate;
        Price price;
        Route route;

        public RouteFare(Route route, Price price, Date departureDate, Date arrivalDate) {
            this.arrivalDate = arrivalDate;
            this.departureDate = departureDate;
            this.price = price;
            this.route = route;
        }

        public Route getRoute() {
            return route;
        }

        public void setRoute(Route route) {
            this.route = route;
        }



        public class Price implements Comparable {
            double value;
            String currencyCode, currencySymbol;

            public Price(double value, String currencySymbol) {
                this.value = value;
                this.currencySymbol = currencySymbol;
            }

            public Price(double value) {
                this.value = value;
                this.currencySymbol = "€";
            }

            public double getValue() {
                return value;
            }

            public String getStringValue(boolean rounded) {
                return currencySymbol + (rounded ? (int) Math.ceil(value) : value);      // €19.99
            }

            @Override
            public int compareTo(Object o) {
                Price p = (Price) o;

                return Double.compare(this.getValue(), p.getValue());
            }
        }

        public Date getArrivalDate() {
            return arrivalDate;
        }

        public Date getDepartureDate() {
            return departureDate;
        }

        public Price getPrice() {
            return price;
        }

        public String getDay() {
            return day;
        }

        public String getDayAndMonth() {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM");
            return format.format(getDepartureDate());
        }


        @Override
        public String toString() {
            //return price == null ? null : getDayAndMonth() + " " + price;
            return price == null ? null : getDayAndMonth();

        }
        // 2021-09-07 €19.99 or null
    }

    public RouteFare[] getFares() {
        return fares;
    }

    public TreeMap<RouteFare.Price, ArrayList<RouteFare>> getMap() {
        logger.info("return map with size " + map.size());
        return map;
    }

    public TreeMap<RouteFare.Price, ArrayList<RouteFare>> clean() {

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