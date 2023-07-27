package algonquin.cst2335.cst2335_finalproject.flight;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import algonquin.cst2335.cst2335_finalproject.R;
import algonquin.cst2335.cst2335_finalproject.databinding.FlightMainBinding;


public class FlightMainActivity extends AppCompatActivity implements FlightAdapter.OnItemClickListener {

    FlightMainBinding binding;
    FLightListViewModel flightModel;
    private ArrayList<Flight> flightList;
    private RecyclerView recyclerView;
    private FlightAdapter flightAdapter;
    private Button enterButton;
    private Button viewListButton;

    private EditText typeAirportCode;

    FlightDatabase db;
    FlightDAO myDAO;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.flight_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FlightMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);  // for action toolbar

        //   setContentView(R.layout.flight_main);

        db = Room.databaseBuilder(getApplicationContext(), FlightDatabase.class, "flight-item").build();
        myDAO = db.cmDAO(); // the only function in FlightDatabase

        flightList = new ArrayList<>();
        flightModel = new ViewModelProvider(this).get(FLightListViewModel.class);
        flightList = flightModel.lists.getValue();
        if (flightList == null) {
            flightModel.lists.postValue(flightList = new ArrayList<>());
            Thread backgroundThread = new Thread(() -> {
                List<Flight> allFlight = myDAO.getFlights();
                flightList.addAll(allFlight);
                runOnUiThread(() -> binding.recyclerView.setAdapter(flightAdapter));
            });
            backgroundThread.start();
        }


        typeAirportCode = findViewById(R.id.typeAirportCode);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve and set the saved EditText value
        String savedAirportCode = getSavedEditTextValue("airportCode", "");
        typeAirportCode.setText(savedAirportCode);

        enterButton = findViewById(R.id.enterButton);
        viewListButton = findViewById(R.id.savedFlightButton);

        // for fragment set up
        flightList = new ArrayList<>();
        flightModel = new ViewModelProvider(this).get(FLightListViewModel.class);
        flightList = flightModel.lists.getValue();

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String airportCode = typeAirportCode.getText().toString();
                // Save the EditText value
                saveEditTextValue("airportCode", airportCode);

                getFlightResults(airportCode);
                Toast.makeText(getApplicationContext(), "Airport code entered!", Toast.LENGTH_SHORT).show();
            }
        });

        flightModel.selectedList.observe(this, (flight) -> {

            if (flight != null) {
                FlightDetailFragment flightFragment = new FlightDetailFragment(flight, myDAO);  //newValue is the newly set ChatMessage
                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();
                tx.replace(R.id.fragmentLocation, flightFragment);
                tx.addToBackStack(null);
                tx.commit();
            }
        });


        viewListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Using Thread
                new Thread(() -> {
                    // Retrieve the saved flights from the database using the FlightDAO
                    List<Flight> savedFlights = myDAO.getFlights();
                    // Update the RecyclerView with the saved flights
                    v.post(() -> updateFlightResults(savedFlights));
                }).start();
            }
        });

        initializeRecyclerView();
        // Set the click listener to the adapter
        flightAdapter.setOnItemClickListener(this);

