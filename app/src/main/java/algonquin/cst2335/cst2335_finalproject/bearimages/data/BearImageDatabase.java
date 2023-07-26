/**
 * BearImageDatabase.java
 * <p>
 * The BearImageDatabase class represents the Room database for the BearImages application.
 * It defines the database entities and provides an abstract method to access the DAO (Data Access Object) for bear images.
 *
 * @author Hakan Okay
 * @version 1.0
 * @since JDK 20
 */

package algonquin.cst2335.cst2335_finalproject.bearimages.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * The BearImageDatabase class represents the Room database for the BearImages application.
 * It defines the database entities and provides an abstract method to access the DAO (Data Access Object) for bear images.
 */
@Database(entities = {BearImage.class}, version = 1)
public abstract class BearImageDatabase extends RoomDatabase {

    /**
     * Returns the BearImageDao to access the database operations for bear images.
     *
     * @return The BearImageDao to interact with the database.
     */
    public abstract BearImageDao bearImageDao();
}
