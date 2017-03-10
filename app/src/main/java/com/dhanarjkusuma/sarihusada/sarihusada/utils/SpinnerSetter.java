package com.dhanarjkusuma.sarihusada.sarihusada.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.dhanarjkusuma.sarihusada.sarihusada.model.SpinnerValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhanar J Kusuma on 08/03/2017.
 */

public class SpinnerSetter
{
    public static void setSpinner(Context context, Spinner spinner, List<SpinnerValue> spinnerData, int layout)
    {
        List<SpinnerValue> values = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,layout);
        for (SpinnerValue data  : spinnerData )
        {
            adapter.add(data.getKey());
        }
        spinner.setAdapter(adapter);
    }
}
