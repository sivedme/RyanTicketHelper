package me.sived.ryan.logic;

import com.google.gson.GsonBuilder;
import me.sived.ryan.models.Route;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.*;

public class AllFaresForRouteLogic {

    static HttpClient httpClient = HttpClient.newHttpClient();

    public RouteFareJson fromAndTo(String dep, String arr) {

        try {
            RouteFareJson fareJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create().fromJson(
                    httpClient.send(HttpRequest.newBuilder().GET().uri(URI.create("https://www.ryanair.com/api/farfnd/3/oneWayFares/" + dep + "/" + arr + "/cheapestPerDay?outboundDateFrom=2021-09-01&outboundDateTo=2023-02-01")).build(),
                            HttpResponse.BodyHandlers.ofString()).body(), RouteFareJson.class);
            System.out.println(fareJson);
            return fareJson;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class RouteFareJson {
        Outbound outbound;

        private static class Outbound {
            RouteFare[] fares;
            TreeMap<RouteFare.Price, ArrayList<RouteFare>> map = new TreeMap<>();

            private static class RouteFare {
                String day;
                Date arrivalDate, departureDate;
                Price price;

                private static class Price implements Comparable {
                    double value;
                    String currencyCode, currencySymbol;

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

        }

        public Outbound getOutbound() {
            return outbound;
        }

        public TreeMap<Outbound.RouteFare.Price, ArrayList<Outbound.RouteFare>> getRouteFareMap() {
            return getOutbound().getMap();
        }

    }


}
