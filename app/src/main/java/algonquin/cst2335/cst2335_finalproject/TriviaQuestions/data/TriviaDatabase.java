package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Trivia.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TriviaDatabase extends RoomDatabase {

    public abstract TriviaDAO triviaDao();
    private static volatile TriviaDatabase INSTANCE;

    static TriviaDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TriviaDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TriviaDatabase.class, "trivia_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
