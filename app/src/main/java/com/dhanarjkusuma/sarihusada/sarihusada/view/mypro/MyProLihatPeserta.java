package com.dhanarjkusuma.sarihusada.sarihusada.view.mypro;

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
import com.dhanarjkusuma.sarihusada.sarihusada.model.ResponsePost;
import com.dhanarjkusuma.sarihusada.sarihusada.presenter.FormMyProDelPesertaPresenter;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.PesertaPaginator;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.view.LoginActivity;
import com.dhanarjkusuma.sarihusada.sarihusada.view.mypro.fragments.MyProFragmentUpdate;
import com.dhanarjkusuma.sarihusada.sarihusada.view.reps.RepsDetailPeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.view.sh.SHReadPeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.DoRequestInterface;
import com.dhanarjkusuma.sarihusada.sarihusada.view.ui_interface.ListDataOverflowInterface;
import com.srx.widget.PullToLoadView;

public class MyProLihatPeserta extends AppCompatActivity implements ListDataOverflowInterface, DoRequestInterface {

    private static final int REQUEST_UPDATE = 1;
    private SessionManager session;
    private PullToLoadView pullToLoadView;
    private String kloterCode;
    private String locationCode;
    private LinearLayout listPesertaPage;
    private PesertaPaginator paginator;
    private ProgressDialog progressDialog;
    private String deleteTmp;

    private FormMyProDelPesertaPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pro_lihat_peserta);

        setTitle("Lihat Data Peserta");

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            kloterCode = bundle.getString("kloter");
            locationCode = bundle.getString("location");
        }

        pullToLoadView = (PullToLoadView) findViewById(R.id.pullToLoadView);
        listPesertaPage = (LinearLayout) findViewById(R.id.listPesertaPage);
        paginator = new PesertaPaginator(this, pullToLoadView, this, kloterCode, locationCode, this);
        progressDialog = new ProgressDialog(this);
        pullToLoadView.getRecyclerView().setHasFixedSize(true);
        pullToLoadView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        pullToLoadView.getRecyclerView().setRecycledViewPool(new RecyclerView.RecycledViewPool());

        presenter = new FormMyProDelPesertaPresenter(this, this);

        session = new SessionManager(this);


        paginator.initializePaginator();
    }

    @Override
    public void update(Object object) {
        Peserta peserta = (Peserta) object;
        Intent updatePage = new Intent(this, MyProUbahPeserta.class);
        updatePage.putExtra("peserta", peserta);
        startActivityForResult(updatePage, REQUEST_UPDATE);
    }

    @Override
    public void delete(Object object) {
        final Peserta peserta = (Peserta) object;
        new AlertDialog.Builder(this, R.style.SariHusadaAlert)
                .setTitle("Dialog Konfirmasi")
                .setMessage("Hapus peserta dengan nama '" + peserta.getName() + "' ?" )
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTmp = peserta.getId();
                        presenter.doDeletePeserta(peserta.getId());
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
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
        Intent detailPage = new Intent(this, MyProDetailPeserta.class);
        detailPage.putExtra("peserta", peserta);
        startActivity(detailPage);
    }

    @Override
    public void doRequest() {
        progressDialog.setMessage("Menghapus data peserta...");
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                presenter.disposeRequest();
            }
        });
        progressDialog.show();
    }

    @Override
    public void doneRequest(Object data) {
        ResponsePost response = (ResponsePost) data;
        progressDialog.dismiss();
        if(response.getStatus()){
            deleteTmp = null;
            Snackbar
                    .make(listPesertaPage, response.getMessage(), Snackbar.LENGTH_SHORT)
                    .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            paginator.initializePaginator();
                        }
                    })
                    .setActionTextColor(Color.WHITE)
                    .show();

        }else{
            Snackbar
                    .make(listPesertaPage, response.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("COBA LAGI", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(deleteTmp != null){
                                presenter.doDeletePeserta(deleteTmp);
                            }
                        }
                    })
                    .setActionTextColor(Color.RED)
                    .show();
        }

    }

    @Override
    public void failureRequest(String message) {
        Snackbar
                .make(listPesertaPage, message, Snackbar.LENGTH_LONG)
                .setAction("COBA LAGI", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(deleteTmp != null){
                            presenter.doDeletePeserta(deleteTmp);
                        }
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
    }

    @Override
    public void onUnAuthorized() {
        Snackbar.make(findViewById(R.id.listPesertaPage), "Token expired. Mohon untuk login kembali.", Snackbar.LENGTH_LONG)
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        session.logout();
                        Intent loginPage = new Intent(MyProLihatPeserta.this, LoginActivity.class);
                        loginPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginPage);
                        finish();
                    }
                })
                .setActionTextColor(Color.WHITE)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPDATE) {
            if (resultCode == RESULT_OK) {
                boolean isRefresh = data.getBooleanExtra("isRefresh", false);
                if (isRefresh) {
                    paginator.initializePaginator();
                }
            }
        }
    }
}
