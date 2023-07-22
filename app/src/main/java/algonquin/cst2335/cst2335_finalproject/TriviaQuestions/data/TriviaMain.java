package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.cst2335_finalproject.R;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.ui.TriviaApplication;

public class TriviaMain extends AppCompatActivity {

    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trivia_user_details);  // Changed this line

        usernameEditText = findViewById(R.id.usernameEditText);
        Button startTriviaButton = findViewById(R.id.startTriviaButton);

        startTriviaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();

                Intent intent = new Intent(TriviaMain.this, TriviaApplication.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });
        Button highScoresButton = findViewById(R.id.highScoresButton);

        highScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new HighScoresFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

    }
}
