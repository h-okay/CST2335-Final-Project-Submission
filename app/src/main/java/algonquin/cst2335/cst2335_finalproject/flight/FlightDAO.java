package algonquin.cst2335.cst2335_finalproject.flight;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
/**
 * Data Access Object (DAO) interface for managing Flight entities in the database.
 * This interface provides methods for inserting, querying, and deleting Flight objects.
 */
@Dao
public interface FlightDAO {

    /**
     * Inserts a new Flight object into the database.
     * @param flight The Flight object to be inserted.
     * @return The unique identifier (primary key) of the newly inserted Flight object.
     */
    @Insert
    public long insertFlight(Flight flight);

    /**
     * Retrieves a list of all Flight objects from the database.
     * @return A list of Flight objects representing all flights in the database.
     */
    @Query("Select * from Flight")
    public List<Flight> getFlights();

    /**
     * Deletes a Flight object from the database.
     * @param flight The Flight object to be deleted.
     */
    @Delete
    void deleteFlight(Flight flight);
}
