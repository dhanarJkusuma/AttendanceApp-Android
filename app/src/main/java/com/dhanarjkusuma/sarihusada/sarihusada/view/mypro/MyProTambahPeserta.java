package com.dhanarjkusuma.sarihusada.sarihusada.view.mypro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dhanarjkusuma.sarihusada.sarihusada.R;
import com.dhanarjkusuma.sarihusada.sarihusada.model.Kloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.Location;
import com.dhanarjkusuma.sarihusada.sarihusada.model.MergeResponseLocationKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponsePost;
import com.dhanarjkusuma.sarihusada.sarihusada.model.SpinnerValue;
import com.dhanarjkusuma.sarihusada.sarihusada.presenter.FormInsPesertaPresenter;
import com.dhanarjkusuma.sarihusada.sarihusada.presenter.FormMyProInsPesertaPresenter;
import com.dhanarjkusuma.sarihusada.sarihusada.presenter.FormSHSearchByPresenter;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SpinnerSetter;
import com.dhanarjkusuma.sarihusada.sarihusada.view.LoginActivity;
import com.dhanarjkusuma.sarihusada.sarihusada.view.reps.RepsTambahPeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.FormInsPesertaInterface;

import java.util.ArrayList;
import java.util.List;

public class MyProTambahPeserta extends AppCompatActivity implements FormInsPesertaInterface, View.OnClickListener {

    private FormMyProInsPesertaPresenter presenter;
    private ProgressDialog progressDialog;
    private List<SpinnerValue> klotersValue;
    private List<SpinnerValue> locationsValue;
    private Button submitBtn;

    private EditText name;
    private EditText alamat;
    private Spinner kloterSpinner;
    private Spinner locationSpinner;

    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pro_tambah_peserta);
        setTitle("Tambah Peserta");
        kloterSpinner = (Spinner) findViewById(R.id.kloter);
        locationSpinner = (Spinner) findViewById(R.id.location);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        name = (EditText) findViewById(R.id.name);
        alamat = (EditText) findViewById(R.id.alamat);



        progressDialog = new ProgressDialog(this);
        session = new SessionManager(this);
        presenter = new FormMyProInsPesertaPresenter(this, this);
        presenter.doPrepareForm();


        submitBtn.setOnClickListener(this);
    }

    @Override
    public void doPrepareForm() {
        progressDialog.setMessage("Memuat data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void donePrepareForm(Object data) {
        MergeResponseLocationKloter responseLocationKloter = (MergeResponseLocationKloter) data;
        if(responseLocationKloter.getResponseGetKloter().getStatus()){
            populateSpinnerKloter(responseLocationKloter.getResponseGetKloter().getData());
        }

        if(responseLocationKloter.getResponseGetLocation().getStatus()){
            populateSpinnerLocation(responseLocationKloter.getResponseGetLocation().getData());
        }
        progressDialog.dismiss();
    }

    @Override
    public void failurePrepareForm(String message) {
        progressDialog.dismiss();
        Snackbar.make(findViewById(R.id.formPage), message, Snackbar.LENGTH_LONG)
                .setAction("TRY AGAIN", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        klotersValue = new ArrayList<SpinnerValue>();
                        locationsValue = new ArrayList<SpinnerValue>();
                        presenter.doPrepareForm();
                    }
                })
                .setActionTextColor(Color.WHITE)
                .show();
    }

    @Override
    public void doRequest() {
        progressDialog.setMessage("Mengirim data...");
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
                        Intent loginPage = new Intent(MyProTambahPeserta.this, LoginActivity.class);
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
                presenter.doInsertData(
                        name.getText().toString(),
                        alamat.getText().toString(),
                        klotersValue.get(kloterSpinner.getSelectedItemPosition()).getValue().toString(),
                        locationsValue.get(locationSpinner.getSelectedItemPosition()).getValue().toString()
                );
                break;
        }
    }

    private void populateSpinnerKloter(List<Kloter> kloters){
        klotersValue = new ArrayList<>();
        for(int i=0;i<kloters.size();i++){
            SpinnerValue spinnerValue = new SpinnerValue();
            spinnerValue.setKey(kloters.get(i).getName());
            spinnerValue.setValue(kloters.get(i).getId());
            klotersValue.add(spinnerValue);
        }
        SpinnerSetter.setSpinner(this, kloterSpinner, klotersValue, android.R.layout.simple_spinner_dropdown_item);
    }

    private void populateSpinnerLocation(List<Location> locations){
        locationsValue = new ArrayList<>();
        for(int i=0;i<locations.size();i++){
            SpinnerValue spinnerValue = new SpinnerValue();
            spinnerValue.setKey(locations.get(i).getName());
            spinnerValue.setValue(locations.get(i).getId());
            locationsValue.add(spinnerValue);
        }
        SpinnerSetter.setSpinner(this, locationSpinner, locationsValue, android.R.layout.simple_spinner_dropdown_item);
    }
}
