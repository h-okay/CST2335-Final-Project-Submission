package algonquin.cst2335.cst2335_finalproject.flight;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Represents a flight with various attributes such as departure airport, flight number, delay,
 * terminal, gate, and destination.
 * This class is used to store flight information and is annotated with Room annotations for
 * database handling.
 */
@Entity
public class Flight {
    /**
     * The unique identifier for the flight in the database.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    /**
     * The departure airport of the flight.
     */
    @ColumnInfo(name = "departureAirport")
    protected String departureAirport;
    /**
     * The flight number.
     */
    @ColumnInfo(name = "flightNumber")
    protected String flightNumber;
    /**
     * The delay status of the flight.
     */
    @ColumnInfo(name = "delay")
    protected String delay;
    /**
     * The gate number for the flight.
     */
    @ColumnInfo(name = "gate")
    protected String gate;
    /**
     * The terminal number for the flight.
     */
    @ColumnInfo(name = "terminal")
    protected String terminal;
    /**
     * The destination of the flight.
     */
    @ColumnInfo(name = "destination")
    protected String destination;

    /**
     * Constructs a new Flight object with the provided information.
     *
     * @param departureAirport The departure airport of the flight.
     * @param flightNumber     The flight number.
     * @param delay            The delay status of the flight.
     * @param terminal         The terminal number for the flight.
     * @param gate             The gate number for the flight.
     * @param destination      The destination of the flight.
     */
    public Flight(String departureAirport, String flightNumber, String delay, String terminal, String gate, String destination) {
        this.departureAirport = departureAirport;
        this.flightNumber = flightNumber;
        this.delay = delay;
        this.gate = gate;
        this.terminal = terminal;
        this.destination = destination;
    }

    /**
     * Gets the departure airport of the flight.
     *
     * @return The departure airport.
     */
    public String getDepartureAirport() {
        return departureAirport;
    }
    /**
     * Gets the flight number.
     *
     * @return The flight number.
     */
    public String getFlightNumber() {
        return flightNumber;
    }
    /**
     * Gets the delay status of the flight.
     *
     * @return The delay status.
     */
    public String getDelay() {
        return delay;
    }
    /**
     * Gets the gate number for the flight.
     *
     * @return The gate number.
     */
    public String getGate() {
        return gate;
    }
    /**
     * Gets the terminal number for the flight.
     *
     * @return The terminal number.
     */
    public String getTerminal() {
        return terminal;
    }
    /**
     * Gets the destination of the flight.
     *
     * @return The destination.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Checks if this Flight object is equal to another object.
     *
     * @param o The object to compare.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flight)) return false;
        Flight flight = (Flight) o;
        return flightNumber.equals(flight.flightNumber);
    }

    /**
     * Generates a hash code for this Flight object.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(flightNumber);
    }
}
