package algonquin.cst2335.cst2335_finalproject.flight;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Flight.class}, version = 1)
public abstract class FlightDatabase extends RoomDatabase {

    public abstract FlightDAO cmDAO();

}
