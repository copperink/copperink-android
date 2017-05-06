package co.firetools.copperink.controllers.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import co.firetools.copperink.R;

public class AccountListFragment extends Fragment {
    public AccountListFragment() {}

    Toolbar toolbar;
    AppCompatActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account_list, container, false);

        toolbar  = (Toolbar) root.findViewById (R.id.toolbar);
        activity = (AppCompatActivity) getActivity();

        setHasOptionsMenu(true);
        activity.setSupportActionBar(toolbar);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_accounts, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
}
