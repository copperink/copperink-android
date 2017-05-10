package co.firetools.copperink.services;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import co.firetools.copperink.R;

public class GlobalService {
    private static Context context;

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
        Picasso
            .with(iv.getContext())
            .load(url)
            .placeholder(R.color.primary)
            .noFade()
            .into(iv);
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
    }
}
