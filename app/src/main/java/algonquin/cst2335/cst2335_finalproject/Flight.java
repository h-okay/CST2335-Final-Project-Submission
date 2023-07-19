package algonquin.cst2335.cst2335_finalproject;

public class Flight {

    private String departureAirport;

    private String flightNumber;
    private String delay;
    private String gate;
    private String terminal;
    private String destination;


    public Flight(String departureAirport, String flightNumber, String delay, String gate, String terminal, String destination) {
        this.departureAirport = departureAirport;
        this.flightNumber = flightNumber;
        this.delay = delay;
        this.gate = gate;
        this.terminal = terminal;
        this.destination = destination;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDelay() {return delay; }

    public String getGate() {
        return gate;
    }

    public String getTerminal() {
        return terminal;
    }

    public String getDestination() {
        return destination;
    }
}
