package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserScore.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserScoreDAO userScoreDao();
}
