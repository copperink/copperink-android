package co.firetools.copperink.controllers.activities;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import co.firetools.copperink.R;
import co.firetools.copperink.services.GlobalService;
import co.firetools.copperink.services.UserService;

public class InitialActivity extends AppCompatActivity {
    private final static int SPLASH_WAIT = 2000; // ms
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        // Set the Context
        context = getApplicationContext();

        // Make application context available to Global
        GlobalService.setContext(context);

        // Load existing user profile
        UserService.loadUser(context);


        // Wait determined time before showing login
        // or going to next activity
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... unused) {
                SystemClock.sleep(SPLASH_WAIT);
                return null;
            }

            protected void onPostExecute(Void unused) {
                if (UserService.userExists())
                    GlobalService.showToast("User Account Exists");
                else
                    showLoginView();
            }
        }.execute();
    }


    /**
     * Renders the Login view in Fragment
     */
    private void showLoginView() {
        // Initialize Views
        ImageView   logo  = (ImageView)   findViewById(R.id.logo);
        FrameLayout login = (FrameLayout) findViewById(R.id.loginView);

        // Animate the Logo
        ObjectAnimator animator = ObjectAnimator.ofFloat(logo,"y", 150);
        animator.setDuration(2000);
        animator.start();

        // Show the Login Fragment
        login.setVisibility(View.VISIBLE);
    }
}
