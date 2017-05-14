package co.firetools.copperink.clients;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class APIClient {
    private static final String HOST         = "http://copperink.192.168.1.5.xip.io";
    private static final String BASE_URL     = HOST + "/api/v1";
    private static final String AUTH_HEADER  = "X-AUTH-TOKEN";
    private static final String CONTENT_TYPE = "application/json";
    private static AsyncHttpClient client    = new AsyncHttpClient();

    public static final String RESOURCE_ACCOUNT = "account";
    public static final String RESOURCE_POST    = "post";


    /**
     * Checks if connected to the internet
     */
    public static Boolean connectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager) GlobalClient.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    /**
     * Get full image url for host
     */
    public static String imageUrl(String path) {
        return HOST + path;
    }


    /**
     * Handles JSON Error Objects
     */
    public static void handleError(JSONObject errorObject) {
        try {
            JSONArray errors = errorObject.getJSONArray("errors");
            GlobalClient.showError(errors.join(","));
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }


    /**
     * Encode an Image from it's path to Base64
     */
    public static String encodeImage(String path) {
        if (path == null || path.isEmpty())
            return null;

        File imagefile = new File(path);
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(imagefile);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        return "data:image/png;base64," + Base64.encodeToString(b, Base64.DEFAULT);
    }


    /**
     * Prepares JSON Params for Web Request
     */
    private static StringEntity prepareJSONParams(JSONObject json) {
        try {
            StringEntity entity = new StringEntity(json.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE));
            return entity;
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
            client.get(getAbsoluteURL(path), params, responseHandler);
        }

        public static void POST(String path, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.post(getAbsoluteURL(path), params, responseHandler);
        }

        public static void PATCH(String path, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.patch(getAbsoluteURL(path), params, responseHandler);
        }

        public static void DELETE(String path, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.delete(getAbsoluteURL(path), params, responseHandler);
        }

        public static void jsonPOST(String path, JSONObject json, JsonHttpResponseHandler responseHandler) {
            client.post(GlobalClient.getContext(), getAbsoluteURL(path), prepareJSONParams(json), CONTENT_TYPE, responseHandler);
        }

        public static void jsonPATCH(String path, JSONObject json, JsonHttpResponseHandler responseHandler) {
            client.patch(GlobalClient.getContext(), getAbsoluteURL(path), prepareJSONParams(json), CONTENT_TYPE, responseHandler);
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

        public static void DELETE(String path, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            authorizeClient();
            Basic.DELETE(path, params, responseHandler);
        }

        public static void jsonPOST(String path, JSONObject json, JsonHttpResponseHandler responseHandler) {
            authorizeClient();
            Basic.jsonPOST(path, json, responseHandler);
        }

        public static void jsonPATCH(String path, JSONObject json, JsonHttpResponseHandler responseHandler) {
            authorizeClient();
            Basic.jsonPATCH(path, json, responseHandler);
        }

        private static void authorizeClient() {
            client.removeHeader(AUTH_HEADER);
            client.addHeader(AUTH_HEADER, UserClient.getUser().getToken());
        }
    }

}
