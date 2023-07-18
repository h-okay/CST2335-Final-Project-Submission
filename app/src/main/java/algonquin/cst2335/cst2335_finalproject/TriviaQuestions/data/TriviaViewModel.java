package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TriviaViewModel extends AndroidViewModel {

    private TriviaRepository repository;
    private LiveData<List<Trivia>> allTrivia;

    public TriviaViewModel(Application application) {
        super(application);
        repository = new TriviaRepository(application);
        allTrivia = repository.getAllTrivia();
    }

    LiveData<List<Trivia>> getAllTrivia() {
        return allTrivia;
    }

    public void insert(Trivia trivia) {
        repository.insert(trivia);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
