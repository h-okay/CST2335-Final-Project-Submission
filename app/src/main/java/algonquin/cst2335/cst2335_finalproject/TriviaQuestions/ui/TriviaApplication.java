package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algonquin.cst2335.cst2335_finalproject.R;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.AppDatabase;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.HighScoresFragment;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.TriviaQuestion;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.UserScore;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.UserScoreDAO;

public class TriviaApplication extends AppCompatActivity {

    private AppDatabase db;
    private UserScoreDAO userScoreDao;
    private ExecutorService executorService;
    private List<TriviaQuestion> quizQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private Button nextButton;
    private Button highScoresButton;
    private RadioGroup answerRadioGroup;
    private TextView questionTextView;
    private EditText userNameEditText;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_main);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();
        userScoreDao = db.userScoreDao();
        executorService = Executors.newSingleThreadExecutor();

        userName = getIntent().getStringExtra("USERNAME");

        nextButton = findViewById(R.id.nextButton);
        highScoresButton = findViewById(R.id.highScoresButton);
        answerRadioGroup = findViewById(R.id.radioGroup);
        questionTextView = findViewById(R.id.questionTextView);

        nextButton.setEnabled(false); // disable at start

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerRadioGroup.getCheckedRadioButtonId() != -1) {
                    RadioButton selectedRadioButton = findViewById(answerRadioGroup.getCheckedRadioButtonId());
                    String selectedAnswer = selectedRadioButton.getText().toString();
                    TriviaQuestion currentQuestion = quizQuestions.get(currentQuestionIndex);
                    if (selectedAnswer.equals(currentQuestion.getCorrectAnswer())) {
                        score++;
                    }
                    currentQuestionIndex++;
                    if (currentQuestionIndex < quizQuestions.size()) {
                        displayQuestion(quizQuestions.get(currentQuestionIndex));
                    } else {
                        onQuizEnd();
                    }
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
                                String question = questionObject.getString("question");
                                String correctAnswer = questionObject.getString("correct_answer");
                                JSONArray incorrectAnswersArray = questionObject.getJSONArray("incorrect_answers");
                                List<String> answers = new ArrayList<>();
                                for (int j = 0; j < incorrectAnswersArray.length(); j++) {
                                    answers.add(incorrectAnswersArray.getString(j));
                                }
                                answers.add(correctAnswer);
                                Collections.shuffle(answers);
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
        answerRadioGroup.removeAllViews();
        for (String answer : quizQuestion.getAnswers()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(answer);
            answerRadioGroup.addView(radioButton);
        }
    }

    private void onQuizEnd() {
        if(userName.isEmpty()){
            Toast.makeText(TriviaApplication.this, "Please enter your username!", Toast.LENGTH_SHORT).show();
            return;
        }
        UserScore newUserScore = new UserScore(userName, score);
        executorService.execute(() -> {
            userScoreDao.insert(newUserScore);
            runOnUiThread(() -> Toast.makeText(TriviaApplication.this, "Score saved!", Toast.LENGTH_LONG).show());
        });
    }
}
