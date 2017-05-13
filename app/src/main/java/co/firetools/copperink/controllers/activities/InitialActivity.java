package co.firetools.copperink.controllers.activities;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import co.firetools.copperink.R;
import co.firetools.copperink.clients.GlobalClient;
import co.firetools.copperink.clients.UserClient;

public class InitialActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        // Set the Context
        context = getApplicationContext();

        // Make application context available to Global
        GlobalClient.setContext(context);

        // Load existing user profile
        UserClient.loadUser();


        // Wait determined time before showing login
        // or going to next activity
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... unused) {
                int waitTime = context.getResources().getInteger(R.integer.splash_wait_time);
                SystemClock.sleep(waitTime);
                return null;
            }

            protected void onPostExecute(Void unused) {
                if (UserClient.userExists())
                    openMainActivity();
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
        int animTime = context.getResources().getInteger(R.integer.splash_animation_time);
        ObjectAnimator animator = ObjectAnimator.ofFloat(logo,"y", 200);
        animator.setDuration(animTime);
        animator.start();

        // Show the Login Fragment
        Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        login.startAnimation(slideDown);
        login.setVisibility(View.VISIBLE);
    }


    /**
     * Opens MainActivity
     */
    public void openMainActivity() {
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
}
