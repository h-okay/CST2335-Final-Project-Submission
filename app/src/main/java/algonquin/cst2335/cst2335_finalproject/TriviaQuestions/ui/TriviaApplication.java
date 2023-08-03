/**
 * TriviaApplication.java
 * <p>
 * The TriviaApplication class extends AppCompatActivity and manages the game logic and UI interactions for the trivia application.
 * It handles fetching the quiz questions from a remote API, displaying the questions and possible answers, keeping track of the player's score,
 * and saving the score when the quiz ends.
 * <p>
 * The class communicates with the Room database through a ViewModel, HighScoresViewModel.
 *
 * @author Ahmed Almutawakel
 * @version 1.0
 * @since JDK 20
 */
package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import algonquin.cst2335.cst2335_finalproject.R;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.AppDatabase;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.HighScoresFragment;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.HighScoresViewModel;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.TriviaMain;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.TriviaQuestion;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.UserScore;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.UserScoreDAO;

/**
 * The TriviaApplication class extends AppCompatActivity and manages the game logic and UI interactions for the trivia application.
 */
public class TriviaApplication extends AppCompatActivity {

    /**
     * The SQLite database
     */
    private AppDatabase db;

    /**
     * Data Access Object for UserScore table
     */
    private UserScoreDAO userScoreDao;

    /**
     * ViewModel for high scores
     */
    private HighScoresViewModel highScoresViewModel;

    /**
     * List of trivia questions fetched from the API
     */
    private final List<TriviaQuestion> quizQuestions = new ArrayList<>();

    /**
     * Index of the current question being displayed
     */
    private int currentQuestionIndex = 0;

    /**
     * Current score of the player
     */
    private int score = 0;

    /**
     * Button to proceed to the next question
     */
    private Button nextButton;

    /**
     * TextView to display the question
     */
    private TextView questionTextView;

    /**
     * TextViews to display the possible answers
     */
    private TextView answer1, answer2, answer3, answer4;

    /**
     * TextView for the currently selected answer
     */
    private TextView selectedAnswer;

    /**
     * Toolbar for the application
     */
    protected Toolbar theToolbar;

    /**
     * Username of the player
     */
    private String userName;

    /**
     * Number of Questions the player wants to have
     */
    private int numberOfQuestions;

    private int categoryId;

