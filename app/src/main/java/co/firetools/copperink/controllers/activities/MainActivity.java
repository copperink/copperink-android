package co.firetools.copperink.controllers.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ImageView;

import co.firetools.copperink.R;

public class MainActivity extends AppCompatActivity {
    AppBarLayout appbar;
    Toolbar toolbar;
    ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        toolbar = (Toolbar)      findViewById(R.id.toolbar);
        appbar  = (AppBarLayout) findViewById(R.id.app_bar);
        logo    = (ImageView)    findViewById(R.id.logo);


        // Configure Toolbar
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        // Resize logo according to scroll state
        attachOffsetListener();

    }



    /**
     * Attaches an AppBarOffsetListener to listen for scrolls and
     * change the logo size in Toolbar accordingly
     */
    private void attachOffsetListener() {
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            float MAX_LOGO_HEIGHT = getResources().getDimension(R.dimen.logo_height_max);
            float MIN_LOGO_HEIGHT = getResources().getDimension(R.dimen.logo_height_min);
            float MAX_LOGO_MARGIN = getResources().getDimension(R.dimen.logo_margin_max);
            float MIN_LOGO_MARGIN = getResources().getDimension(R.dimen.logo_margin_min);

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
                // Do calculations when Logo Layout Params are not busy,
                // otherwise pollutes log with warnings
                if (!logo.isInLayout()) {

                    // Get Maximum Scoll Range
                    // This is positive while actual offset is negative
                    int total = appBarLayout.getTotalScrollRange();

                    // Calculate scroll ratio from range
                    // This is 1 when fully opened, and 0 when fully closed
                    float scale  = (float) (total + offset) / total;

                    // Calculate new height between min-max range
                    float height = MIN_LOGO_HEIGHT + ((MAX_LOGO_HEIGHT - MIN_LOGO_HEIGHT) * scale);

                    // Calculate new left-margin
                    float margin = MIN_LOGO_MARGIN + ((MAX_LOGO_MARGIN - MIN_LOGO_MARGIN) * scale);

                    // Apply new Layout Params
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) logo.getLayoutParams();
                    params.leftMargin = (int) margin;
                    params.height     = (int) height;
                    logo.setLayoutParams(params);
                }
            }
        });
    }
}
