package com.dhanarjkusuma.sarihusada.sarihusada.view.sh.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dhanarjkusuma.sarihusada.sarihusada.R;
import com.dhanarjkusuma.sarihusada.sarihusada.model.PesertaRevisi;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryUpdatePesertaSH extends Fragment {

    private EditText name;
    private EditText alamat;
    private EditText kloter;

    private PesertaRevisi revisi;
    public HistoryUpdatePesertaSH() {
        // Required empty public constructor
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            revisi = bundle.getParcelable("revisi");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history_update_peserta_sh, container, false);
        name = (EditText) v.findViewById(R.id.name);
        alamat = (EditText) v.findViewById(R.id.alamat);
        kloter = (EditText) v.findViewById(R.id.kloter);
        populateValue();

        return v;
    }

    private void populateValue(){
        name.setText(revisi.getName());
        alamat.setText(revisi.getAlamat());
        kloter.setText(revisi.getKloter().getName());
    }

}
