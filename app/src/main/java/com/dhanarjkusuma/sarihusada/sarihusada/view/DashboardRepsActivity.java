package com.dhanarjkusuma.sarihusada.sarihusada.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dhanarjkusuma.sarihusada.sarihusada.R;
import com.dhanarjkusuma.sarihusada.sarihusada.utils.SessionManager;
import com.dhanarjkusuma.sarihusada.sarihusada.view.reps.RepsKloterPeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.view.reps.RepsLihatPeserta;
import com.dhanarjkusuma.sarihusada.sarihusada.view.reps.RepsTambahPeserta;

public class DashboardRepsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addBtn;
    private Button readBtn;
    private Button logoutBtn;
    private TextView welcome;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_reps);
        setTitle("Dashboard Reps");

        addBtn = (Button) findViewById(R.id.addBtn);
        readBtn = (Button) findViewById(R.id.readBtn);
        logoutBtn = (Button) findViewById(R.id.logoutBtn);
        welcome = (TextView) findViewById(R.id.welcome);
        addBtn.setOnClickListener(this);
        readBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);

        session = new SessionManager(this);
        welcome.setText(session.getUser().getUsername());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addBtn:
                Intent insertPage = new Intent(this, RepsTambahPeserta.class);
                startActivity(insertPage);
                break;
            case R.id.readBtn:
                Intent readPage = new Intent(this, RepsKloterPeserta.class);
                startActivity(readPage);
                break;
            case R.id.logoutBtn:
                new AlertDialog.Builder(this,R.style.SariHusadaAlert)
                        .setTitle("Dialog Konfirmasi")
                        .setMessage("Apakah anda yakin ingin logout ? ")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                session.logout();
                                Intent loginPage = new Intent(DashboardRepsActivity.this, LoginActivity.class);
                                loginPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(loginPage);
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                break;
        }
    }
}
