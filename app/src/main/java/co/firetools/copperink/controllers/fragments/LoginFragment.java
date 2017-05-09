package co.firetools.copperink.controllers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import co.firetools.copperink.R;
import co.firetools.copperink.controllers.activities.InitialActivity;
import co.firetools.copperink.services.APIService;
import co.firetools.copperink.services.GlobalService;
import co.firetools.copperink.services.UserService;
import cz.msebera.android.httpclient.Header;


public class LoginFragment extends Fragment {
    public LoginFragment() { }

    private boolean loginMode = false;

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmationField;

    private Button   loginButton;
    private Button   switchMode;
    private ProgressBar  loader;
    private LinearLayout formView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        nameField         = (EditText)     root.findViewById(R.id.name_field);
        emailField        = (EditText)     root.findViewById(R.id.email_field);
        passwordField     = (EditText)     root.findViewById(R.id.password_field);
        confirmationField = (EditText)     root.findViewById(R.id.confirmation_field);

        loginButton       = (Button)       root.findViewById(R.id.login_button);
        switchMode        = (Button)       root.findViewById(R.id.switch_mode_button);
        loader            = (ProgressBar)  root.findViewById(R.id.loader);
        formView         = (LinearLayout) root.findViewById(R.id.form_view);

        toggleMode(true);
        startLoading(false);

        switchMode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { toggleMode(); }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (loginMode)
                    doLogin();
                else
                    doSignup();
            }
        });

        return root;
    }


    /**
     * Sets the mode of the login view, hiding
     * or showing fields
     */
    private void toggleMode() { toggleMode(!loginMode); }
    private void toggleMode(boolean mode) {
        loginMode = mode;

        if (mode) {
            nameField.setVisibility(View.GONE);
            confirmationField.setVisibility(View.GONE);
            loginButton.setText("LOGIN");
            switchMode.setText("Create an Account");
            emailField.requestFocus();
        } else {
            nameField.setVisibility(View.VISIBLE);
            confirmationField.setVisibility(View.VISIBLE);
            loginButton.setText("SIGN UP");
            switchMode.setText("Login");
            nameField.requestFocus();
        }
    }


    /**
     * Loader for login view
     */
    private void startLoading(boolean loading) {
        if (loading) {
            formView.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
        } else {
            formView.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        }
    }


    /**
     * Login API Call
     */
    private void doLogin() {
        startLoading(true);

        RequestParams params = new RequestParams();
        params.put("user[email]",    emailField.getText());
        params.put("user[password]", passwordField.getText());

        APIService.Basic.POST("/sessions/sign-in", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject user) {
                startLoading(false);
                UserService.saveUser(user);
                openMainActivity();
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                startLoading(false);
                APIService.handleError(error);
            }
        });
    }


    /**
     * Signup API Call
     */
    private void doSignup() {
        startLoading(true);

        if (!passwordField.getText().equals(confirmationField.getText())){
            GlobalService.showError("Passwords do not match");
            return;
        }

        RequestParams params = new RequestParams();
        params.put("user[name]",     nameField.getText());
        params.put("user[email]",    emailField.getText());
        params.put("user[password]", passwordField.getText());

        APIService.Basic.POST("/sessions/sign-up", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject user) {
                startLoading(false);
                UserService.saveUser(user);
                openMainActivity();
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                startLoading(false);
                APIService.handleError(error);
            }
        });
    }


    /**
     * Continue to MainActivity
     */
    private void openMainActivity() {
        ((InitialActivity) getActivity()).openMainActivity();
    }

}