    /**
     * Called when the activity is starting. This is where most initialization happens.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_main);

        theToolbar = findViewById(R.id.myToolbar);
        theToolbar.setTitle("Trivia");
        setSupportActionBar(theToolbar);
        numberOfQuestions = getIntent().getIntExtra("NUMBER_OF_QUESTIONS", 10); // default to 10 if not provided
        categoryId = getIntent().getIntExtra("CATEGORY_ID", 0);

        highScoresViewModel = new ViewModelProvider(this).get(HighScoresViewModel.class);

        userName = getIntent().getStringExtra("USERNAME");

        nextButton = findViewById(R.id.nextButton);
        questionTextView = findViewById(R.id.questionTextView);

        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);

        View.OnClickListener answerClickListener = v -> {
            // If an answer was previously selected, reset its color
            if (selectedAnswer != null) {
                selectedAnswer.setTextColor(Color.BLACK);
            }

            // Mark the new selection and store it
            ((TextView) v).setTextColor(Color.BLUE);
            selectedAnswer = (TextView) v;
        };

        answer1.setOnClickListener(answerClickListener);
        answer2.setOnClickListener(answerClickListener);
        answer3.setOnClickListener(answerClickListener);
        answer4.setOnClickListener(answerClickListener);

        nextButton.setEnabled(false); // disable at start

        nextButton.setOnClickListener(v -> {
            if (selectedAnswer != null) {
                TriviaQuestion currentQuestion = quizQuestions.get(currentQuestionIndex);
                String correctAnswer = currentQuestion.getCorrectAnswer();
                // Reset color for all TextViews
                answer1.setTextColor(Color.BLACK);
                answer2.setTextColor(Color.BLACK);
                answer3.setTextColor(Color.BLACK);
                answer4.setTextColor(Color.BLACK);

                // If the selected answer is correct
                if (selectedAnswer.getText().toString().equals(correctAnswer)) {
                    score++;
                    selectedAnswer.setTextColor(Color.GREEN); // Change color of selected answer to green
                } else {
                    // If the selected answer is incorrect
                    selectedAnswer.setTextColor(Color.RED); // Change color of selected answer to red

                    // Change color of correct answer to green
                    if (answer1.getText().toString().equals(correctAnswer)) {
                        answer1.setTextColor(Color.GREEN);
                    } else if (answer2.getText().toString().equals(correctAnswer)) {
                        answer2.setTextColor(Color.GREEN);
                    } else if (answer3.getText().toString().equals(correctAnswer)) {
                        answer3.setTextColor(Color.GREEN);
                    } else if (answer4.getText().toString().equals(correctAnswer)) {
                        answer4.setTextColor(Color.GREEN);
                    }
                }

                // Delay moving to the next question
                new Handler().postDelayed(() -> {
                    currentQuestionIndex++;
                    if (currentQuestionIndex < quizQuestions.size()) {
                        displayQuestion(quizQuestions.get(currentQuestionIndex));
                    } else {
                        onQuizEnd();
                    }
                }, 2000); // delay of 2 seconds

            } else {
                Toast.makeText(TriviaApplication.this, R.string.trivia_select_answer, Toast.LENGTH_SHORT).show();
            }
        });

        fetchQuizQuestions();
    }


    /**
     * Fetches quiz questions from a remote API and parses the response into a list of TriviaQuestion objects.
     */
    private void fetchQuizQuestions() {
        String url = "https://opentdb.com/api.php?amount=" + numberOfQuestions + "&category=" + categoryId + "&type=multiple";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        JSONArray resultsArray = response.getJSONArray("results");
                        for (int i = 0; i < resultsArray.length(); i++) {
                            JSONObject questionObject = resultsArray.getJSONObject(i);
                            String question = Html.fromHtml(questionObject.getString("question")).toString();
                            String correctAnswer = Html.fromHtml(questionObject.getString("correct_answer")).toString();
                            JSONArray incorrectAnswersArray = questionObject.getJSONArray("incorrect_answers");
                            List<String> answers = new ArrayList<>();
                            List<TextView> answerViews = new ArrayList<>();
                            answerViews.add(answer1);
                            answerViews.add(answer2);
                            answerViews.add(answer3);
                            answerViews.add(answer4);
                            Collections.shuffle(answerViews);

                            for (int j = 0; j < incorrectAnswersArray.length(); j++) {
                                TextView answerView = answerViews.get(j);
                                String answerText = Html.fromHtml(incorrectAnswersArray.getString(j)).toString();
                                answerView.setText(answerText);
                                answers.add(answerText);
                            }

                            TextView correctAnswerView = answerViews.get(incorrectAnswersArray.length());
                            correctAnswerView.setText(correctAnswer);
                            answers.add(correctAnswer);

                            quizQuestions.add(new TriviaQuestion(question, answers, correctAnswer));
                        }
                        displayQuestion(quizQuestions.get(0));
                        nextButton.setEnabled(true); // enable once questions are loaded
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(TriviaApplication.this, "Error fetching quiz questions", Toast.LENGTH_LONG).show());

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    /**
     * Displays the given trivia question and its possible answers.
     *
     * @param quizQuestion The TriviaQuestion to display.
     */
    private void displayQuestion(TriviaQuestion quizQuestion) {
        questionTextView.setText(quizQuestion.getQuestion());

        answer1.setText(quizQuestion.getAnswers().get(0));
        answer2.setText(quizQuestion.getAnswers().get(1));
        answer3.setText(quizQuestion.getAnswers().get(2));
        answer4.setText(quizQuestion.getAnswers().get(3));

        answer1.setTextColor(Color.BLACK);
        answer2.setTextColor(Color.BLACK);
        answer3.setTextColor(Color.BLACK);
        answer4.setTextColor(Color.BLACK);

        selectedAnswer = null;
    }


    /**
     * Saves the player's score when the quiz ends.
     */
    private void onQuizEnd() {
        UserScore newUserScore = new UserScore(userName, score);
        highScoresViewModel.insertUserScore(newUserScore);
        Toast.makeText(TriviaApplication.this, R.string.trivia_score_saved, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.id_help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TriviaApplication.this);
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
