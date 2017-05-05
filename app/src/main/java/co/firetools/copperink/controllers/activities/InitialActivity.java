package co.firetools.copperink.controllers.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import co.firetools.copperink.R;
import co.firetools.copperink.services.GlobalService;
import co.firetools.copperink.services.UserService;

public class InitialActivity extends AppCompatActivity {
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

        if (UserService.userExists())
            GlobalService.showToast("User Account Exists");
        else
            GlobalService.showToast("User Account NOT FOUND");
    }
}
