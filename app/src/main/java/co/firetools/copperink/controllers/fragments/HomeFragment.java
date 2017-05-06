package co.firetools.copperink.controllers.fragments;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import co.firetools.copperink.R;
import co.firetools.copperink.behaviors.ToolbarLogoBehavior;

public class HomeFragment extends Fragment {
    public HomeFragment() { }

    AppBarLayout appbar;
    Toolbar toolbar;
    ImageView logo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Views
        logo    = (ImageView)    root.findViewById(R.id.logo);
        toolbar = (Toolbar)      root.findViewById(R.id.toolbar);
        appbar  = (AppBarLayout) root.findViewById(R.id.app_bar);

        // Configure Toolbar
        toolbar.setTitle("");

        // Resize logo according to scroll state
        appbar.addOnOffsetChangedListener(new ToolbarLogoBehavior(logo));

        return root;
    }

}
