package me.sived.ryan.models;

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

    public double eqInEuros() {
        return switch (currencySymbol) {
            case "€" -> value;
            case "zł" -> value * 0.22;
            case "£" -> value * 1.17;
            case "DKK" -> value * 0.13;
            case "MAD" -> value * 0.095;
            case "NOK" -> value * 0.095;
            case "Ft" -> value * 0.028;
            case "Kč" -> value * 0.039;
            case "CHF" -> value * 0.93;
            case "kr" -> value * 0.098;
            default -> value * 10000;
        };
    }

    @Override
    public int compareTo(Object o) {
        Price p = (Price) o;

        return Double.compare(this.getValue(), p.getValue());
    }
}