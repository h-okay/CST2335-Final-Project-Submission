package algonquin.cst2335.cst2335_finalproject.CurrencyConverter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.cst2335_finalproject.R;

/**
 * @author Kang Dowon
 * @version Currency Converter version is up-to date
 *
 * The main of converter which include a important part
 * to run program with API information.
 */
public class ConversionMain extends AppCompatActivity {

    private EditText editTextAmount;
    private Button buttonConvert;
    private Spinner spinnerSourceCurrency;
    private Spinner spinnerTargetCurrency;

    private ArrayAdapter<String> currencyAdapter;
    private String[] currencyCodes;
    private String[] currencyNames;

    private ListView listViewConversions;
    private List<ConversionDAO> conversions;
    private ConversionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversion_main);

        editTextAmount = findViewById(R.id.edit_text_amount);
        buttonConvert = findViewById(R.id.button_convert);
        spinnerSourceCurrency = findViewById(R.id.spinner_source_currency);
        spinnerTargetCurrency = findViewById(R.id.spinner_target_currency);

        listViewConversions = findViewById(R.id.list_view_conversions);
        conversions = new ArrayList<>();
        adapter = new ConversionAdapter(this, conversions);
        listViewConversions.setAdapter(adapter);

        // Call the method to load data from the database
        loadConversionsFromDatabase();

        // Countries select
        setupSpinners();

        // Convert Click -> show currency converter
        buttonConvert.setOnClickListener(v -> {
            String amount = editTextAmount.getText().toString();
            String sourceCurrency = currencyCodes[spinnerSourceCurrency.getSelectedItemPosition()];
            String targetCurrency = currencyCodes[spinnerTargetCurrency.getSelectedItemPosition()];

            new ApiRequestTask().execute(amount, sourceCurrency, targetCurrency);
        });
    }

    private void setupSpinners() {
        // code - short name of country, names -> country + currency
        currencyCodes = getResources().getStringArray(R.array.currency_codes);
        currencyNames = getResources().getStringArray(R.array.currency_names);

        currencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyNames);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSourceCurrency.setAdapter(currencyAdapter);
        spinnerTargetCurrency.setAdapter(currencyAdapter);
    }

    private class ApiRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String amount = params[0];
            String sourceCurrency = params[1];
            String targetCurrency = params[2];

            // Connection with API
            String apiUrl = "https://currency-converter5.p.rapidapi.com/currency/convert?format=json&from="
                    + sourceCurrency + "&to=" + targetCurrency + "&amount=" + amount;

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .get()
                    .addHeader("X-RapidAPI-Key", "1500d68574msh2aa454c8cdc6dc9p184191jsnd2096834e1fa")
                    .addHeader("X-RapidAPI-Host", "currency-converter5.p.rapidapi.com")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return "Error: " + response.code();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String resultString) {
            String sourceCurrency = currencyCodes[spinnerSourceCurrency.getSelectedItemPosition()];
            String targetCurrency = currencyCodes[spinnerTargetCurrency.getSelectedItemPosition()];
            String amount = editTextAmount.getText().toString();

            try {
                JSONObject jsonObject = new JSONObject(resultString);
                JSONObject ratesObject = jsonObject.getJSONObject("rates");
                JSONObject targetCurrencyObject = ratesObject.getJSONObject(currencyCodes[spinnerTargetCurrency.getSelectedItemPosition()]);
                String convertedAmount = targetCurrencyObject.getString("rate_for_amount");

                // Initialization ConversionDAO variables
                ConversionDAO conversionDAO = new ConversionDAO(sourceCurrency, targetCurrency, amount, convertedAmount);
                conversions.add(conversionDAO);
                adapter.notifyDataSetChanged();

                // Insert data into the database
                ConversionDB conversionDB = new ConversionDB(ConversionMain.this);
                SQLiteDatabase db = conversionDB.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(ConversionDB.COLUMN_SOURCE_CURRENCY, sourceCurrency);
                values.put(ConversionDB.COLUMN_TARGET_CURRENCY, targetCurrency);
                values.put(ConversionDB.COLUMN_AMOUNT, amount);
                values.put(ConversionDB.COLUMN_CONVERTED_AMOUNT, convertedAmount);

                long newRowId = db.insert(ConversionDB.TABLE_NAME, null, values);

                if (newRowId == -1) {
                    Toast.makeText(ConversionMain.this, "Error inserting data into the database", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ConversionMain.this, "Data inserted into the database", Toast.LENGTH_SHORT).show();
                }

                // db.close();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ConversionMain.this, "Error parsing response", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadConversionsFromDatabase() {
        conversions.clear();

        // Read data from the database
        ConversionDB conversionDB = new ConversionDB(ConversionMain.this);
        SQLiteDatabase db = conversionDB.getReadableDatabase();

        String[] projection = {
                ConversionDB.COLUMN_SOURCE_CURRENCY,
                ConversionDB.COLUMN_TARGET_CURRENCY,
                ConversionDB.COLUMN_AMOUNT,
                ConversionDB.COLUMN_CONVERTED_AMOUNT
        };

        Cursor cursor = db.query(
                ConversionDB.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String sourceCurrency = cursor.getString(cursor.getColumnIndexOrThrow(ConversionDB.COLUMN_SOURCE_CURRENCY));
            String targetCurrency = cursor.getString(cursor.getColumnIndexOrThrow(ConversionDB.COLUMN_TARGET_CURRENCY));
            String amount = cursor.getString(cursor.getColumnIndexOrThrow(ConversionDB.COLUMN_AMOUNT));
            String convertedAmount = cursor.getString(cursor.getColumnIndexOrThrow(ConversionDB.COLUMN_CONVERTED_AMOUNT));

            ConversionDAO conversionDAO = new ConversionDAO(sourceCurrency, targetCurrency, amount, convertedAmount);
            conversions.add(conversionDAO);
        }

        cursor.close();
        // db.close();

        adapter.notifyDataSetChanged();
    }
}
