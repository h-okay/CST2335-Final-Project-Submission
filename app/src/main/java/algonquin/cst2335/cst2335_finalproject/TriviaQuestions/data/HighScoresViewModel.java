package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.AppDatabase;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.UserScore;

public class HighScoresViewModel extends AndroidViewModel {

    private LiveData<List<UserScore>> highScores;
    private AppDatabase appDatabase;

    public HighScoresViewModel(Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(application);
        highScores = appDatabase.userScoreDao().getTopScores();
    }

    public LiveData<List<UserScore>> getHighScores() {
        return highScores;
    }

    // New function
    public void getHighScoresData() {
        highScores.observeForever(new Observer<List<UserScore>>() {
            @Override
            public void onChanged(List<UserScore> userScores) {

            }
        });
    }
}
