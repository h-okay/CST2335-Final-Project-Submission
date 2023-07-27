package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserScoreDAO {
    @Insert
    void insert(UserScore userScore);

    @Query("SELECT * FROM UserScore ORDER BY score DESC LIMIT 10")
    LiveData<List<UserScore>> getTopScores();

    @Delete
    void delete(UserScore userScore);
}
