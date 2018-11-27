package com.balta.atested;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class AcercaActivity extends AppCompatActivity {

    private static final String LOGTAG = "AcercaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    protected void onStart() {
        MyLog.d(LOGTAG, Constantes.start);
        super.onStart();
    }

    @Override
    protected void onResume() {
        MyLog.d(LOGTAG, Constantes.resume);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MyLog.d(LOGTAG, Constantes.pause);
        super.onPause();
    }

    @Override
    protected void onStop() {
        MyLog.d(LOGTAG, Constantes.stop);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        MyLog.d(LOGTAG, Constantes.restart);
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        MyLog.d(LOGTAG, Constantes.destroy);
        super.onDestroy();
    }



}
