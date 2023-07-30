/**
 * UserScoreDAO.java
 * <p>
 * The UserScoreDAO interface defines the operations that can be performed on the UserScore table in the Room database.
 * It includes operations for inserting a new UserScore object, retrieving the top 10 scores, and deleting a UserScore object.
 * <p>
 * This interface is used by Room to generate an implementation of the DAO.
 *
 * @author Ahmed Almutawakel
 * @version 1.0
 * @since JDK 20
 */
package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * The UserScoreDAO interface defines the operations that can be performed on the UserScore table in the Room database.
 */
@Dao
public interface UserScoreDAO {

    /**
     * Inserts a new UserScore object into the UserScore table.
     *
     * @param userScore The UserScore object to insert.
     */
    @Insert
    void insert(UserScore userScore);

    /**
     * Retrieves the top 10 scores from the UserScore table.
     *
     * @return A LiveData object containing a list of the top 10 UserScore objects.
     */
    @Query("SELECT * FROM UserScore ORDER BY score DESC LIMIT 10")
    LiveData<List<UserScore>> getTopScores();

    /**
     * Deletes a UserScore object from the UserScore table.
     *
     * @param userScore The UserScore object to delete.
     */
    @Delete
    void delete(UserScore userScore);
}
