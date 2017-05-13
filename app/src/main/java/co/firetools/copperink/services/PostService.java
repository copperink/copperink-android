package co.firetools.copperink.services;

import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import co.firetools.copperink.behaviors.Model;
import co.firetools.copperink.db.DBContract;
import co.firetools.copperink.db.DBQuery;
import co.firetools.copperink.models.Post;

public class PostService {
    private final static String DATETIME_FORMAT = "hh:mm aaa (MMM d, yyyy)";

    /**
     * Load all posts from the DB that are queued
     * (still not posted)
     */
    public static ArrayList<Post> whereStatusIs(String status) {
        Model.Contract contract = new DBContract.PostTable();
        Cursor cursor = DBQuery.getCursor(contract, "status = ?", new String[] { status });
        return (ArrayList<Post>) DBQuery.getAll(contract, cursor);
    }


    /**
     * Set a Post's image in an ImageView depending if
     * it's locally stored or on the cloud
     */
    public static void setImage(Post post, ImageView iv) {
        String imageUrl = post.getImageUrl();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (post.isSynced())
                GlobalService.setImage(iv, post.getImageUrl());
            else
                GlobalService.setImage(iv, new File(post.getImageUrl()));

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
