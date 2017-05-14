package co.firetools.copperink.controllers.fragments;

import android.content.Context;
import android.content.Intent;
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

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import co.firetools.copperink.R;
import co.firetools.copperink.db.DBContract;
import co.firetools.copperink.db.DBQuery;
import co.firetools.copperink.models.Account;
import co.firetools.copperink.models.Post;
import co.firetools.copperink.clients.AccountClient;
import co.firetools.copperink.clients.GlobalClient;
import co.firetools.copperink.clients.PostClient;

import static android.app.Activity.RESULT_OK;

public class CreatePostFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public CreatePostFragment() { }

    private final static int IMAGE_PICKER_REQUEST_CODE = 10;
    private final static int MINIMUM_TIME_DIFF = 10000; // ms

    Toolbar   toolbar;
    ImageView postImage;
    EditText  postContent;
    TextView  postDateTime;
    TextView  accountName;
    ImageView accountImage;
    Image     selectedImage;
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
        postImage       = (ImageView)    root.findViewById(R.id.post_image);
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
        setAccount(AccountClient.getLastUsedAccount());

        // Focus the Content Text
        GlobalClient.showKeyboard(getActivity(), postContent);

        // Allow the user to select another account
        attachAccountSelectorListener();

        // Use Current time + 5 hours
        selectedDateTime = Calendar.getInstance();
        selectedDateTime.add(Calendar.HOUR, 5);
        setDateTime();

        // Allow the user to select custom time
        attachDateTimePicker();

        // Allow the user to select an image
        attachImageSelectorListener();

        return root;
    }



    /**
     * Select an account and show it in the view
     */
    private void setAccount(Account account) {
        accountName.setText(account.getName());
        GlobalClient.setImage(accountImage, account.getImageUrl());
        selectedAccount = account;
        AccountClient.setLastUsedAccount(account);
    }



    /**
     * Set and show the DateTime in View
     */
    private void setDateTime() {
        postDateTime.setText(PostClient.dateToString(selectedDateTime));
    }


    /**
     * Set selected image
     */
    private void setImage(Image image) {
        selectedImage = image;
        File file = new File(image.getPath());
        GlobalClient.setImage(postImage, file);
        postImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        postImage.setElevation(5);
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
     * Listener to select post image
     */
    public void attachImageSelectorListener() {
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker
                    .create(CreatePostFragment.this)
                    .single()
                    .returnAfterFirst(true)
                    .showCamera(true)
                    .imageTitle("Select an Image")
                    .start(IMAGE_PICKER_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);

            if (images.size() > 0)
                setImage(images.get(0));
        }
    }




    /**
     * OnClickListener to select Accounts
     */
    private void attachAccountSelectorListener() {
        final ArrayList<Account> accounts = AccountClient.getAllAccounts();
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
                        GlobalClient.setImage(image, account.getImageUrl());

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
     * Save Post
     */
    private void savePost() {
        long timestamp = selectedDateTime.getTimeInMillis();
        long timediff  = timestamp - Calendar.getInstance().getTimeInMillis();

        String imagePath = (selectedImage == null) ? null : selectedImage.getPath();

        if (timediff > MINIMUM_TIME_DIFF) {
            Post post = new Post(
                postContent.getText().toString(),
                selectedAccount.getID(),
                imagePath,
                timestamp
            );

            DBQuery.insert(new DBContract.PostTable(), post);
            PostClient.uploadPost(post, null);
            getActivity().onBackPressed();
        } else {
            GlobalClient.showError("Choose a time in the future");
        }
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
                savePost();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
