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
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.firetools.copperink.R;
import co.firetools.copperink.controllers.adapters.AccountAdapter;
import co.firetools.copperink.db.DB;
import co.firetools.copperink.db.DBContract;
import co.firetools.copperink.models.Account;

public class AccountListFragment extends Fragment {
    public AccountListFragment() {}

    Toolbar toolbar;
    RecyclerView accountList;
    AppCompatActivity activity;
    AccountAdapter adapter;
    ArrayList<Account> accounts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account_list, container, false);

        // Initialize Components
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
        accounts.addAll(DBContract.AccountTable.all(DB.getReadable()));
        adapter.notifyDataSetChanged();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_accounts, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
}
