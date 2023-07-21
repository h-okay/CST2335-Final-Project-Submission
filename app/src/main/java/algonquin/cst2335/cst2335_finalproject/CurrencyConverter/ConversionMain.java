package algonquin.cst2335.cst2335_finalproject.CurrencyConverter;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import algonquin.cst2335.cst2335_finalproject.CurrencyConverter.data.ConversionViewModel;
import algonquin.cst2335.cst2335_finalproject.R;

public class ConversionMain extends AppCompatActivity {

    private EditText editTextAmount;
    private Button buttonConvert;
    private Spinner spinnerSourceCurrency;
    private Spinner spinnerTargetCurrency;

    private ArrayAdapter<String> currencyAdapter;
    private String[] currencyCodes;
    private String[] currencyNames;

    private RecyclerView recyclerViewConversions;
    private List<ConversionDAO> conversions = new ArrayList<>();
    private ConversionAdapter adapter;
    private ConversionViewModel conversionViewModel;
    protected ArrayList<ConversionDAO> dao;

    private Button helpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversion_main);

        conversionViewModel = new ViewModelProvider(this).get(ConversionViewModel.class);

        editTextAmount = findViewById(R.id.edit_text_amount);
        buttonConvert = findViewById(R.id.button_convert);
        spinnerSourceCurrency = findViewById(R.id.spinner_source_currency);
        spinnerTargetCurrency = findViewById(R.id.spinner_target_currency);
        helpBtn = findViewById(R.id.button2);

        recyclerViewConversions = findViewById(R.id.recycler_view_conversions);
        recyclerViewConversions.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConversionAdapter(conversions);
        recyclerViewConversions.setAdapter(adapter);

        recyclerViewConversions.setAdapter(adapter);

        dao = conversionViewModel.dao;

        adapter.setOnLongClickListener(new ConversionAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                deleteItem(position);
            }
        });

// With this line
        adapter.setOnItemClickListener(new ConversionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ConversionDAO selectedConversion = conversions.get(position);
                showConversionDetailFragment(selectedConversion);
            }
        });

        // Help button
        helpBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ConversionMain.this);
            builder.setTitle("Help");
            builder.setMessage(R.string.helpMessage);

            builder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        // Countries select
        setupSpinners();

        // Load data from SharedPreferences
        loadConversionsFromSharedPreferences();

        // Convert Click -> show currency converter
        buttonConvert.setOnClickListener(v -> {
            String amount = editTextAmount.getText().toString();
            String sourceCurrency = currencyCodes[spinnerSourceCurrency.getSelectedItemPosition()];
            String targetCurrency = currencyCodes[spinnerTargetCurrency.getSelectedItemPosition()];

            makeApiRequest(amount, sourceCurrency, targetCurrency);
        });
    }

    private void setupSpinners() {
        // code - short name of country, names -> country + currency
        currencyCodes = getResources().getStringArray(R.array.currency_codes);
        currencyNames = getResources().getStringArray(R.array.currency_names);

        // Create a list of country names with their corresponding currency codes
        List<String> countryNamesWithCodes = new ArrayList<>();
        for (int i = 0; i < currencyCodes.length; i++) {
            countryNamesWithCodes.add(currencyNames[i] + " (" + currencyCodes[i] + ")");
        }

        currencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countryNamesWithCodes);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSourceCurrency.setAdapter(currencyAdapter);
        spinnerTargetCurrency.setAdapter(currencyAdapter);
    }

    private void makeApiRequest(String amount, String sourceCurrency, String targetCurrency) {
        String apiUrl = "https://currency-converter5.p.rapidapi.com/currency/convert?format=json&from="
                + sourceCurrency + "&to=" + targetCurrency + "&amount=" + amount;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject ratesObject = response.getJSONObject("rates");
                            JSONObject targetCurrencyObject = ratesObject.getJSONObject(targetCurrency);
                            String convertedAmount = targetCurrencyObject.getString("rate_for_amount");

                            // Initialization ConversionDAO variables
                            ConversionDAO conversionDAO = new ConversionDAO(sourceCurrency, targetCurrency, amount, convertedAmount);
                            conversions.add(conversionDAO);
                            adapter.notifyDataSetChanged();

                            // Save data to SharedPreferences
                            saveToSharedPreferences(sourceCurrency, targetCurrency, amount, convertedAmount);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ConversionMain.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ConversionMain.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-RapidAPI-Key", "1500d68574msh2aa454c8cdc6dc9p184191jsnd2096834e1fa");
                headers.put("X-RapidAPI-Host", "currency-converter5.p.rapidapi.com");
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void saveToSharedPreferences(String sourceCurrency, String targetCurrency, String amount, String convertedAmount) {
        SharedPreferences sharedPreferences = getSharedPreferences("CurrencyConversion", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String conversionKey = sourceCurrency + "_" + targetCurrency;
        String conversionValue = amount + "_" + convertedAmount;
        editor.putString(conversionKey, conversionValue);
        editor.apply();
    }

    private void loadConversionsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CurrencyConversion", MODE_PRIVATE);
        conversions.clear();

        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();

            String[] keyValue = key.split("_");
            String[] valueArray = value.split("_");

            String sourceCurrency = keyValue[0];
            String targetCurrency = keyValue[1];
            String amount = valueArray[0];
            String convertedAmount = valueArray[1];

            ConversionDAO conversionDAO = new ConversionDAO(sourceCurrency, targetCurrency, amount, convertedAmount);
            conversions.add(conversionDAO);
        }

        adapter.notifyDataSetChanged();
    }

    // Method to delete an item from the RecyclerView
    private void deleteItem(int position) {

        if (position >= 0 && position < conversions.size()) {
            ConversionDAO deletedConversion = conversions.get(position);
            conversions.remove(position);
            adapter.notifyItemRemoved(position);

            // Show a Snackbar to notify the user of the deletion
            Snackbar snackbar = Snackbar.make(recyclerViewConversions, "Conversion deleted", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Undo the deletion if the "UNDO" button is clicked
                    conversions.add(position, deletedConversion);
                    adapter.notifyItemInserted(position);
                }
            });
            snackbar.show();
        }
    }
    private void showConversionDetailFragment(ConversionDAO conversionDAO) {
        ConversionDetailFragment fragment = ConversionDetailFragment.newInstance(conversionDAO);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLocation2, fragment)
                .addToBackStack(null)
                .commit();
    }
}




