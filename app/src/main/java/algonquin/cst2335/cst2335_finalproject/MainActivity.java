package algonquin.cst2335.cst2335_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button enterButton;
    private EditText typeAirportCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        typeAirportCode = findViewById(R.id.typeAirportCode);
        // Retrieve and set the saved EditText value
        typeAirportCode.setText(getSavedEditTextValue("airportCode", ""));

        enterButton = findViewById(R.id.enterButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String airportCode = typeAirportCode.getText().toString();
                // Save the EditText value
                saveEditTextValue("airportCode", airportCode);
               // Toast.makeText(getApplicationContext(), "Airport code saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveEditTextValue(String airportCode, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Save the EditText value with the provided key
        editor.putString(airportCode, value);
        editor.apply();
    }

    private String getSavedEditTextValue(String airportCode, String defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        // Retrieve the saved EditText value using the key
        return sharedPreferences.getString(airportCode, defaultValue);
    }
}
