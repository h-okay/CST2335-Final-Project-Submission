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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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

    private SharedPreferences sharedPreferences;

    private EditText numberOfQuestionsEditText;

    private static final String USERNAME_KEY= "USERNAME";

    private static final String NUMBER_OF_QUESTIONS_KEY = "NUMBER_OF_QUESTIONS";

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
        theToolbar.setTitle("Trivia");
        setSupportActionBar(theToolbar);

        usernameEditText = findViewById(R.id.usernameEditText);
        numberOfQuestionsEditText = findViewById(R.id.numberOfQuestionsEditText);
        sharedPreferences = getSharedPreferences("default", MODE_PRIVATE);

        // Retrieve the saved username
        String savedUsername = sharedPreferences.getString(USERNAME_KEY, "");
        usernameEditText.setText(savedUsername);

        // Retrieve the saved number of questions
        String savedNumberOfQuestions = sharedPreferences.getString(NUMBER_OF_QUESTIONS_KEY, "");
        numberOfQuestionsEditText.setText(savedNumberOfQuestions);

        Button startTriviaButton = findViewById(R.id.startTriviaButton);
        startTriviaButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(TriviaMain.this, R.string.trivia_select_answer, Toast.LENGTH_SHORT).show();
                return;
            }

            // Save the username when the button is clicked
            sharedPreferences.edit().putString(USERNAME_KEY, username).apply();

            // Save the number of questions when the button is clicked
            sharedPreferences.edit().putString(NUMBER_OF_QUESTIONS_KEY, numberOfQuestionsEditText.getText().toString()).apply();

            int numberOfQuestions = Integer.parseInt(numberOfQuestionsEditText.getText().toString());

            if (numberOfQuestions < 1 || numberOfQuestions > 50) {
                Toast.makeText(this, "Please enter a number between 1 and 50", Toast.LENGTH_SHORT).show();
                return;
            }

            // Initialize the RadioGroup
            RadioGroup categoryRadioGroup = findViewById(R.id.categoryRadioGroup);

            // Set a default category
            int categoryId = 22; // Let's assume 22 corresponds to Geography

            // Check which radio button is selected
            int selectedId = categoryRadioGroup.getCheckedRadioButtonId();

            // Set the category id based on the selected radio button
            if (selectedId == R.id.radioButtonHistory) {
                categoryId = 23;
            } else if (selectedId == R.id.radioButtonSport) {
                categoryId = 21;
            } else if (selectedId == R.id.radioButtonArt) {
                categoryId = 25;
            }

            Intent intent = new Intent(this, TriviaApplication.class);
            intent.putExtra("USERNAME", username);
            intent.putExtra("CATEGORY_ID", categoryId);  // Add the category ID
            intent.putExtra("NUMBER_OF_QUESTIONS", numberOfQuestions);
            startActivity(intent);

        });


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
            builder.setTitle(R.string.trivia_usage).setMessage(R.string.trivia_info)
                    .setPositiveButton(R.string.trivia_okay, (dialog, which) -> {
                    }).create().show();
        }
     else if (item.getItemId() == R.id.id_highscore) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new HighScoresFragment())
                .addToBackStack(null)
                .commit();
    }

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
