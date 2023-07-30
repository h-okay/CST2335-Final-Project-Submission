/**
 * TriviaMain.java
 * <p>
 * The TriviaMain class is an Activity that serves as the main entry point of the trivia game.
 * It includes a text field for the user to enter their name and buttons to start the trivia game
 * and view the high scores. This class also implements an options menu with help and delete options.
 *
 * @author Ahmed Almutawakel
 * @version 1.0
 * @since JDK 20
 */
package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import algonquin.cst2335.cst2335_finalproject.R;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.ui.TriviaApplication;

/**
 * The TriviaMain class is an Activity that serves as the main entry point of the trivia game.
 */
public class TriviaMain extends AppCompatActivity {
    private EditText usernameEditText;
    protected Toolbar theToolbar;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trivia_user_details);

        theToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(theToolbar); // adds your toolbar, onCreateOptionsMenu

        usernameEditText = findViewById(R.id.usernameEditText);
        Button startTriviaButton = findViewById(R.id.startTriviaButton);

        startTriviaButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();

            Intent intent = new Intent(TriviaMain.this, TriviaApplication.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        });
        Button highScoresButton = findViewById(R.id.highScoresButton);

        highScoresButton.setOnClickListener(v -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new HighScoresFragment())
                .addToBackStack(null)
                .commit());

    }

    /**
     * This method is called whenever an item in your options menu is selected.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.id_help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TriviaMain.this);
            builder.setTitle("How to use").setMessage("- Press Start Trivia to begin the Trivia." +
                            "\n\n- Tap on Leaderboards to view the top 10 scores." +
                            " \n\n- Read the questionnaires and try to guess the answer. \n\n- Have fun!")
                    .setPositiveButton("OK", (dialog, which) -> {
                    }).create().show();
        }
        else if (item.getItemId() == R.id.id_delete)
            Toast.makeText(this, "You clicked on delete", Toast.LENGTH_LONG).show();

        return true;
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which you place your items.
     * @return boolean You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.trivia_menu, menu);

        return true;
    }
}
