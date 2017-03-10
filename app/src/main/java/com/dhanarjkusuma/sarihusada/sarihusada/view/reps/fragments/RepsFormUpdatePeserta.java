package com.dhanarjkusuma.sarihusada.sarihusada.view.reps.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.dhanarjkusuma.sarihusada.sarihusada.R;
import com.dhanarjkusuma.sarihusada.sarihusada.model.Peserta;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponseGetKloter;
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponsePost;
import com.dhanarjkusuma.sarihusada.sarihusada.model.SpinnerValue;
import com.dhanarjkusuma.sarihusada.sarihusada.presenter.FormUpdPesertaPresenter;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SpinnerSetter;
import com.dhanarjkusuma.sarihusada.sarihusada.view.LoginActivity;
import com.dhanarjkusuma.sarihusada.sarihusada.view.reps.RepsKloterPeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.view.reps.RepsTambahPeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.FormUpdPesertaInterface;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class RepsFormUpdatePeserta extends Fragment implements FormUpdPesertaInterface, View.OnClickListener {

    private ProgressDialog progressDialog;
    private Peserta peserta;
    private EditText name;
    private EditText alamat;
    private SessionManager session;
    private Spinner kloterSpinner;
    private Button submitBtn;
    private List<SpinnerValue> klotersValue;
    private LinearLayout updateForm;
    private FormUpdPesertaPresenter presenter;
    private int selectedKloter;

    public RepsFormUpdatePeserta() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            peserta = bundle.getParcelable("peserta");
        }
        presenter = new FormUpdPesertaPresenter(this, getContext());
        progressDialog = new ProgressDialog(getContext());
        session = new SessionManager(getContext());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reps_form_update_peserta, container, false);
        updateForm = (LinearLayout) v.findViewById(R.id.updateForm);
        name = (EditText) v.findViewById(R.id.name);
        alamat = (EditText) v.findViewById(R.id.alamat);
        kloterSpinner = (Spinner) v.findViewById(R.id.kloter);
        submitBtn = (Button) v.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);
        presenter.doPrepareForm();

        return v;
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
        if(responseGetKloter.getStatus()){
            klotersValue = new ArrayList<>();
            for(int i=0;i<responseGetKloter.getData().size();i++){
                SpinnerValue spinnerValue = new SpinnerValue();
                spinnerValue.setKey(responseGetKloter.getData().get(i).getName());
                spinnerValue.setValue(responseGetKloter.getData().get(i).getId());
                if(responseGetKloter.getData().get(i).getId().equals(peserta.getKloter().getId())){
                    selectedKloter = i;
                }
                klotersValue.add(spinnerValue);
            }
            SpinnerSetter.setSpinner(getContext(), kloterSpinner, klotersValue, android.R.layout.simple_spinner_dropdown_item);
            populateValue();
        }
    }

    private void populateValue(){
        name.setText(peserta.getName());
        alamat.setText(peserta.getAlamat());
        kloterSpinner.setSelection(selectedKloter);
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
                .make(updateForm,message, Snackbar.LENGTH_LONG)
                .setAction("COBA LAGI", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.doUpdatePeserta(
                                peserta,
                                name.getText().toString(),
                                alamat.getText().toString(),
                                klotersValue.get(kloterSpinner.getSelectedItemPosition()).getValue().toString()
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
                        klotersValue.get(kloterSpinner.getSelectedItemPosition()).getValue().toString()
                );
                break;
        }
    }
}
