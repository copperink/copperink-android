package co.firetools.copperink.controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.View;

import co.firetools.copperink.R;
import co.firetools.copperink.controllers.fragments.AccountListFragment;
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



    /**
     * Account Button click callback
     */
    public void accountButtonPressed(View v) {
        AccountListFragment af = new AccountListFragment();

        af.setExitTransition(new Fade());
        af.setEnterTransition(new Fade());

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container, af)
            .addToBackStack(null)
            .commit();
    }

}
