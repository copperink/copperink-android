package co.firetools.copperink.controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import co.firetools.copperink.R;
import co.firetools.copperink.models.Account;
import co.firetools.copperink.services.AccountService;
import co.firetools.copperink.services.GlobalService;

public class CreatePostFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public CreatePostFragment() { }

    private final static String DATETIME_FORMAT = "hh:mm aaa (MMM d, yyyy)";
    private final static SimpleDateFormat SDF = new SimpleDateFormat(DATETIME_FORMAT);

    Toolbar   toolbar;
    EditText  postContent;
    TextView  postDateTime;
    TextView  accountName;
    ImageView accountImage;
    Account   selectedAccount;
    Calendar  selectedDateTime;
    LinearLayout timeSelector;
    LinearLayout accountSelector;
    AppCompatActivity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_post, container, false);

        // Initialize Components
        toolbar         = (Toolbar)      root.findViewById(R.id.toolbar);
        postContent     = (EditText)     root.findViewById(R.id.post_content);
        postDateTime    = (TextView)     root.findViewById(R.id.post_datetime);
        accountName     = (TextView)     root.findViewById(R.id.account_name);
        accountImage    = (ImageView)    root.findViewById(R.id.account_image);
        accountSelector = (LinearLayout) root.findViewById(R.id.account_selector);
        timeSelector    = (LinearLayout) root.findViewById(R.id.time_selector);
        activity        = (AppCompatActivity) getActivity();

        // Render Toolbar and Menu
        setHasOptionsMenu(true);
        activity.setSupportActionBar(toolbar);

        // Load last used account
        setAccount(AccountService.getLastUsedAccount());

        // Focus the Content Text
        GlobalService.showKeyboard(getActivity(), postContent);

        // Allow the user to select another account
        attachAccountSelectorListener();

        // Use Current time + 5 hours
        selectedDateTime = Calendar.getInstance();
        selectedDateTime.add(Calendar.HOUR, 5);
        setDateTime();

        // Allow the user to select custom time
        attachDateTimePicker();

        return root;
    }



    /**
     * Select an account and show it in the view
     */
    private void setAccount(Account account) {
        accountName.setText(account.getName());
        GlobalService.setImage(accountImage, account.getImageUrl());
        selectedAccount = account;
        AccountService.setLastUsedAccount(account);
    }



    /**
     * Set and show the DateTime in View
     */
    private void setDateTime() {
        postDateTime.setText(SDF.format(selectedDateTime.getTime()));
    }



    /**
     * Date and Time picker callbacks
     */
    private void attachDateTimePicker() {
        timeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                    CreatePostFragment.this,
                    selectedDateTime.get(Calendar.YEAR),
                    selectedDateTime.get(Calendar.MONTH),
                    selectedDateTime.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "datepicker");
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        selectedDateTime.set(Calendar.YEAR, year);
        selectedDateTime.set(Calendar.MONTH, monthOfYear);
        selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setDateTime();

        TimePickerDialog tpd = TimePickerDialog.newInstance(
                CreatePostFragment.this,
                selectedDateTime.get(Calendar.HOUR_OF_DAY),
                selectedDateTime.get(Calendar.MINUTE),
                selectedDateTime.get(Calendar.SECOND),
                false
        );
        tpd.show(getActivity().getFragmentManager(), "timepicker");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedDateTime.set(Calendar.MINUTE, minute);
        selectedDateTime.set(Calendar.SECOND, second);
        setDateTime();
    }




    /**
     * OnClickListener to select Accounts
     */
    private void attachAccountSelectorListener() {
        final ArrayList<Account> accounts = AccountService.getAllAccounts();
        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        accountSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build a ListView of Accounts
                ListView lv = new ListView(getContext());
                lv.setAdapter(new ArrayAdapter<String>(getContext(), -1) {
                    public int getCount() { return accounts.size(); }

                    public View getView(int position, View root, ViewGroup parent) {
                        if (root == null)
                            root = inflater.inflate(R.layout.row_account_dialog, parent, false);

                        TextView  name  = (TextView)  root.findViewById(R.id.account_name);
                        ImageView image = (ImageView) root.findViewById(R.id.account_image);
                        Account account = accounts.get(position);
                        name.setText(account.getName());
                        GlobalService.setImage(image, account.getImageUrl());

                        return root;
                    }
                });

                // Show the Selection Dialog
                final AlertDialog dialog =
                    new AlertDialog.Builder(getContext())
                        .setTitle("Select Account")
                        .setView(lv)
                        .show();

                // Attach Account Click listener
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        setAccount(accounts.get(position));
                        dialog.dismiss();
                    }
                });
            }
        });
    }



    /**
     * Render Menu Actions
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_post, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }



    /**
     * Handle Menu Options in Toolbar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Done was pressed. Do stuff.
            case R.id.action_done:
                GlobalService.showToast("Done!");
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
