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
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.firetools.copperink.R;
import co.firetools.copperink.controllers.adapters.AccountSelectorAdapter;
import co.firetools.copperink.services.APIService;
import co.firetools.copperink.utils.SimpleJSON;
import cz.msebera.android.httpclient.Header;

public class FacebookAccountSelectorFragment extends Fragment {
    public FacebookAccountSelectorFragment() {}

    String                 token;
    Toolbar                toolbar;
    ProgressBar            loader;
    RecyclerView           accountList;
    ArrayList<HashMap>     accounts;
    AppCompatActivity      activity;
    AccountSelectorAdapter adapter;


    /**
     * Fragment creator for given facebook token
     */
    public static FacebookAccountSelectorFragment create(String token) {
        FacebookAccountSelectorFragment fasf = new FacebookAccountSelectorFragment();
        fasf.token = token;
        return fasf;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_facebook_account_selector, container, false);

        // Initialize Components
        toolbar     = (Toolbar)      root.findViewById(R.id.toolbar);
        loader      = (ProgressBar)  root.findViewById(R.id.loader);
        accountList = (RecyclerView) root.findViewById(R.id.account_list_view);
        activity    = (AppCompatActivity) getActivity();

        setHasOptionsMenu(true);
        activity.setSupportActionBar(toolbar);

        // Set up Recycler View
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        accounts = new ArrayList<>();
        adapter = new AccountSelectorAdapter(accounts);
        accountList.setAdapter(adapter);
        accountList.setLayoutManager(llm);

        // Show all user facebook accounts that can be added
        fetchAccountsFromToken();

        return root;
    }


    /**
     * Get postable accounts
     */
    private void fetchAccountsFromToken(){
        startLoading(true);

        RequestParams params = new RequestParams();
        params.put("facebook[token]", token);

        APIService.Auth.POST("/accounts/facebook/list", params, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                try {
                    accounts.addAll(SimpleJSON.getList(data, "accounts"));
                    adapter.notifyDataSetChanged();
                } catch (JSONException je) {
                    je.printStackTrace();
                }

                startLoading(false);
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                startLoading(false);
                APIService.handleError(error);
            }
        });
    }


    /**
     * Save Selected Accounts and reload them
     */
    private void saveSelectedAccounts() {
        startLoading(true);

        try {
            JSONObject params = new JSONObject();
            params.put("accounts", SimpleJSON.toJSON(adapter.getSelected()));

            APIService.Auth.jsonPOST("/accounts/facebook/save", params, new JsonHttpResponseHandler(){
                public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                    APIService.fetchAccounts(new Runnable() {
                        @Override
                        public void run() {
                            startLoading(false);
                            getActivity().onBackPressed();
                        }
                    });
                }
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                    startLoading(false);
                    APIService.handleError(error);
                }
            });

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }


    /**
     * Loader for login view
     */
    private void startLoading(boolean loading) {
        if (loading) {
            accountList.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
        } else {
            accountList.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        }
    }


    /**
     * Render Menu Actions
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account_selector, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    /**
     * Handle Menu Options in Toolbar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Done Button was pressed
            case R.id.action_done:
                saveSelectedAccounts();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
