package algonquin.cst2335.cst2335_finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Flight {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    @ColumnInfo(name="departureAirport")
    protected String departureAirport;
    @ColumnInfo(name="flightNumber")
    protected String flightNumber;
    @ColumnInfo(name="delay")
    protected String delay;
    @ColumnInfo(name="gate")
    protected String gate;
    @ColumnInfo(name="terminal")
    protected String terminal;
    @ColumnInfo(name="destination")
    protected String destination;


    public Flight(String departureAirport, String flightNumber, String delay, String terminal,String gate, String destination) {
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
