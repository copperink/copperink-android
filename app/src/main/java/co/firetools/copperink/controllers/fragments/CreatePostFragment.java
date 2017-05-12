package co.firetools.copperink.controllers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import co.firetools.copperink.R;
import co.firetools.copperink.models.Account;
import co.firetools.copperink.services.AccountService;
import co.firetools.copperink.services.GlobalService;

public class CreatePostFragment extends Fragment {
    public CreatePostFragment() { }

    Toolbar   toolbar;
    EditText  postContent;
    TextView  accountName;
    ImageView accountImage;
    AppCompatActivity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_post, container, false);

        // Initialize Components
        toolbar      = (Toolbar)   root.findViewById(R.id.toolbar);
        postContent  = (EditText)  root.findViewById(R.id.post_content);
        accountName  = (TextView)  root.findViewById(R.id.account_name);
        accountImage = (ImageView) root.findViewById(R.id.account_image);
        activity     = (AppCompatActivity) getActivity();

        // Render Toolbar and Menu
        setHasOptionsMenu(true);
        activity.setSupportActionBar(toolbar);

        // Load last used account
        selectAccount(AccountService.getLastUsedAccount());

        // Focus the Content Text
        GlobalService.showKeyboard(getActivity(), postContent);

        return root;
    }



    /**
     * Select an account and show it in the view
     */
    private void selectAccount(Account account) {
        accountName.setText(account.getName());
        GlobalService.setImage(accountImage, account.getImageUrl());
    }



    /**
     * Render Menu Actions
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_post, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }



    /**
     * Handle Menu Options in Toolbar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Done was pressed. Do stuff.
            case R.id.action_done:
                GlobalService.showToast("Done!");
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
