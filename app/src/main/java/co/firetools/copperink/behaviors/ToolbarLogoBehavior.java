package co.firetools.copperink.behaviors;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.view.ViewGroup;
import android.widget.ImageView;

import co.firetools.copperink.R;
import co.firetools.copperink.services.GlobalService;



/**
 * An Implementation of AppBarOffsetListener to listen for scrolls and
 * change the logo size in Toolbar accordingly
 */
public class ToolbarLogoBehavior implements AppBarLayout.OnOffsetChangedListener {
    private float MAX_LOGO_HEIGHT;
    private float MIN_LOGO_HEIGHT;
    private float MAX_LOGO_MARGIN;
    private float MIN_LOGO_MARGIN;

    private ImageView logo;



    /**
     * Public Constuctor to intialize the Listener with default
     * dimensions
     *
     * @param logo - ImageView in the Toolbar
     */
    public ToolbarLogoBehavior(ImageView logo) {
        Context context = GlobalService.getContext();

        MAX_LOGO_HEIGHT = context.getResources().getDimension(R.dimen.logo_height_max);
        MIN_LOGO_HEIGHT = context.getResources().getDimension(R.dimen.logo_height_min);
        MAX_LOGO_MARGIN = context.getResources().getDimension(R.dimen.logo_margin_left_max);
        MIN_LOGO_MARGIN = context.getResources().getDimension(R.dimen.logo_margin_left_min);

        this.logo = logo;
    }



    /**
     * The actual offset listener callback that listens to appbar offset
     * changes and resizes the logo
     */
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
}