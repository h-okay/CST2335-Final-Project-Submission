/**
 * AppDatabase.java
 * <p>
 * The AppDatabase class is a RoomDatabase that provides access to the application's local database.
 * It uses Room library to create and manage the SQLite database for storing UserScore objects.
 *
 * @author Ahmed Almutawakel
 * @version 1.0
 * @since JDK 20
 */
package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import android.app.Application;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * This class serves as the main access point for the underlying SQLite database.
 * It uses the Room library to provide a clean abstraction over SQLite and handle object mapping.
 *
 * This class is annotated as a Room database and includes entities and its version.
 * It also extends the RoomDatabase class to be a part of Room persistence library.
 *
 * @see RoomDatabase
 * @see androidx.room.Dao
 * @see UserScore
 */
@Database(entities = {UserScore.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * Each DAO is accessed via an abstract getter method in the RoomDatabase.
     *
     * @return the DAO for the UserScore entities.
     */
    public abstract UserScoreDAO userScoreDao();

    private static volatile AppDatabase INSTANCE;

    /**
     * A singleton design pattern is used to ensure that only one instance of the database
     * is created across the entire application's lifecycle.
     *
     * This method returns the singleton instance of the database. If the database doesn't
     * exist yet, it builds a new one.
     *
     * @param application The Application instance associated with this process.
     * @return the singleton instance of AppDatabase
     */
    public static AppDatabase getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(application.getApplicationContext(),
                                    AppDatabase.class, "database-name")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
