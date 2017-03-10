package com.dhanarjkusuma.sarihusada.sarihusada.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;


import com.dhanarjkusuma.sarihusada.sarihusada.R;
import com.dhanarjkusuma.sarihusada.sarihusada.model.Peserta;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.ListDataOverflowInterface;

/**
 * Created by Dhanar J Kusuma on 09/03/2017.
 */

public class AdapterPesertaOverflow implements View.OnClickListener {
    private Context context;
    private Peserta peserta;
    private ListDataOverflowInterface listDataOverflowInterface;

    public AdapterPesertaOverflow(Context context, Peserta peserta, ListDataOverflowInterface listDataOverflowInterface) {
        this.context = context;
        this.peserta = peserta;
        this.listDataOverflowInterface = listDataOverflowInterface;
    }

    @Override
    public void onClick(View v) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.inflate(R.menu.peserta_popup);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit:
                        listDataOverflowInterface.update(peserta);
                        break;
                    case R.id.delete:
                        listDataOverflowInterface.delete(peserta);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
