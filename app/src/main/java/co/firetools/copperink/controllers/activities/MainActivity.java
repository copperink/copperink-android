package co.firetools.copperink.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;
import java.util.List;

import co.firetools.copperink.R;
import co.firetools.copperink.controllers.fragments.AccountListFragment;
import co.firetools.copperink.controllers.fragments.HomeFragment;
import co.firetools.copperink.services.GlobalService;

public class MainActivity extends AppCompatActivity {
    CallbackManager facebookCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Populate HomeFragment
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container, new HomeFragment())
            .commit();


        // Set up Facebook Callbacks
        setFacebookCallbacks();

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
            // So initiate facebook login process
            case R.id.action_add_account:
                GlobalService.showToast("Adding Account");
                List<String> permissions = Arrays.asList(getResources().getStringArray(R.array.facebook_write_permissions));
                LoginManager.getInstance().logInWithPublishPermissions(this, permissions);

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    /**
     * Set up a Facebook Callback Manager to handle appropriate
     * cases of logins
     */
    private void setFacebookCallbacks() {
        facebookCallback = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(facebookCallback,
            new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d("Success", "Login");
                    Log.d("FACEBOOK DATA", loginResult.getAccessToken().getToken());
                }

                @Override
                public void onCancel() {
                    GlobalService.showToast("Login Cancelled");
                }

                @Override
                public void onError(FacebookException exception) {
                    GlobalService.showToast("Error: " + exception.getMessage());
                }
            });
    }



    /**
     * Listen to Facebook callback if a login process is completed
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallback.onActivityResult(requestCode, resultCode, data);
    }

}
