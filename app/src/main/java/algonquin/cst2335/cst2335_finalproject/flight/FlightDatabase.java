package algonquin.cst2335.cst2335_finalproject.flight;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * FlightDatabase is an abstract class that extends RoomDatabase and serves as the main access point
 * to the underlying SQLite database for managing Flight entities.
 * This class is annotated with @Database, which specifies the entities it manages and the database version.
 */
@Database(entities = {Flight.class}, version = 1)
public abstract class FlightDatabase extends RoomDatabase {

    /**
     * Abstract method to get the Data Access Object (DAO) for the Flight entity.
     * @return The FlightDAO instance used to interact with the Flight table in the database.
     */
    public abstract FlightDAO cmDAO();

}
