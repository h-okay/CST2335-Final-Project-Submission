package algonquin.cst2335.cst2335_finalproject;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FlightDAO {

    @Insert
    public long insertFlight(Flight flight);

    @Query("Select * from Flight")
    public List<Flight> getFlights();

    @Delete
    void deleteFlight(Flight flight);
}
