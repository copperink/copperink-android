package co.firetools.copperink.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import co.firetools.copperink.R;
import co.firetools.copperink.controllers.fragments.AccountListFragment;
import co.firetools.copperink.controllers.fragments.CreatePostFragment;
import co.firetools.copperink.controllers.fragments.FacebookAccountSelectorFragment;
import co.firetools.copperink.controllers.fragments.HomeFragment;
import co.firetools.copperink.db.DB;
import co.firetools.copperink.services.AccountService;
import co.firetools.copperink.services.GlobalService;
import co.firetools.copperink.services.UserService;

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
     * Helper method to push fragments on stack
     */
    public void pushFragment(Fragment fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.exit_to_right, R.anim.enter_from_left)
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit();
    }



    /**
     * Account Button click callback
     */
    public void accountButtonPressed(View v) {
        pushFragment(new AccountListFragment());
    }



    /**
     * FAB Pressed - Create a new post
     * (if user has at least one account)
     */
    public void fabPressed(View v) {
        if (AccountService.hasAtleastOneAccount()) {
            pushFragment(new CreatePostFragment());
        } else {
            GlobalService.showToast("Connect a Social Account first");
            accountButtonPressed(null);
        }
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


            // Logout Button was pressed
            case R.id.action_logout:
                UserService.destroyUser();
                Intent i = new Intent(this, InitialActivity.class);
                startActivity(i);
                finish();
                return true;


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
                    String token = loginResult.getAccessToken().getToken();

                    // Open Facebook Account Selector
                    pushFragment(FacebookAccountSelectorFragment.create(token));
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



    /**
     * Close Database connections when app closes
     */
    @Override
    protected void onDestroy() {
        DB.close();
        super.onDestroy();
    }

}
