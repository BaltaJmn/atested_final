package com.balta.atested;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class LogoActivity extends AppCompatActivity {

    private static final String LOGTAG = "LogoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        //Temporizador de 5 segundos
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), ResumenActivity.class));
            }
        }, 1000);
    }

    @Override
    protected void onStart() {
        MyLog.d(LOGTAG, "Iniciando OnStart...");
        super.onStart();
    }

    @Override
    protected void onResume() {
        MyLog.d(LOGTAG, "Iniciando OnResume...");
        super.onResume();
    }

    @Override
    protected void onPause() {
        MyLog.d(LOGTAG, "Iniciando OnPause...");
        super.onPause();
    }

    @Override
    protected void onStop() {
        MyLog.d(LOGTAG, "Iniciando OnStop...");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        MyLog.d(LOGTAG, "Iniciando OnRestart...");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        MyLog.d(LOGTAG, "Iniciando OnDestroy...");
        super.onDestroy();
    }


}
