package com.dhanarjkusuma.sarihusada.sarihusada.view.mypro.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.dhanarjkusuma.sarihusada.sarihusada.R;
import com.dhanarjkusuma.sarihusada.sarihusada.model.Kloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.Location;
import com.dhanarjkusuma.sarihusada.sarihusada.model.MergeResponseLocationKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.Peserta;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponsePost;
import com.dhanarjkusuma.sarihusada.sarihusada.model.SpinnerValue;
import com.dhanarjkusuma.sarihusada.sarihusada.presenter.FormMyProUpdPesertaPresenter;
import com.dhanarjkusuma.sarihusada.sarihusada.presenter.FormUpdPesertaPresenter;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SpinnerSetter;
import com.dhanarjkusuma.sarihusada.sarihusada.view.LoginActivity;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.FormUpdPesertaInterface;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProFragmentUpdate extends Fragment implements FormUpdPesertaInterface, View.OnClickListener {

    private ProgressDialog progressDialog;

    private Peserta peserta;
    private EditText name;
    private EditText alamat;
    private Spinner kloterSpinner;
    private Spinner locationSpinner;

    private SessionManager session;
    private Button submitBtn;
    private List<SpinnerValue> klotersValue;
    private List<SpinnerValue> locationsValue;
    private LinearLayout updateForm;
    private FormMyProUpdPesertaPresenter presenter;
    private int selectedKloter;
    private int selectedLocation;

    public MyProFragmentUpdate() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            peserta = bundle.getParcelable("peserta");
        }
        presenter = new FormMyProUpdPesertaPresenter(this, getContext());
        progressDialog = new ProgressDialog(getContext());
        session = new SessionManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_my_pro_fragment_update, container, false);
        updateForm = (LinearLayout) v.findViewById(R.id.updateForm);
        name = (EditText) v.findViewById(R.id.name);
        alamat = (EditText) v.findViewById(R.id.alamat);
        kloterSpinner = (Spinner) v.findViewById(R.id.kloter);
        locationSpinner = (Spinner) v.findViewById(R.id.location);
        submitBtn = (Button) v.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);
        presenter.doPrepareForm();

        return v;
    }

    private void populateSpinnerKloter(List<Kloter> kloters){
        klotersValue = new ArrayList<>();
        for(int i=0;i<kloters.size();i++){
            SpinnerValue spinnerValue = new SpinnerValue();
            spinnerValue.setKey(kloters.get(i).getName());
            spinnerValue.setValue(kloters.get(i).getId());
            if(kloters.get(i).getId().equals(peserta.getKloter().getId())){
                selectedKloter = i;
            }
            klotersValue.add(spinnerValue);
        }
        SpinnerSetter.setSpinner(getContext(), kloterSpinner, klotersValue, android.R.layout.simple_spinner_dropdown_item);
    }

    private void populateSpinnerLocation(List<Location> locations){
        locationsValue = new ArrayList<>();
        for(int i=0;i<locations.size();i++){
            SpinnerValue spinnerValue = new SpinnerValue();
            spinnerValue.setKey(locations.get(i).getName());
            spinnerValue.setValue(locations.get(i).getId());
            if(locations.get(i).getId().equals(peserta.getLocation().getId())){
                selectedLocation = i;
            }
            locationsValue.add(spinnerValue);
        }
        SpinnerSetter.setSpinner(getContext(), locationSpinner, locationsValue, android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public void doPrepareForm() {
        progressDialog.setMessage("Loading data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void donePrepareForm(Object data) {
        MergeResponseLocationKloter response = (MergeResponseLocationKloter) data;

        if(response.getResponseGetKloter().getStatus()){
            populateSpinnerKloter(response.getResponseGetKloter().getData());
        }

        if(response.getResponseGetLocation().getStatus()){
            populateSpinnerLocation(response.getResponseGetLocation().getData());
        }
        populateValue();
        progressDialog.dismiss();
    }

    private void populateValue(){
        name.setText(peserta.getName());
        alamat.setText(peserta.getAlamat());
        kloterSpinner.setSelection(selectedKloter);
        locationSpinner.setSelection(selectedLocation);
    }

    @Override
    public void failurePrepareForm(String message) {
        progressDialog.dismiss();
        Snackbar.make(updateForm, message, Snackbar.LENGTH_LONG)
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
        progressDialog.setMessage("Mengupdate data...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    public void doneRequest(Object data) {
        ResponsePost response = (ResponsePost) data;
        progressDialog.dismiss();
        if(response.getStatus()){
            Snackbar
                    .make(updateForm, response.getMessage(), Snackbar.LENGTH_SHORT)
                    .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            Intent refresh = new Intent();
                            refresh.putExtra("isRefresh", true);
                            getActivity().setResult(RESULT_OK, refresh);
                            getActivity().onBackPressed();
                        }
                    })
                    .show();
        }else{
            failureRequest(response.getMessage());
        }
    }

    @Override
    public void failureRequest(String message) {
        Snackbar
                .make(updateForm,message, Snackbar.LENGTH_SHORT)
                .setAction("COBA LAGI", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.doUpdatePeserta(
                                peserta,
                                name.getText().toString(),
                                alamat.getText().toString(),
                                klotersValue.get(kloterSpinner.getSelectedItemPosition()).getValue().toString(),
                                locationsValue.get(locationSpinner.getSelectedItemPosition()).getValue().toString()
                        );
                    }
                })
                .setActionTextColor(Color.WHITE)
                .show();
    }

    @Override
    public void onUnAuthorized() {
        Snackbar.make(updateForm, "Token expired. Mohon untuk login kembali.", Snackbar.LENGTH_LONG)
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        session.logout();
                        Intent loginPage = new Intent(getActivity(), LoginActivity.class);
                        loginPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginPage);
                        getActivity().finish();
                    }
                })
                .setActionTextColor(Color.WHITE)
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitBtn:
                presenter.doUpdatePeserta(
                        peserta,
                        name.getText().toString(),
                        alamat.getText().toString(),
                        klotersValue.get(kloterSpinner.getSelectedItemPosition()).getValue().toString(),
                        locationsValue.get(locationSpinner.getSelectedItemPosition()).getValue().toString()
                );
                break;
        }
    }
}

