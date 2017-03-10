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

import com.dhanarjkusuma.sarihusada.sarihusada.R;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.SpinnerValue;
import com.dhanarjkusuma.sarihusada.sarihusada.presenter.FormInsPesertaPresenter;
import com.dhanarjkusuma.sarihusada.sarihusada.presenter.PesertaKloterPresenter;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SpinnerSetter;
import com.dhanarjkusuma.sarihusada.sarihusada.view.LoginActivity;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.DoRequestInterface;

import java.util.ArrayList;
import java.util.List;

public class RepsKloterPeserta extends AppCompatActivity implements DoRequestInterface, View.OnClickListener {

    private ProgressDialog progressDialog;
    private List<SpinnerValue> klotersValue;
    private Spinner kloterSpinner;
    private Button submitBtn;
    private SessionManager session;
    private TextView reps;

    private PesertaKloterPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reps_kloter_peserta);
        kloterSpinner = (Spinner) findViewById(R.id.kloter);
        klotersValue = new ArrayList<>();

        submitBtn = (Button) findViewById(R.id.submitBtn);
        reps = (TextView) findViewById(R.id.reps);


        progressDialog = new ProgressDialog(this);
        session = new SessionManager(this);
        reps.setText(session.getUser().getReps().getName());

        presenter = new PesertaKloterPresenter(this, this);

        presenter.doGetKloter();

        submitBtn.setOnClickListener(this);


    }

    @Override
    public void doRequest() {
        progressDialog.setMessage("Loading data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void doneRequest(Object data) {
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
    public void failureRequest(String message) {
        progressDialog.dismiss();
        Snackbar.make(findViewById(R.id.mainPage), message, Snackbar.LENGTH_LONG)
                .setAction("TRY AGAIN", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        klotersValue = new ArrayList<SpinnerValue>();
                        presenter.doGetKloter();
                    }
                })
                .setActionTextColor(Color.WHITE)
                .show();
    }

    @Override
    public void onUnAuthorized() {
        Snackbar.make(findViewById(R.id.formPage), "Token expired. Mohon untuk login kembali.", Snackbar.LENGTH_SHORT)
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        session.logout();
                        Intent loginPage = new Intent(RepsKloterPeserta.this, LoginActivity.class);
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
                Intent readPage = new Intent(this, RepsLihatPeserta.class);
                readPage.putExtra("kloter", klotersValue.get(kloterSpinner.getSelectedItemPosition()).getValue().toString());
                startActivity(readPage);
                break;
        }
    }
}
