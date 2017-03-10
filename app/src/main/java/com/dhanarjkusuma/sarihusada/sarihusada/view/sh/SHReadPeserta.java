package com.dhanarjkusuma.sarihusada.sarihusada.view.sh;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.dhanarjkusuma.sarihusada.sarihusada.R;
import com.dhanarjkusuma.sarihusada.sarihusada.model.Peserta;
import com.dhanarjkusuma.sarihusada.sarihusada.presenter.ReadPesertaPresenter;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.PesertaPaginator;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.view.LoginActivity;
import com.dhanarjkusuma.sarihusada.sarihusada.view.reps.RepsDetailPeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.view.reps.RepsLihatPeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.DoRequestInterface;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.ListDataOverflowInterface;
import com.srx.widget.PullToLoadView;

public class SHReadPeserta extends AppCompatActivity implements DoRequestInterface, ListDataOverflowInterface {

    private SessionManager session;
    private PullToLoadView pullToLoadView;
    private String kloterCode;
    private String locationCode;
    private LinearLayout listPesertaPage;
    private PesertaPaginator paginator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shread_peserta);

        setTitle("Lihat Data Peserta");

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            kloterCode = bundle.getString("kloter");
            locationCode = bundle.getString("location");
        }

        pullToLoadView = (PullToLoadView) findViewById(R.id.pullToLoadView);
        listPesertaPage = (LinearLayout) findViewById(R.id.listPesertaPage);
        paginator = new PesertaPaginator(this, pullToLoadView, this, kloterCode, locationCode, this);

        pullToLoadView.getRecyclerView().setHasFixedSize(true);
        pullToLoadView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        pullToLoadView.getRecyclerView().setRecycledViewPool(new RecyclerView.RecycledViewPool());


        session = new SessionManager(this);


        paginator.initializePaginator();
    }

    @Override
    public void doRequest() {

    }

    @Override
    public void doneRequest(Object data) {

    }

    @Override
    public void failureRequest(String message) {

    }

    @Override
    public void onUnAuthorized() {
        Snackbar.make(findViewById(R.id.listPesertaPage), "Token expired. Mohon untuk login kembali.", Snackbar.LENGTH_LONG)
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        session.logout();
                        Intent loginPage = new Intent(SHReadPeserta.this, LoginActivity.class);
                        loginPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginPage);
                        finish();
                    }
                })
                .setActionTextColor(Color.WHITE)
                .show();
    }

    @Override
    public void update(Object object) {
        new AlertDialog.Builder(this, R.style.SariHusadaAlert)
                .setTitle("Pesan")
                .setMessage("Tidak mendapatkan hak akses untuk mengubah data." )
                .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void delete(Object object) {
        new AlertDialog.Builder(this, R.style.SariHusadaAlert)
                .setTitle("Pesan")
                .setMessage("Tidak mendapatkan hak akses untuk menghapus data." )
                .setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void detail(Object object) {
        Peserta peserta = (Peserta) object;
        Intent detailPage = new Intent(this, RepsDetailPeserta.class);
        detailPage.putExtra("peserta", peserta);
        startActivity(detailPage);
    }
}
