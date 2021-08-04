package me.sived.ryan.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RouteFare {
        String day;
        Date arrivalDate, departureDate;
        Price price;
        Route route;
        String label;

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

        public Date getArrivalDate() {
            return arrivalDate;
        }

        public Date getDepartureDate() {
            return departureDate;
        }

        public String getStringDateAndTime() {
            SimpleDateFormat format1 = new SimpleDateFormat("dd.MM HH:mm");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
            return format1.format(departureDate) + " – " + format2.format(arrivalDate);
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
        public String toString() {
            return price == null ? null : getDayAndMonth();

        }
    }