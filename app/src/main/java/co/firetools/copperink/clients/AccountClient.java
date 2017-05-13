package co.firetools.copperink.clients;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import co.firetools.copperink.behaviors.Model;
import co.firetools.copperink.db.DBContract;
import co.firetools.copperink.db.DBQuery;
import co.firetools.copperink.models.Account;
import cz.msebera.android.httpclient.Header;

import static co.firetools.copperink.clients.GlobalClient.getStore;

public class AccountClient {
    private static final String KEY_LAST_ACCOUNT_ID = "CopperAccountLast";


    /**
     * Checks if user has at least one account
     */
    public static boolean hasAtleastOneAccount(){
        return (DBQuery.count(new DBContract.AccountTable()) > 0);
    }



    /**
     * Set Last Used account
     */
    public static Account getLastUsedAccount() {
        String id = GlobalClient.getStore().getString(KEY_LAST_ACCOUNT_ID);
        GlobalClient.log("Last Used Account ID: " + id);

        if (id == null || id.isEmpty())
            return (Account) DBQuery.first(new DBContract.AccountTable());
        else
            return get(id);
    }


    /**
     * Get an Account by ID
     */
    public static Account get(String id) {
        return (Account) DBQuery.findBy(new DBContract.AccountTable(), DBContract.COLUMN_ID, id);
    }



    /**
     * Set Last Used account
     */
    public static void setLastUsedAccount(Account account) {
        getStore().putString(KEY_LAST_ACCOUNT_ID, account.getID());
    }


    /**
     * Get all Accounts
     */
    public static ArrayList<Account> getAllAccounts() {
        return (ArrayList<Account>) DBQuery.getAll(new DBContract.AccountTable());
    }



    /**
     * Fetch all accounts from server and save them
     */
    public static void fetchAccounts(final Runnable onFinish){
        APIClient.Auth.GET("/accounts", null, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                Model.Contract contract = new DBContract.AccountTable();
                DBQuery.deleteAll(contract);

                try {
                    JSONArray jsonAccounts = data.getJSONArray("accounts");
                    for (int i = 0; i < jsonAccounts.length(); i++) {
                        Account account = Account.deserialize(jsonAccounts.get(i).toString());
                        DBQuery.insert(contract, account);
                    }
                } catch (JSONException | IOException ex) {
                    ex.printStackTrace();
                }

                onFinish.run();
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                APIClient.handleError(error);
                onFinish.run();
            }
        });
    }

}
