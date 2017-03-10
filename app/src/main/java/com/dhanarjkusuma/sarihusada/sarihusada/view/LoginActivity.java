package com.dhanarjkusuma.sarihusada.sarihusada.view;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dhanarjkusuma.sarihusada.sarihusada.R;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseLogin;
import com.dhanarjkusuma.sarihusada.sarihusada.model.User;
import com.dhanarjkusuma.sarihusada.sarihusada.presenter.LoginPresenter;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.DoRequestInterface;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements DoRequestInterface{

    private EditText mUsernameView;
    private EditText mPasswordView;

    private ProgressDialog dialogLogin;
    private LoginPresenter presenter;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        presenter = new LoginPresenter(this);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        dialogLogin = new ProgressDialog(this);
        dialogLogin.setMessage("Sedang Melakukan Login...");
        dialogLogin.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                presenter.breakLogin();
            }
        });


        session = new SessionManager(this);



        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        if(session.getToken() != null && session.getToken() != ""){
            nextToDashboard(session.getUser());
        }

    }




    private void attemptLogin() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);


        // Store values at the time of the login attempt.
        String email = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        View focusView = null;
        boolean isValid = true;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            isValid = false;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            isValid = false;
        }

        if(focusView!=null){
            focusView.requestFocus();
        }
        if(isValid){
            presenter.doLogin(mUsernameView.getText().toString(), mPasswordView.getText().toString());
        }
    }

    @Override
    public void doRequest() {
        dialogLogin.show();
    }

    @Override
    public void doneRequest(Object data) {
        ResponseLogin responseLogin = (ResponseLogin) data;
        if(responseLogin.getStatus()){
            session.setLogin(responseLogin.getStatus(), responseLogin.getToken(), responseLogin.getUser());
            dialogLogin.dismiss();
            nextToDashboard(responseLogin.getUser());
        }else{
            dialogLogin.dismiss();
            Snackbar.make(findViewById(R.id.mainLogin), "Invalid Credential", Snackbar.LENGTH_LONG)
                    .setAction("TRY AGAIN", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            attemptLogin();
                        }
                    })
                    .setActionTextColor(Color.WHITE)
                    .show();

        }

    }

    @Override
    public void failureRequest(String message) {
        dialogLogin.dismiss();
        Snackbar.make(findViewById(R.id.mainLogin), message, Snackbar.LENGTH_LONG)
                .setAction("TRY AGAIN", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        attemptLogin();
                    }
                })
                .setActionTextColor(Color.WHITE)
                .show();
    }

    @Override
    public void onUnAuthorized() {

    }

    private void nextToDashboard(User user){
        Intent nextPage = null;
        switch (user.getLevel()){
            case "mypro":
                nextPage = new Intent(this, DashboardMyproActivity.class);
                nextPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case "sh":
                nextPage = new Intent(this, DashboardSHActivity.class);
                nextPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case "reps":
                nextPage = new Intent(this, DashboardRepsActivity.class);
                nextPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
        }

        if(nextPage != null){
            startActivity(nextPage);
            finish();
        }
    }
}

