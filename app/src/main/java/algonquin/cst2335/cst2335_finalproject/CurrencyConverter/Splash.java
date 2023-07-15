package algonquin.cst2335.cst2335_finalproject.CurrencyConverter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import algonquin.cst2335.cst2335_finalproject.MainActivity;
import algonquin.cst2335.cst2335_finalproject.R;

@SuppressLint("CustomSplashScreen")
public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashandler(), 3000); // maintain 3 sec
    }

    private class splashandler implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class)); // move to main
            Splash.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        // Protect going back
    }
}

// android:name=".bearimages.ui.BearApplication"