// Check if there is a saved airport code and fetch flight results
//        if (!savedAirportCode.isEmpty()) {
//            getFlightResults(savedAirportCode);
//        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Instructions").setMessage(R.string.flight_help)
                        .setPositiveButton("OK", (dialog, which) -> {
                }).create().show();

                break;
        }

        return true;
    }

    @Override
    public void onItemClick(Flight flight) {
        int position = flightList.indexOf(flight);
        new Thread(() -> {
            // Check if the clicked flight is in the saved list (from the database)
            List<Flight> savedFlights = myDAO.getFlights();
            if (savedFlights.contains(flight)) {
                // Show the AlertDialog for the clicked item in the saved list
                runOnUiThread(() -> showAlertDialog(flight, position));
            } else {
                // Show the FlightDetailFragment for the clicked item not in the saved list
                runOnUiThread(() -> showFlightDetailFragment(flight));
            }
        }).start();
    }

    private void showAlertDialog(Flight flight, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.flight_detail))
                .setMessage(getString(R.string.flight_number, flight.getFlightNumber())
                        + "\n" + getString(R.string.departure, flight.getDepartureAirport())
                        + "\n" + getString(R.string.delay, flight.getDelay())
                        + "\n" + getString(R.string.gate, flight.getGate())
                        + "\n" + getString(R.string.terminal, flight.getTerminal())
                        + "\n" + getString(R.string.destination, flight.getDestination()))
                .setNegativeButton("Cancel", (dialog, which) -> {
                }).setPositiveButton("Delete", (dialog, which) -> {
            new Thread(() -> {
                // Delete the selected flight from the database
                myDAO.deleteFlight(flight);
                runOnUiThread(() -> {
                    // Remove the flight from the list and notify the adapter
                    flightList.remove(position);
                    flightAdapter.notifyItemRemoved(position);

                    Snackbar.make(recyclerView, "You deleted flight #" + (position + 1), Snackbar.LENGTH_LONG).setAction("Undo", click -> {
                        new Thread(() -> {
                            // Insert the deleted flight back to the database
                            myDAO.insertFlight(flight);
                            runOnUiThread(() -> {
                                // Add the flight back to the list and notify the adapter
                                flightList.add(position, flight);
                                flightAdapter.notifyItemInserted(position);
                            });
                        }).start();
                    }).show();
                });
            }).start();
        }).create().show();
    }


    private void showFlightDetailFragment(Flight flight) {
        FlightDetailFragment flightFragment = new FlightDetailFragment(flight, myDAO);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLocation, flightFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initializeRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        flightList = new ArrayList<>();
        flightAdapter = new FlightAdapter(flightList, this); // pass 'this' as the listner
        recyclerView.setAdapter(flightAdapter);
    }

    private void updateFlightResults(List<Flight> newFlightList) {
        flightList.clear();
        flightList.addAll(newFlightList);
        flightAdapter.notifyDataSetChanged();
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


    // Method to retrieve flight results based on search query

    private void getFlightResults(String airportCode) {
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        String url = "http://api.aviationstack.com/v1/flights?access_key=64c8ab1b70d567836fa9b2c80be480e2&dep_iata=" + airportCode + "&limit=100";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<Flight> newFlightList = new ArrayList<>();

                try {
                    JSONArray array = response.getJSONArray("data");
                    String terminal;
                    String gate;
                    String delay;

                    for (int i = 0; i < array.length() && i < 100; i++) {
                        JSONObject flightObject = array.getJSONObject(i);


                        String departureAirport = flightObject.getJSONObject("departure").getString("airport");
                        String destination = flightObject.getJSONObject("arrival").getString("timezone");
                        String flightNumber = flightObject.getJSONObject("flight").getString("icao");

                        delay = flightObject.getJSONObject("departure").isNull("delay") ? "No delay" : flightObject.getJSONObject("departure").getString("delay");

                        if (!flightObject.getJSONObject("departure").getString("terminal").equals("null")) {
                            terminal = flightObject.getJSONObject("departure").getString("terminal");
                        } else {
                            terminal = "Not available now";
                        }

                        if (!flightObject.getJSONObject("departure").getString("gate").equals("null")) {
                            gate = flightObject.getJSONObject("departure").getString("gate");
                        } else {
                            gate = "Not available now";
                        }

                        Flight flight = new Flight(departureAirport, flightNumber, delay, gate, terminal, destination);
                        newFlightList.add(flight);
                    }
                    // Call updateFlightResults() to update the flight data in the adapter
                    updateFlightResults(newFlightList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error response here
                error.printStackTrace();
                Log.e("url", "on Error Response:" + error.getLocalizedMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "Mozilla/5.0");
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(0,  // Timeout duration in milliseconds
                -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        rq.add(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }

}


