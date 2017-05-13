package co.firetools.copperink.controllers.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.firetools.copperink.R;
import co.firetools.copperink.behaviors.RVEmptyObserver;
import co.firetools.copperink.controllers.adapters.AccountAdapter;
import co.firetools.copperink.models.Account;
import co.firetools.copperink.clients.AccountClient;

public class AccountListFragment extends Fragment {
    public AccountListFragment() {}

    View emptyView;
    Toolbar toolbar;
    RecyclerView accountList;
    AppCompatActivity activity;
    AccountAdapter adapter;
    ArrayList<Account> accounts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account_list, container, false);

        // Initialize Components
        emptyView   =                root.findViewById(R.id.empty_view);
        toolbar     = (Toolbar)      root.findViewById(R.id.toolbar);
        accountList = (RecyclerView) root.findViewById(R.id.account_list_view);
        activity    = (AppCompatActivity) getActivity();

        // Render Toolbar and Menu
        setHasOptionsMenu(true);
        activity.setSupportActionBar(toolbar);

        // Set up Recycler View
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        accounts = new ArrayList<>();
        adapter = new AccountAdapter(accounts);
        accountList.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RVEmptyObserver(accountList, emptyView));
        accountList.setLayoutManager(llm);

        // Load Accounts from DB
        loadAccounts();

        return root;
    }



    /**
     * Load accounts from DB
     */
    private void loadAccounts() {
        accounts.clear();
        accounts.addAll(AccountClient.getAllAccounts());
        adapter.notifyDataSetChanged();
    }



    /**
     * Render Menu Actions
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_accounts, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }



    /**
     * Handle Menu Options in Toolbar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Add Account Button was pressed
            // So initiate facebook login process
            case R.id.action_add_account:
                List<String> permissions = Arrays.asList(getResources().getStringArray(R.array.facebook_write_permissions));
                LoginManager.getInstance().logInWithPublishPermissions(activity, permissions);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
