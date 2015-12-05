package com.ebookfrenzy.proyfinalcmovil2016_1v1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListarJuegosActivity extends AppCompatActivity {
    public ArrayList<DatosJuego> ArrayJuegos = new ArrayList<DatosJuego>();
    private AdapterJuegos adapterJuegos;
    private String URL = "http://www.serverbpw.com/cm/2016-1/list.php";

    ListView lstviListaJuegos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_juegos);
        rellenarJuegos();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irInicio = new Intent(getBaseContext(), MainActivity.class);
                startActivity(irInicio);
                finish();
            }
        });

    }

    private void inicializaListView(){
        lstviListaJuegos = (ListView) findViewById(R.id.lstviListaJuegos);
        adapterJuegos = new AdapterJuegos(this,ArrayJuegos);
        lstviListaJuegos.setAdapter(adapterJuegos);
        lstviListaJuegos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent irActivityImagenesJuego = new Intent(ListarJuegosActivity.this,MainActivityImagesJuego.class);
                irActivityImagenesJuego.putExtra("getIDJuego",ArrayJuegos.get(position).getID());
                irActivityImagenesJuego.putExtra("getNombreJuego",ArrayJuegos.get(position).getNombre());
                startActivity(irActivityImagenesJuego);
            }
        });
    }

    private void rellenarJuegos(){
        if(isOnline()){
            new DescargarListaJuegos(getBaseContext(),URL).execute();
        }else{
            Toast.makeText(this, "Desconectado", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isOnline() {//Para verificar que estamos conectados
        ConnectivityManager connect = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connect.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnected()){
            return true;
        }
        return false;
    }

    private class DescargarListaJuegos extends AsyncTask<String, Void, Boolean> {
        private String strURL;
        private Context context;
        ProgressDialog pDialog;

        public DescargarListaJuegos (Context context, String strURL) {
            this.context = context;
            this.strURL = strURL;
        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(ListarJuegosActivity.this);
            pDialog.setMessage("Cargando Informacion");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();

        }

        @Override
        protected Boolean doInBackground( String... params) {
            XMLParserJuegos parser = new XMLParserJuegos(strURL, getBaseContext());
            ArrayJuegos = parser.parse();
            return true;
        }


        @Override
        protected void onPostExecute(Boolean success) {
            if(success){
                try {
                    inicializaListView();
                    pDialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(context,"Error de Lectura",Toast.LENGTH_SHORT).show();
            }
        }
    }

}


