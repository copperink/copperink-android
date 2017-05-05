package co.firetools.copperink.services;

import android.content.Context;

public class GlobalService {
    private static Context context;

    /**
     * Get/Set Application context from anywhere in the app
     */
    public static Context getContext()           { return context;  }
    public static void setContext(Context cntxt) { context = cntxt; }
}
