package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import algonquin.cst2335.cst2335_finalproject.R;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.ui.TriviaApplication;
import algonquin.cst2335.cst2335_finalproject.bearimages.ui.BearApplication;

public class TriviaMain extends AppCompatActivity {
    private EditText usernameEditText;

    protected Toolbar theToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trivia_user_details);  // Changed this line

        theToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(theToolbar); //adds your toolbar, onCreateOptionsMenu


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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.id_help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TriviaMain.this);
            builder.setTitle("How to use").setMessage("- Press Start Trivia to begin the Trivia.\n\n- Tap on Leaderboards to view the top 10 scores. \n\n- Read the questionnaires and try to guess the answer. \n\n- Have fun!").setPositiveButton("OK", (dialog, which) -> {
            }).create().show();
        }
        else if (item.getItemId() == R.id.id_delete)
            Toast.makeText(this, "You clicked on delete", Toast.LENGTH_LONG).show();



        return true;
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.trivia_menu, menu);

        return true;
    }

}
