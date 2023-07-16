package algonquin.cst2335.cst2335_finalproject;

public class Flight {

    private String flightNumber;
    private String departureTime;
    private String destination;

    public Flight(String flightNumber, String departureTime, String destination) {
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.destination = destination;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getDestination() {
        return destination;
    }
}
