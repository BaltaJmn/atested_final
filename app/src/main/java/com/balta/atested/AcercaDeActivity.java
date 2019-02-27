package com.balta.atested;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class AcercaDeActivity extends AppCompatActivity {

    private static final String LOGTAG = "AcercaActivity";
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button arriba = (Button) findViewById(R.id.buttonArriba);
        Button abajo = (Button) findViewById(R.id.buttonAbajo);
        Button izquierda = (Button) findViewById(R.id.buttonIzquierda);
        Button derecha = (Button) findViewById(R.id.buttonDerecha);

        arriba.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(AcercaDeActivity.this, R.anim.exit_up);
                iv = findViewById(R.id.imageView);
                iv.startAnimation(anim);
            }
        });

        abajo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(AcercaDeActivity.this, R.anim.exit_bottom);
                iv = findViewById(R.id.imageView);
                iv.startAnimation(anim);
            }
        });

        izquierda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(AcercaDeActivity.this, R.anim.exit_left);
                iv = findViewById(R.id.imageView);
                iv.startAnimation(anim);
            }
        });

        derecha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(AcercaDeActivity.this, R.anim.exit_right);
                iv = findViewById(R.id.imageView);
                iv.startAnimation(anim);
            }
        });



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
