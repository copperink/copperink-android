package co.firetools.copperink.services;

import android.content.Context;
import android.widget.Toast;

public class GlobalService {
    private static Context context;

    /**
     * Get/Set Application context from anywhere in the app
     */
    public static Context getContext()           { return context;  }
    public static void setContext(Context cntxt) { context = cntxt; }


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
