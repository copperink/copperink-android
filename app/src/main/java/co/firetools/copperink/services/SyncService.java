package co.firetools.copperink.services;

import android.app.IntentService;
import android.content.Intent;

public class SyncService extends IntentService {
    private final static String TAG = "CopperInk.SyncService";

    /**
     * Default Constructor. Names the worker thread.
     * Useful for debugging.
     */
    public SyncService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // This describes what will happen when service is triggered
    }
}
