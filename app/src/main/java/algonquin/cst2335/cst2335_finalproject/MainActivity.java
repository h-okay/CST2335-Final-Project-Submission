package algonquin.cst2335.cst2335_finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.cst2335_finalproject.bearimages.ui.BearApplication;
import algonquin.cst2335.cst2335_finalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences prefs;
    private ActivityResultLauncher<Intent> bearApplicationLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.bear) {
            Intent intent = new Intent(MainActivity.this, BearApplication.class);
            bearApplicationLauncher.launch(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}