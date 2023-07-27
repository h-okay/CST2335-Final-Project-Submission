package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserScore.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserScoreDAO userScoreDao();

    private static volatile AppDatabase INSTANCE;

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
