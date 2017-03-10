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
import android.widget.Spinner;

import com.dhanarjkusuma.sarihusada.sarihusada.R;
import com.dhanarjkusuma.sarihusada.sarihusada.model.Kloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.Location;
import com.dhanarjkusuma.sarihusada.sarihusada.model.MergeResponseLocationKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.SpinnerValue;
import com.dhanarjkusuma.sarihusada.sarihusada.presenter.FormMyProCategoryPresenter;
import com.dhanarjkusuma.sarihusada.sarihusada.presenter.FormSHSearchByPresenter;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SpinnerSetter;
import com.dhanarjkusuma.sarihusada.sarihusada.view.LoginActivity;
import com.dhanarjkusuma.sarihusada.sarihusada.view.sh.SHReadPeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.view.sh.SHSearchBy;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.DoRequestInterface;

import java.util.ArrayList;
import java.util.List;

public class MyProCategoryPeserta extends AppCompatActivity implements DoRequestInterface, View.OnClickListener {

    private ProgressDialog progressDialog;

    private List<SpinnerValue> klotersValue;
    private List<SpinnerValue> locationsValue;

    private Spinner kloterSpinner;
    private Spinner locationSpinner;
    private Button submitBtn;
    private FormMyProCategoryPresenter presenter;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pro_category_peserta);

        setTitle("Filter Peserta");

        kloterSpinner = (Spinner) findViewById(R.id.kloter);
        locationSpinner = (Spinner) findViewById(R.id.location);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        presenter = new FormMyProCategoryPresenter(this, this);
        progressDialog = new ProgressDialog(this);
        session = new SessionManager(this);
        submitBtn.setOnClickListener(this);
        presenter.doFillSpinner();
    }

    @Override
    public void doRequest() {
        progressDialog.setMessage("Memuat data..");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    public void doneRequest(Object data) {
        progressDialog.dismiss();
        MergeResponseLocationKloter response = (MergeResponseLocationKloter) data;
        if(response.getResponseGetKloter().getStatus()){
            populateSpinnerKloter(response.getResponseGetKloter().getData());
        }

        if(response.getResponseGetLocation().getStatus()){
            populateSpinnerLocation(response.getResponseGetLocation().getData());
        }
    }

    @Override
    public void failureRequest(String message) {
        progressDialog.dismiss();
        Snackbar.make(findViewById(R.id.mainPage), message, Snackbar.LENGTH_LONG)
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        presenter.doFillSpinner();
                    }
                })
                .setActionTextColor(Color.WHITE)
                .show();
    }

    @Override
    public void onUnAuthorized() {
        Snackbar.make(findViewById(R.id.mainPage), "Token expired. Mohon untuk login kembali.", Snackbar.LENGTH_LONG)
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        session.logout();
                        Intent loginPage = new Intent(MyProCategoryPeserta.this, LoginActivity.class);
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
                Intent readPage = new Intent(this, MyProLihatPeserta.class);
                readPage.putExtra("kloter", klotersValue.get(kloterSpinner.getSelectedItemPosition()).getValue().toString());
                readPage.putExtra("location", locationsValue.get(locationSpinner.getSelectedItemPosition()).getValue().toString());
                startActivity(readPage);
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
