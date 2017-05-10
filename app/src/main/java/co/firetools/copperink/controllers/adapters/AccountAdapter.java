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
import co.firetools.copperink.services.GlobalService;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    List<Account> accounts;


    /**
     * Default Constructor and required method getItemCount()
     */
    public int getItemCount()                     { return accounts.size(); }
    public AccountAdapter(List<Account> accounts) { this.accounts = accounts; }


    /**
     * Required Static class to initialize rows
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  name;
        ImageView image;

        ViewHolder(View root) {
            super(root);

            name  = (TextView)  root.findViewById(R.id.account_name);
            image = (ImageView) root.findViewById(R.id.account_image);
        }
    }


    /**
     * Required method to create rows
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_account, viewGroup, false);
        ViewHolder vh = new ViewHolder(root);
        return vh;
    }


    /**
     * Required method populate row content
     */
    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {
        final Account account = accounts.get(position);
        vh.name.setText(account.getName());
        GlobalService.setImage(vh.image, account.getImageUrl());
    }
}
