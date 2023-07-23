package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.cst2335_finalproject.R;

import java.util.List;

public class HighScoresFragment extends Fragment {

    private HighScoresViewModel highScoresViewModel;
    private ListView highScoresListView;
    private ArrayAdapter<UserScore> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_high_scores, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        highScoresListView = view.findViewById(R.id.highScoresListView);

        highScoresViewModel = new ViewModelProvider(this).get(HighScoresViewModel.class);
        highScoresViewModel.getHighScores().observe(getViewLifecycleOwner(), new Observer<List<UserScore>>() {
            @Override
            public void onChanged(List<UserScore> userScores) {
                // Update the ListView with the high scores
                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, userScores);
                highScoresListView.setAdapter(adapter);
            }
        });

        highScoresListView.setOnItemLongClickListener((parent, view1, position, id) -> {
            UserScore userScore = adapter.getItem(position);
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete high score")
                    .setMessage("Are you sure you want to delete " + userScore.getUserName() + "'s high score?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        highScoresViewModel.deleteUserScore(userScore);

                        // Show a Snackbar with an Undo action
                        Snackbar.make(view, "Deleted " + userScore.getUserName() + "'s high score", Snackbar.LENGTH_LONG)
                                .setAction("Undo", v -> {
                                    highScoresViewModel.insertUserScore(userScore);
                                    refreshHighScores();
                                })
                                .show();
                    })

                    .setNegativeButton(android.R.string.no, null)
                    .show();

            return true;
        });
    }

    private void refreshHighScores() {
        highScoresViewModel.getHighScores().observe(getViewLifecycleOwner(), new Observer<List<UserScore>>() {
            @Override
            public void onChanged(List<UserScore> userScores) {
                adapter.clear();
                adapter.addAll(userScores);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
