package co.firetools.copperink.controllers.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.firetools.copperink.R;
import co.firetools.copperink.models.Account;
import co.firetools.copperink.models.Post;
import co.firetools.copperink.services.AccountService;
import co.firetools.copperink.services.GlobalService;
import co.firetools.copperink.services.PostService;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<Post> posts;


    /**
     * Default Constructor and required method getItemCount()
     */
    public int getItemCount()            { return posts.size(); }
    public PostAdapter(List<Post> posts) { this.posts = posts;  }


    /**
     * Required Static class to initialize rows
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  postMeta;
        TextView  postContent;
        ImageView postImage;
        ImageView accountImage;

        ViewHolder(View root) {
            super(root);

            postMeta     = (TextView)  root.findViewById(R.id.post_meta);
            postContent  = (TextView)  root.findViewById(R.id.post_content);
            postImage    = (ImageView) root.findViewById(R.id.post_image);
            accountImage = (ImageView) root.findViewById(R.id.account_image);
        }
    }


    /**
     * Required method to create rows
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_post, viewGroup, false);
        ViewHolder vh = new ViewHolder(root);
        return vh;
    }


    /**
     * Required method populate row content
     */
    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {
        Post post = posts.get(position);
        Account account = AccountService.get(post.getAccountID());

        vh.postContent.setText(post.getContent());
        vh.postMeta.setText(post.prettyPostTime());

        PostService.setImage(post, vh.postImage);
        GlobalService.setImage(vh.accountImage, account.getImageUrl());
    }
}
