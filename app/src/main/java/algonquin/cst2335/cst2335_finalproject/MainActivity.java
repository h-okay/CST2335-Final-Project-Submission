/**
 * MainActivity.java
 * <p>
 * The MainActivity class represents the main entry point of the CST2335 Final Project application.
 * It is the main activity that displays the app's main interface and handles navigation to other features.
 * <p>
 * |-------------------|--------------------------------|
 * | Student Name      |          Project Name          |
 * |-------------------|--------------------------------|
 * | Iseul Park        | Aviation Stack Flight Tracker  |
 * | Ahmed Almutawakel | Trivia Question Database       |
 * | Hakan Okay        | Bear Image Generator           |
 * | Dowon Kang        | Currency Converter             |
 * |-------------------|--------------------------------|
 *
 * @author Iseul Park
 * @author Ahmed Almutawakel
 * @author Hakan Okay
 * @author Dowon Kang
 * @version 1.0
 * @since JDK 20
 */

package algonquin.cst2335.cst2335_finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import algonquin.cst2335.cst2335_finalproject.CurrencyConverter.ConversionMain;
import algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data.TriviaMain;
import algonquin.cst2335.cst2335_finalproject.bearimages.ui.BearApplication;
import algonquin.cst2335.cst2335_finalproject.databinding.ActivityMainBinding;
import algonquin.cst2335.cst2335_finalproject.flight.FlightMainActivity;

/**
 * The MainActivity class represents the main entry point of the CST2335 Final Project application.
 * It is the main activity that displays the app's main interface and handles navigation to other features.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The binding object for the activity's layout.
     */
    private ActivityMainBinding binding;

    /**
     * The SharedPreferences object to manage app preferences.
     */
    private SharedPreferences prefs;

    /**
     * The ActivityResultLauncher to handle the result of launching the BearApplication activity.
     */
    private ActivityResultLauncher<Intent> bearApplicationLauncher;
    private ActivityResultLauncher<Intent> currencyApplicationLauncher;
    private ActivityResultLauncher<Intent> flightApplicationLauncher;
    private ActivityResultLauncher<Intent> triviaApplicationLauncher;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("CST 2335");
        setSupportActionBar(binding.toolbar);

        prefs = getSharedPreferences("BearAppData", Context.MODE_PRIVATE);

        bearApplicationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                int height = result.getData().getIntExtra("bearHeight", 0);
                int width = result.getData().getIntExtra("bearWidth", 0);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("bearHeight", height);
                editor.putInt("bearWidth", width);
                editor.apply();
            }
        });

        currencyApplicationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        });

        flightApplicationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        });

        triviaApplicationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        });

        ImageButton aviationButton = binding.aviationButton;
        ImageButton bearButton = binding.bearButton;
        ImageButton triviaButton = binding.triviaButton;
        ImageButton currencyButton = binding.currencyButton;

        aviationButton.setOnClickListener(click -> {
            Intent intent = new Intent(MainActivity.this, FlightMainActivity.class);
            flightApplicationLauncher.launch(intent);
        });

        bearButton.setOnClickListener(click -> {
            Intent intent = new Intent(MainActivity.this, BearApplication.class);
            bearApplicationLauncher.launch(intent);
        });

        triviaButton.setOnClickListener(click -> {
            Intent intent = new Intent(MainActivity.this, TriviaMain.class);
            triviaApplicationLauncher.launch(intent);
        });

        currencyButton.setOnClickListener(click -> {
            Intent intent = new Intent(MainActivity.this, ConversionMain.class);
            currencyApplicationLauncher.launch(intent);
        });

    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     *
     * @param item The menu item that was selected.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.aviation) {
            Intent intent = new Intent(MainActivity.this, FlightMainActivity.class);
            flightApplicationLauncher.launch(intent);
            return true;
        }
        if (itemId == R.id.trivia) {
            Intent intent = new Intent(MainActivity.this, TriviaMain.class);
            triviaApplicationLauncher.launch(intent);
            return true;
        }
        if (itemId == R.id.bear) {
            Intent intent = new Intent(MainActivity.this, BearApplication.class);
            bearApplicationLauncher.launch(intent);
            return true;
        }
        if (itemId == R.id.currency) {
            Intent intent = new Intent(MainActivity.this, ConversionMain.class);
            currencyApplicationLauncher.launch(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}






