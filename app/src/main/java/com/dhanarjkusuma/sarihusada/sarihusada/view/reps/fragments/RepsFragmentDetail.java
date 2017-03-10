package com.dhanarjkusuma.sarihusada.sarihusada.view.reps.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dhanarjkusuma.sarihusada.sarihusada.R;
import com.dhanarjkusuma.sarihusada.sarihusada.model.Peserta;
import com.dhanarjkusuma.sarihusada.sarihusada.model.PesertaRevisi;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepsFragmentDetail extends Fragment {

    private EditText name;
    private EditText alamat;
    private EditText kloter;

    private Peserta peserta;

    public RepsFragmentDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            peserta = bundle.getParcelable("peserta");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reps_fragment_detail, container, false);
        name = (EditText) v.findViewById(R.id.name);
        alamat = (EditText) v.findViewById(R.id.alamat);
        kloter = (EditText) v.findViewById(R.id.kloter);
        populateValue();
        return v;
    }


    private void populateValue(){
        name.setText(peserta.getName());
        alamat.setText(peserta.getAlamat());
        kloter.setText(peserta.getKloter().getName());
    }

}
