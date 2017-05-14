package co.firetools.copperink.clients;

import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import co.firetools.copperink.behaviors.Model;
import co.firetools.copperink.db.DBContract;
import co.firetools.copperink.db.DBQuery;
import co.firetools.copperink.models.Post;
import cz.msebera.android.httpclient.Header;

import static co.firetools.copperink.clients.GlobalClient.executeCallback;

public class PostClient {
    private final static String DATETIME_FORMAT = "hh:mm aaa (MMM d, yyyy)";

    /**
     * Load all posts from the DB that are queued
     * (still not posted)
     */
    public static ArrayList<Post> whereStatusIs(String status) {
        Model.Contract contract = new DBContract.PostTable();
        Cursor cursor = DBQuery.getCursor(contract, "status = ?", new String[] { status }, DBContract.COLUMN_POST_AT );
        return (ArrayList<Post>) DBQuery.getAll(contract, cursor);
    }


    /**
     * Upload Post
     */
    public static void uploadPost(final Post post, final Runnable onFinish) {
        try {
            JSONObject params = new JSONObject();
            params.put("post", Post.prepareForRequest(post));
            params.put("image_data", APIClient.encodeImage(post.getImageUrl()));

            APIClient.Auth.jsonPOST("/posts", params, new JsonHttpResponseHandler(){
                public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                    try {
                        JSONObject jsonPost = data.getJSONObject("post");
                        Post updatedPost = Post.deserialize(jsonPost.toString());

                        Model.Contract contract = new DBContract.PostTable();
                        GlobalClient.log("UPLOADING: " + post.getOID());
                        DBQuery.deleteBy(contract, DBContract.COLUMN_OID, Long.toString(post.getOID()));
                        DBQuery.insert(contract, updatedPost);
                    } catch (JSONException | IOException ex) {
                        ex.printStackTrace();
                    }

                    GlobalClient.executeCallback(onFinish);
                }
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                    APIClient.handleError(error);
                    executeCallback(onFinish);
                }
            });

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Fetch all posts, clear local db and repopulate
     */
    public static void fetchPosts(final Runnable onFinish){
        APIClient.Auth.GET("/posts", null, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                Model.Contract contract = new DBContract.PostTable();
                DBQuery.deleteAll(contract);

                try {
                    JSONArray jsonPosts = data.getJSONArray("posts");
                    for (int i = 0; i < jsonPosts.length(); i++) {
                        Post post = Post.deserialize(jsonPosts.get(i).toString());
                        DBQuery.insert(contract, post);
                    }
                } catch (JSONException | IOException ex) {
                    ex.printStackTrace();
                }

                executeCallback(onFinish);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                APIClient.handleError(error);
                executeCallback(onFinish);
            }
        });
    }


    /**
     * Sync posts with webserver
     */
    public static void syncPosts(Runnable onFinish) {
        if (APIClient.connectedToInternet()) {
            Model.Contract contract = new DBContract.PostTable();
            Cursor cursor = DBQuery.getCursor(contract, "synced = ?", new String[] { "0" }, DBContract.COLUMN_POST_AT );
            ArrayList<Post> posts = (ArrayList<Post>) DBQuery.getAll(contract, cursor);
            syncPosts(posts, onFinish);
        } else {
            executeCallback(onFinish);
        }
    }

    private static void syncPosts(ArrayList<Post> posts, final Runnable onFinish) {
        if (posts.size() == 0) {
            fetchPosts(onFinish);
        } else {
            Post post = posts.get(0);
            posts.remove(0);

            final ArrayList<Post> rest = posts;
            uploadPost(post, new Runnable() {
                @Override
                public void run() {
                    syncPosts(rest, onFinish);
                }
            });
        }
    }



    /**
     * Set a Post's image in an ImageView depending if
     * it's locally stored or on the cloud
     */
    public static void setImage(Post post, ImageView iv) {
        String image = post.getImageUrl();

        if (image != null && !image.isEmpty()) {
            if (post.isSynced()) {
                GlobalClient.setImage(iv, APIClient.imageUrl(image));
            } else
                GlobalClient.setImage(iv, new File(image));

        } else {
            iv.setVisibility(View.GONE);
        }
    }


    /**
     * Prints a date in a human readable date
     */
    public static String dateToString(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return dateToString(calendar);
    }
    public static String dateToString(Calendar calendar) {
        return dateToString(calendar.getTime());
    }
    public static String dateToString(Date datetime) {
        return new SimpleDateFormat(DATETIME_FORMAT).format(datetime);
    }
}
