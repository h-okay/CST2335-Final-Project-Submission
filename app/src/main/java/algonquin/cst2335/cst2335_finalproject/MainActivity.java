package algonquin.cst2335.cst2335_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import algonquin.cst2335.cst2335_finalproject.bearimages.ui.BearApplication;
import algonquin.cst2335.cst2335_finalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.aviation) {
            //Intent intent = new Intent(MainActivity.this, AviationApplication.class);
            //startActivity(intent);
        }
        if (itemId == R.id.trivia) {
            //Intent intent = new Intent(MainActivity.this, TriviaApplication.class);
            //startActivity(intent);
        }
        if (itemId == R.id.bear) {
            Intent intent = new Intent(MainActivity.this, BearApplication.class);
            startActivity(intent);
        }
        if (itemId == R.id.currency) {
            //Intent intent = new Intent(MainActivity.this, CurrencyApplication.class);
            //startActivity(intent);
        }
        return true;
    }
}