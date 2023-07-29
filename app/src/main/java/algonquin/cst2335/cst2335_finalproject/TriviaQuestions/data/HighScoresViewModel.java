/**
 * HighScoresViewModel.java
 * <p>
 * The HighScoresViewModel class is an AndroidViewModel that acts as an interface between the
 * HighScoresFragment and the data layer of the app, represented by the UserScoreDAO and AppDatabase.
 * This class provides methods for accessing the high scores in the database and for
 * inserting and deleting UserScore objects.
 *
 * @author Ahmed Almutawakel
 * @version 1.0
 * @since JDK 20
 */
package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * The HighScoresViewModel class is an AndroidViewModel that acts as an interface between the
 * HighScoresFragment and the data layer of the app, represented by the UserScoreDAO and AppDatabase.
 */
public class HighScoresViewModel extends AndroidViewModel {

    private final LiveData<List<UserScore>> highScores;
    private final UserScoreDAO userScoreDao;

    /**
     * Creates a new instance of the HighScoresViewModel.
     * It initializes the local database instance, the data access object, and the LiveData object.
     *
     * @param application The application context. AndroidViewModel holds a strong reference to the application.
     */
    public HighScoresViewModel(Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        userScoreDao = appDatabase.userScoreDao();
        highScores = userScoreDao.getTopScores();
    }

    /**
     * Returns a LiveData object containing a list of UserScore objects.
     * The returned LiveData object is observed by the HighScoresFragment to update the UI.
     *
     * @return a LiveData object containing a list of UserScore objects.
     */
    public LiveData<List<UserScore>> getHighScores() {
        return highScores;
    }

    /**
     * Inserts a UserScore object into the local database.
     * The operation is performed on a new thread.
     *
     * @param userScore The UserScore object to be inserted into the database.
     */
    public void insertUserScore(UserScore userScore) {
        new Thread(() -> userScoreDao.insert(userScore)).start();
    }

    /**
     * Deletes a UserScore object from the local database.
     * The operation is performed on a new thread.
     *
     * @param userScore The UserScore object to be deleted from the database.
     */
    public void deleteUserScore(UserScore userScore) {
        new Thread(() -> userScoreDao.delete(userScore)).start();
    }
}
