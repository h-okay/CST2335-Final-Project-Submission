package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import algonquin.cst2335.cst2335_finalproject.R;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.UserScore;

public class HighScoresFragment extends Fragment {

    private HighScoresViewModel highScoresViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_high_scores, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView highScoresTextView = view.findViewById(R.id.highScoresTextView);


        highScoresViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())
                .create(HighScoresViewModel.class);
        highScoresViewModel.getHighScores().observe(getViewLifecycleOwner(), new Observer<List<UserScore>>() {
            @Override
            public void onChanged(List<UserScore> userScores) {
                // Update the TextView with the high scores
                StringBuilder highScoresText = new StringBuilder();
                for (UserScore userScore : userScores) {
                    highScoresText.append(userScore.getUserName()).append(": ").append(userScore.getScore()).append("\n");
                }
                highScoresTextView.setText(highScoresText.toString());
            }
        });
    }
}
