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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appbar = (AppBarLayout) findViewById(R.id.app_bar);
        logo = (ImageView) findViewById(R.id.logo);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        attachOffsetListener();



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    /**
     * Attaches an AppBarOffsetListener to listen for scrolls and
     * change the logo size in Toolbar accordingly
     */
    private void attachOffsetListener() {
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            float MAX_LOGO_HEIGHT = getResources().getDimension(R.dimen.logo_height_max);
            float MIN_LOGO_HEIGHT = getResources().getDimension(R.dimen.logo_height_min);

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {

                // Get Maximum Scoll Range
                int total = appBarLayout.getTotalScrollRange();

                // Calculate scroll ratio from range
                float scale  = (float) (total + offset) / total;

                // Calculate new height between min-max range
                float height = MIN_LOGO_HEIGHT + ((MAX_LOGO_HEIGHT - MIN_LOGO_HEIGHT) * scale);

                // Apply new Layout Params
                logo.requestLayout();
                ViewGroup.LayoutParams logoParams = logo.getLayoutParams();
                logoParams.height = (int) height;
                logo.requestLayout();
//                logo.setLayoutParams(logoParams);
            }
        });
    }
}
