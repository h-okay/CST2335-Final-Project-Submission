/**
 * HighScoresFragment.java
 * <p>
 * The HighScoresFragment class is a Fragment that displays a list of high scores
 * from the local database in a ListView. This class also provides an option
 * to delete high scores from the ListView and the database.
 *
 * @author Ahmed Almutawakel
 * @version 1.0
 * @since JDK 20
 */
package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.cst2335_finalproject.R;


/**
 * The HighScoresFragment class is a Fragment that displays a list of high scores
 * from the local database in a ListView. This class also provides an option
 * to delete high scores from the ListView and the database.
 */
public class HighScoresFragment extends Fragment {

    private HighScoresViewModel highScoresViewModel;
    private ListView highScoresListView;
    private ArrayAdapter<UserScore> adapter;

    /**
     * Called to have the fragment create its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_high_scores, container, false);
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state
     * has been restored in to the view. This gives subclasses a chance to initialize
     * themselves once they know their view hierarchy has been completely created.
     *
     * @param view               The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        highScoresListView = view.findViewById(R.id.highScoresListView);

        highScoresViewModel = new ViewModelProvider(this).get(HighScoresViewModel.class);
        highScoresViewModel.getHighScores().observe(getViewLifecycleOwner(), userScores -> {
            // Update the ListView with the high scores
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, userScores);
            highScoresListView.setAdapter(adapter);
        });

        highScoresListView.setOnItemLongClickListener((parent, view1, position, id) -> {
            UserScore userScore = adapter.getItem(position);
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete high score")
                    .setMessage("Are you sure you want to delete " + userScore.getUserName() + "'s high score?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        highScoresViewModel.deleteUserScore(userScore);

                        // Show a Snack bar with an Undo action
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

    /**
     * This method is used to refresh the high scores in the ListView.
     * It observes the highScores LiveData from the ViewModel and updates the adapter
     * with the latest high scores when a change is detected.
     */
    private void refreshHighScores() {
        highScoresViewModel.getHighScores().observe(getViewLifecycleOwner(), userScores -> {
            adapter.clear();
            adapter.addAll(userScores);
            adapter.notifyDataSetChanged();
        });
    }
}
