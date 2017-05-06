package co.firetools.copperink.controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import co.firetools.copperink.R;
import co.firetools.copperink.controllers.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Populate HomeFragment
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container, new HomeFragment())
            .commit();

    }

}
