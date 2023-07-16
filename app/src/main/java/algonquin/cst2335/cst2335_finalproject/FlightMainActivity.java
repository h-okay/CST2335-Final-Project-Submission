package algonquin.cst2335.cst2335_finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class FlightMainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private FlightAdapter flightAdapter;
    private Button enterButton;
    private EditText typeAirportCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_main);

        typeAirportCode = findViewById(R.id.typeAirportCode);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Retrieve and set the saved EditText value
        typeAirportCode.setText(getSavedEditTextValue("airportCode", ""));

        enterButton = findViewById(R.id.enterButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFlightSearch();
                String airportCode = typeAirportCode.getText().toString();
                // Save the EditText value
                saveEditTextValue("airportCode", airportCode);
               // Toast.makeText(getApplicationContext(), "Airport code saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    // sharedPreferences
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

    private void performFlightSearch() {
        String airportCode = typeAirportCode.getText().toString();
        // Perform the flight search using the airport code
        List<Flight> flightList = getFlightResults(airportCode);

        flightAdapter = new FlightAdapter(flightList);
        recyclerView.setAdapter(flightAdapter);
    }

    // Method to retrieve flight results based on search query
    private List<Flight> getFlightResults(String airportCode) {
        // Implement your logic here to fetch flight results based on the airport code
        // Return a list of Flight objects
    }
}


