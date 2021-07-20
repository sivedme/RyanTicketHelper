package me.sived.ryan.logic;

import com.google.gson.GsonBuilder;
import me.sived.ryan.models.Route;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.*;

public class AllFaresLogic {

    static HttpClient httpClient = HttpClient.newHttpClient();

    public List from(String airportCode) {

        Route[] routes = new RoutesLogic().from(airportCode);
        ArrayList<RouteFareJson> fares = new ArrayList<>();

        Arrays.stream(routes).forEach(iterator ->
        {
            try {
                RouteFareJson temp = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create().fromJson(
                        httpClient.send(HttpRequest.newBuilder().GET().uri(URI.create("https://www.ryanair.com/api/farfnd/3/oneWayFares/" + iterator.getAirportFrom() + "/" + iterator.getAirportTo() + "/cheapestPerDay?outboundDateFrom=2021-07-01&outboundDateTo=2021-12-31")).build(),
                                HttpResponse.BodyHandlers.ofString()).body(), RouteFareJson.class);
                fares.add(temp);
                System.out.println(iterator.getAirportFrom() + " - " + iterator.getAirportTo());
                System.out.println(fares.get(fares.size()-1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return fares;
    }


    public RouteFareJson fromAndTo(String dep, String arr) {

        try {
            RouteFareJson temp = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create().fromJson(
                    httpClient.send(HttpRequest.newBuilder().GET().uri(URI.create("https://www.ryanair.com/api/farfnd/3/oneWayFares/" + dep + "/" + arr + "/cheapestPerDay?outboundDateFrom=2021-09-01&outboundDateTo=2023-02-01")).build(),
                            HttpResponse.BodyHandlers.ofString()).body(), RouteFareJson.class);
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class RouteFareJson {
        Outbound outbound;

        private static class Outbound {
            RouteFare[] fares;
            TreeMap<Double, ArrayList<RouteFare>> map = new TreeMap<>();

            private static class RouteFare {
                String day;
                Date arrivalDate, departureDate;
                Price price;

                private static class Price {
                    double value;
                    String currencyCode, currencySymbol;

                    public double getValue() {
                        return value;
                    }

                    public double getRoundedValue() {
                        return Math.ceil(value);
                    }

                    @Override
                    public String toString() {
                        return currencySymbol + value;
                    }
                    // €19.99
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

            @Override
            public String toString() {
                return clean();
            }

            public String clean() {
                for (RouteFare fare : fares) {
                    if (fare.toString() != null) {
                        double value = fare.price.getRoundedValue();

                        if (map.containsKey(value)) {
                            map.get(value).add(fare);
                        } else {
                            ArrayList<RouteFare> temp = new ArrayList<>();
                            temp.add(fare);
                            map.put(value, temp);
                        }
                    }
                }

                StringBuilder builder = new StringBuilder();
                map.forEach((key, value) -> {
                    builder.append(map.get(key).get(0).getPrice().currencySymbol + key.intValue()).append(" -> ").append(map.get(key)).append("\n");
                });
                return builder.toString();
            }
        }

        public Outbound getOutbound() {
            return outbound;
        }

        @Override
        public String toString() {
            return outbound.toString();
        }
    }


}
