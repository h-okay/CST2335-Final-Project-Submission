package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class HighScoresViewModel extends AndroidViewModel {

    private LiveData<List<UserScore>> highScores;
    private AppDatabase appDatabase;
    private UserScoreDAO userScoreDao;

    public HighScoresViewModel(Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(application);
        userScoreDao = appDatabase.userScoreDao();
        highScores = userScoreDao.getTopScores();
    }

    public LiveData<List<UserScore>> getHighScores() {
        return highScores;
    }

    public void insertUserScore(UserScore userScore) {
        new Thread(() -> userScoreDao.insert(userScore)).start();
    }

    public void deleteUserScore(UserScore userScore) {
        new Thread(() -> userScoreDao.delete(userScore)).start();
    }
}
