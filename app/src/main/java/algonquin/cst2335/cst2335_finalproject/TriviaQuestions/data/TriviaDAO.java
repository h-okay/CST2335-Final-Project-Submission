package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TriviaDAO {

    @Query("SELECT * FROM trivia_table")
    LiveData<List<Trivia>> getAllTrivia();

    @Insert
    void insert(Trivia trivia);

    @Query("DELETE FROM trivia_table")
    void deleteAll();
}
