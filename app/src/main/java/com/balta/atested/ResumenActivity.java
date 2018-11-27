package com.balta.atested;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import static com.balta.atested.R.id.contador1;

public class ResumenActivity extends AppCompatActivity {

    private static final String LOGTAG = "ResumenActivity";
    private Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        myContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        MyLog.d(LOGTAG, Constantes.resume);
        super.onResume();

        TextView contador1 = findViewById(R.id.contador1);
        TextView contador2 = findViewById(R.id.contador2);
        TextView contador3 = findViewById(R.id.contador3);
        TextView contador4 = findViewById(R.id.contador4);

        contador1.setText(Repositorio.contarPregunta(myContext));
        contador2.setText(Repositorio.contarCategoria(myContext));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resumen, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_listado:
                Log.i("ActionBar", "Listado de Pregunta");
                Intent listadoIntent = new Intent(ResumenActivity.this, ListadoActivity.class);
                startActivity(listadoIntent);
                return true;
            case R.id.action_configuracion:
                Log.i("ActionBar", "Configuracion");
                ;
                return true;
            case R.id.action_acercade:
                Log.i("ActionBar", "Acerca De");
                Intent acercadeIntent = new Intent(ResumenActivity.this, AcercaActivity.class);
                startActivity(acercadeIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        MyLog.d(LOGTAG, Constantes.start);
        super.onStart();
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
