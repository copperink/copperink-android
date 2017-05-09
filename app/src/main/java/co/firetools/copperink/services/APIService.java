package co.firetools.copperink.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class APIService {
    private static final String BASE_URL    = "http://copperink.192.168.1.5.xip.io/api/v1";
    private static final String AUTH_HEADER = "X-AUTH-TOKEN";
    private static AsyncHttpClient client   = new AsyncHttpClient();




    /**
     * Checks if connected to the internet
     */
    public static Boolean connectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager) GlobalService.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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
            client.get(getAbsoluteURL(path), params, responseHandler);
        }

        public static void POST(String path, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.post(getAbsoluteURL(path), params, responseHandler);
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

        private static void authorizeClient() {
            client.removeHeader(AUTH_HEADER);
            client.addHeader(AUTH_HEADER, UserService.getUser().getToken());
        }
    }

}
