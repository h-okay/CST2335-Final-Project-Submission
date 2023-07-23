package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.Response;
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
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.TriviaQuestion;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.UserScore;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.UserScoreDAO;

public class TriviaApplication extends AppCompatActivity {

    private AppDatabase db;
    private UserScoreDAO userScoreDao;
    private HighScoresViewModel highScoresViewModel;

    private List<TriviaQuestion> quizQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private Button nextButton;
    private Button highScoresButton;
    private TextView questionTextView;
    private TextView answer1, answer2, answer3, answer4;
    private TextView selectedAnswer;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_main);

        highScoresViewModel = new ViewModelProvider(this).get(HighScoresViewModel.class);

        userName = getIntent().getStringExtra("USERNAME");

        nextButton = findViewById(R.id.nextButton);
        highScoresButton = findViewById(R.id.highScoresButton);
        questionTextView = findViewById(R.id.questionTextView);

        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);

        View.OnClickListener answerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If an answer was previously selected, reset its color
                if (selectedAnswer != null) {
                    selectedAnswer.setTextColor(Color.BLACK);
                }

                // Mark the new selection and store it
                ((TextView) v).setTextColor(Color.BLUE);
                selectedAnswer = (TextView) v;
            }
        };

        answer1.setOnClickListener(answerClickListener);
        answer2.setOnClickListener(answerClickListener);
        answer3.setOnClickListener(answerClickListener);
        answer4.setOnClickListener(answerClickListener);

        nextButton.setEnabled(false); // disable at start

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            currentQuestionIndex++;
                            if (currentQuestionIndex < quizQuestions.size()) {
                                displayQuestion(quizQuestions.get(currentQuestionIndex));
                            } else {
                                onQuizEnd();
                            }
                        }
                    }, 2000); // delay of 2 seconds

                } else {
                    Toast.makeText(TriviaApplication.this, "Please select an answer!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        highScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new HighScoresFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        fetchQuizQuestions();
    }

    private void fetchQuizQuestions() {
        String url = "https://opentdb.com/api.php?amount=10&category=22&type=multiple";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
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
                    }

                }, error -> Toast.makeText(TriviaApplication.this, "Error fetching quiz questions", Toast.LENGTH_LONG).show());

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

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


    private void onQuizEnd() {
        if (userName.isEmpty()) {
            Toast.makeText(TriviaApplication.this, "Please enter your username!", Toast.LENGTH_SHORT).show();
            return;
        }
        UserScore newUserScore = new UserScore(userName, score);
        highScoresViewModel.insertUserScore(newUserScore);
        Toast.makeText(TriviaApplication.this, "Score saved!", Toast.LENGTH_LONG).show();
    }

}
