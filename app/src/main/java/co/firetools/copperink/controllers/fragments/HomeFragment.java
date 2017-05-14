package co.firetools.copperink.controllers.fragments;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import co.firetools.copperink.R;
import co.firetools.copperink.behaviors.RVEmptyObserver;
import co.firetools.copperink.behaviors.ToolbarLogoBehavior;
import co.firetools.copperink.clients.PostClient;
import co.firetools.copperink.controllers.activities.MainActivity;
import co.firetools.copperink.controllers.adapters.PostAdapter;
import co.firetools.copperink.models.Post;

public class HomeFragment extends Fragment {
    public HomeFragment() { }

    AppBarLayout appbar;
    Toolbar toolbar;
    ImageView logo;
    View emptyView;

    RecyclerView postList;
    PostAdapter adapter;
    ArrayList<Post> posts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Views
        emptyView =                root.findViewById(R.id.empty_view);
        logo      = (ImageView)    root.findViewById(R.id.logo);
        toolbar   = (Toolbar)      root.findViewById(R.id.toolbar);
        appbar    = (AppBarLayout) root.findViewById(R.id.app_bar);
        postList  = (RecyclerView) root.findViewById(R.id.post_list_view);

        // Configure Toolbar
        toolbar.setTitle("");

        // Resize logo according to scroll state
        appbar.addOnOffsetChangedListener(new ToolbarLogoBehavior(logo));

        // Set up Recycler View
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        posts = new ArrayList<>();
        adapter = new PostAdapter(this, posts);
        adapter.registerAdapterDataObserver(new RVEmptyObserver(postList, emptyView));
        postList.setAdapter(adapter);
        postList.setNestedScrollingEnabled(false);
        postList.setLayoutManager(llm);

        // Load Posts from DB
        loadPosts();

        // Sync with webserver to get latest data
        PostClient.syncPosts(new Runnable() {
            @Override
            public void run() {
                loadPosts();
            }
        });

        return root;
    }


    /**
     * Load all queued posts from DB
     */
    private void loadPosts() {
        posts.clear();
        posts.addAll(PostClient.whereStatusIs(Post.STATUS_QUEUED));
        adapter.notifyDataSetChanged();
    }


    /**
     * Edit post
     */
    public void editPost(Post post) {
        CreatePostFragment cpf = CreatePostFragment.inEditMode(post.getOID());
        ((MainActivity) getActivity()).pushFragment(cpf);
    }

}
