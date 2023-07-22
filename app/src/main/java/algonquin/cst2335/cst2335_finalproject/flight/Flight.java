package algonquin.cst2335.cst2335_finalproject.flight;

public class Flight {

    protected String departureAirport;

    protected String flightNumber;
    protected String flightName;
    protected String destination;

    public Flight(String departureAirport, String flightNumber, String flightName, String destination) {
        this.departureAirport = departureAirport;
        this.flightNumber = flightNumber;
        this.flightName = flightName;
        this.destination = destination;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getFlightName() {
        return flightName;
    }

    public String getDestination() {
        return destination;
    }
}
