package co.firetools.copperink.controllers.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import co.firetools.copperink.R;
import co.firetools.copperink.controllers.fragments.AccountListFragment;
import co.firetools.copperink.controllers.fragments.HomeFragment;
import co.firetools.copperink.services.GlobalService;

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

        getSupportFragmentManager()
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.exit_to_right, R.anim.enter_from_left)
            .replace(R.id.container, af)
            .addToBackStack(null)
            .commit();
    }



    /**
     * Handle Menu Options in Toolbar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Pop the Fragment if it's the "Up" button
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;


            // Add Account Button was pressed
            case R.id.action_add_account:
                GlobalService.showToast("Adding Account");

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
