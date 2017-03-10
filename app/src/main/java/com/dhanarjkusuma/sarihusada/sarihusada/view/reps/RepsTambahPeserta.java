package com.dhanarjkusuma.sarihusada.sarihusada.view.reps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dhanarjkusuma.sarihusada.sarihusada.R;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponsePost;
import com.dhanarjkusuma.sarihusada.sarihusada.model.SpinnerValue;
import com.dhanarjkusuma.sarihusada.sarihusada.presenter.FormInsPesertaPresenter;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SpinnerSetter;
import com.dhanarjkusuma.sarihusada.sarihusada.view.LoginActivity;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.FormInsPesertaInterface;

import java.util.ArrayList;
import java.util.List;

public class RepsTambahPeserta extends AppCompatActivity implements FormInsPesertaInterface, View.OnClickListener {

    private FormInsPesertaPresenter presenter;
    private ProgressDialog progressDialog;
    private List<SpinnerValue> klotersValue;
    private Button submitBtn;

    private EditText name;
    private EditText alamat;
    private Spinner kloterSpinner;

    private SessionManager session;
    private TextView reps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reps_tambah_peserta);

        kloterSpinner = (Spinner) findViewById(R.id.kloter);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        name = (EditText) findViewById(R.id.name);
        alamat = (EditText) findViewById(R.id.alamat);
        reps = (TextView) findViewById(R.id.reps);

        klotersValue = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        session = new SessionManager(this);
        reps.setText(session.getUser().getReps().getName());

        presenter = new FormInsPesertaPresenter(this, this);
        presenter.doPrepareForm();


        submitBtn.setOnClickListener(this);
    }

    @Override
    public void doPrepareForm() {
        progressDialog.setMessage("Loading data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void donePrepareForm(Object data) {
        progressDialog.dismiss();
        ResponseGetKloter responseGetKloter = (ResponseGetKloter) data;
        Log.d(RepsTambahPeserta.class.getName(), String.valueOf(responseGetKloter.getStatus()));
        if(responseGetKloter.getStatus()){
            for(int i=0;i<responseGetKloter.getData().size();i++){
                SpinnerValue spinnerValue = new SpinnerValue();
                spinnerValue.setKey(responseGetKloter.getData().get(i).getName());
                spinnerValue.setValue(responseGetKloter.getData().get(i).getId());
                klotersValue.add(spinnerValue);
            }
            SpinnerSetter.setSpinner(this, kloterSpinner, klotersValue, android.R.layout.simple_spinner_dropdown_item);
        }

    }

    @Override
    public void failurePrepareForm(String message) {
        progressDialog.dismiss();
        Snackbar.make(findViewById(R.id.formPage), message, Snackbar.LENGTH_LONG)
                .setAction("TRY AGAIN", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        klotersValue = new ArrayList<SpinnerValue>();
                        presenter.doPrepareForm();
                    }
                })
                .setActionTextColor(Color.WHITE)
                .show();
    }

    @Override
    public void doRequest() {
        progressDialog.setMessage("Sedang mempost data...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    public void doneRequest(Object data) {
        ResponsePost response = (ResponsePost) data;
        if(response.getStatus()){
            Snackbar.make(findViewById(R.id.formPage), response.getMessage(), Snackbar.LENGTH_LONG)
                    .show();
        }else{
            Snackbar.make(findViewById(R.id.formPage), response.getMessage(), Snackbar.LENGTH_LONG)
                    .show();
        }
        progressDialog.dismiss();

    }

    @Override
    public void failureRequest(String message) {
        progressDialog.dismiss();
    }

    @Override
    public void onUnAuthorized() {
        Snackbar.make(findViewById(R.id.formPage), "Token expired. Mohon untuk login kembali.", Snackbar.LENGTH_LONG)
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        session.logout();
                        Intent loginPage = new Intent(RepsTambahPeserta.this, LoginActivity.class);
                        loginPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginPage);
                        finish();
                    }
                })
                .setActionTextColor(Color.WHITE)
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitBtn:
                presenter.doRequestInsert(
                        name.getText().toString(),
                        alamat.getText().toString(),
                        klotersValue.get(kloterSpinner.getSelectedItemPosition()).getValue().toString()
                );
                break;
        }
    }
}
