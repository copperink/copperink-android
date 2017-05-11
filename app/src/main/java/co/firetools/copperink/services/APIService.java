package co.firetools.copperink.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import co.firetools.copperink.db.DB;
import co.firetools.copperink.db.DBContract;
import co.firetools.copperink.models.Account;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class APIService {
    private static final String BASE_URL     = "http://copperink.192.168.1.5.xip.io/api/v1";
    private static final String AUTH_HEADER  = "X-AUTH-TOKEN";
    private static final String CONTENT_TYPE = "application/json";
    private static AsyncHttpClient client    = new AsyncHttpClient();


    /**
     * Load all accounts and save them
     */
    public static void fetchAccounts(final Runnable onFinish){
        Auth.GET("/accounts", null, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                SQLiteDatabase db = DB.getWritable();
                DBContract.AccountTable.deleteAll(db);

                try {
                    JSONArray jsonAccounts = data.getJSONArray("accounts");
                    for (int i = 0; i < jsonAccounts.length(); i++) {
                        Account acc = Account.deserialize(jsonAccounts.get(i).toString());
                        db.insert(DBContract.AccountTable.TABLE_NAME, null, DBContract.AccountTable.contentValues(acc));
                    }
                } catch (JSONException | IOException ex) {
                    ex.printStackTrace();
                }

                onFinish.run();
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                APIService.handleError(error);
                onFinish.run();
            }
        });
    }


    /**
     * Checks if connected to the internet
     */
    public static Boolean connectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager) GlobalService.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



    /**
     * Handles JSON Error Objects
     */
    public static void handleError(JSONObject errorObject) {
        try {
            JSONArray errors = errorObject.getJSONArray("errors");
            GlobalService.showError(errors.join(","));
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }


    /**
     * Prepares JSON Params for Web Request
     */
    private static StringEntity prepareJSONParams(JSONObject json) {
        try {
            return new StringEntity(json.toString());
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }
    }



    /**
     * Forms complete URL for calls
     */
    private static String getAbsoluteURL(String path) {
        return BASE_URL + path;
    }



    /**
     * Class for:
     * Basic API Calls without authentication
     */
    public static class Basic {
        public static void GET(String path, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//            setContentType();
            client.get(getAbsoluteURL(path), params, responseHandler);
        }

        public static void POST(String path, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//            setContentType();
            client.post(getAbsoluteURL(path), params, responseHandler);
        }

        public static void jsonPOST(String path, JSONObject json, JsonHttpResponseHandler responseHandler) {
            client.post(GlobalService.getContext(), getAbsoluteURL(path), prepareJSONParams(json), CONTENT_TYPE, responseHandler);
        }
    }



    /**
     * Class for:
     * Authenticated API Calls
     */
    public static class Auth {
        // TODO:
        // Keep separate http clients for authenticated and basic calls?

        public static void GET(String path, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            authorizeClient();
            Basic.GET(path, params, responseHandler);
        }

        public static void POST(String path, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            authorizeClient();
            Basic.POST(path, params, responseHandler);
        }

        public static void jsonPOST(String path, JSONObject json, JsonHttpResponseHandler responseHandler) {
            authorizeClient();
            Basic.jsonPOST(path, json, responseHandler);
        }

        private static void authorizeClient() {
            client.removeHeader(AUTH_HEADER);
            client.addHeader(AUTH_HEADER, UserService.getUser().getToken());
        }
    }

}
