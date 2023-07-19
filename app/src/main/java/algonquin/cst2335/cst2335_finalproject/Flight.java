package algonquin.cst2335.cst2335_finalproject;

public class Flight {

    protected String departureAirport;
    protected String flightNumber;
    protected String delay;
    protected String gate;
    protected String terminal;
    protected String destination;


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
