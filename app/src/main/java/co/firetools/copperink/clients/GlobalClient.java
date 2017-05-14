package co.firetools.copperink.clients;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

import co.firetools.copperink.R;
import co.firetools.copperink.utils.TinyDB;

public class GlobalClient {
    private static final String TAG = "CopperInk";
    private static Context context;
    private static TinyDB store;


    /**
     * Custom Logger wrapper
     */
    public static void log(String text) {
        Log.d(TAG, text);
    }



    /**
     * Get/Set Application context from anywhere in the app
     */
    public static Context getContext()           { return context;  }
    public static void setContext(Context cntxt) { context = cntxt; }


    /**
     * Set Image into an ImageView using Picasso
     * with default settings
     */
    public static void setImage(ImageView iv, String url) {
        if (iv != null && url != null && !url.isEmpty())
            Picasso
                .with(iv.getContext())
                .load(url)
                .placeholder(R.color.primary)
                .noFade()
                .into(iv);
    }

    public static void setImage(ImageView iv, File file) {
        if (iv != null && file != null)
            Picasso
                .with(iv.getContext())
                .load(file)
                .placeholder(R.color.primary)
                .noFade()
                .into(iv);
    }


    /**
     * Execute callback if valid
     */
    public static void executeCallback(Runnable callback) {
        if (callback != null)
            callback.run();
    }



    /**
     * Get a TinyDB instance
     */
    public static TinyDB getStore() {
        if (store == null)
            store = new TinyDB(GlobalClient.getContext());

        return store;
    }


    /**
     * Show Keyboard
     */
    public static void showKeyboard(Activity activity, EditText view) {
        view.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    /**
     * Hide Keyboard
     */
    public static void hideKeyboard(Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    /**
     * Show Toast with default settings
     */
    public static void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }



    /**
     * Show Error
     */
    public static void showError(String message) {
        showToast("Error: " + message);
        log("ERROR! -- " + message);
    }
}
