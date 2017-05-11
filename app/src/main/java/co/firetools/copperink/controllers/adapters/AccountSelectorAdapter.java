package co.firetools.copperink.controllers.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.firetools.copperink.R;
import co.firetools.copperink.services.GlobalService;

public class AccountSelectorAdapter extends RecyclerView.Adapter<AccountSelectorAdapter.ViewHolder> {
    private List<HashMap> accounts;
    private List<HashMap> selected = new ArrayList<>();


    /**
     * Default Constructor and required method getItemCount()
     */
    public int getItemCount() { return accounts.size(); }
    public AccountSelectorAdapter(List<HashMap> accounts) { this.accounts = accounts; }
    public List<HashMap> getSelected() { return selected; }


    /**
     * Required Static class to initialize rows
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  name;
        ImageView image;
        CheckBox  checkbox;

        ViewHolder(View root) {
            super(root);

            name     = (TextView)  root.findViewById(R.id.account_name);
            image    = (ImageView) root.findViewById(R.id.account_image);
            checkbox = (CheckBox)  root.findViewById(R.id.account_checkbox);
        }
    }


    /**
     * Required method to create rows
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_account_selector, viewGroup, false);
        ViewHolder vh = new ViewHolder(root);
        return vh;
    }


    /**
     * Required method populate row content
     */
    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {
        final HashMap account = accounts.get(position);
        vh.name.setText((String)account.get("name"));
        GlobalService.setImage(vh.image, (String)account.get("image"));

        vh.checkbox.setOnCheckedChangeListener(null);
        vh.checkbox.setChecked(selected.contains(account));

        vh.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    selected.add(account);
                else
                    selected.remove(account);
            }
        });
    }
}
