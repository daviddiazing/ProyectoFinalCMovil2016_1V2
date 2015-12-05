package com.ebookfrenzy.proyfinalcmovil2016_1v1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by JUANITO on 05/12/2015.
 */

public class MainActivityImagesJuego extends Activity implements View.OnClickListener {

        private String strURL = "http://www.serverbpw.com/cm/2016-1/gallery.php?id=";
        private String nombreJuego;
        private String [] direccionesURL;
        private ImageButton btnAnterior;
        private ImageButton btnSiguiente;
        private int numeroImagenes;
        private TextView tvNumImagen;
        private int apuntadorImagen=1;
        ImageView galeria;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_imagenes_juego);
            strURL= strURL+getIntent().getExtras().getString("getIDJuego");
            nombreJuego = getIntent().getExtras().getString("getNombreJuego");
            rellenarImagenes();

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent irInicio = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(irInicio);
                    finish();
                }
            });

            ImageButton back = (ImageButton) findViewById(R.id.imgbtnBack);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent irListarJuegos = new Intent(getBaseContext(), ListarJuegosActivity.class);
                    startActivity(irListarJuegos);
                    finish();
                }
            });

        }

        private void inicializa (){
            TextView tvNombreJuego= (TextView) findViewById(R.id.tvNombreJuego);
            String aux = "";
            numeroImagenes = direccionesURL.length;
            tvNombreJuego.setText(nombreJuego);
            btnAnterior = (ImageButton) findViewById(R.id.btnAnterior);
            btnSiguiente = (ImageButton) findViewById(R.id.btnSiguiente);
            btnAnterior.setOnClickListener(this);
            btnSiguiente.setOnClickListener(this);
            tvNumImagen = (TextView) findViewById(R.id.tvNumImagen);
            tvNumImagen.setText( apuntadorImagen+" de "+ direccionesURL.length );
            galeria = (ImageView) findViewById(R.id.imgGaleriaJuego);
            Picasso.with(MainActivityImagesJuego.this)
                    .load(direccionesURL[apuntadorImagen-1])
                    .placeholder(R.drawable.backgroun_principal)
                    .error(R.mipmap.ic_launcher)
                    .into(galeria);
        }


        private void rellenarImagenes(){
            if(isOnline()){
                new DescargarListaJuegos(getBaseContext(),strURL).execute();
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

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnAnterior:
                    if(apuntadorImagen<=1){
                        apuntadorImagen = direccionesURL.length;
                    }else{
                        apuntadorImagen--;
                    }
                    tvNumImagen.setText(apuntadorImagen+" de "+ direccionesURL.length);
                    Picasso.with(MainActivityImagesJuego.this)
                            .load(direccionesURL[apuntadorImagen-1])
                            .placeholder(R.drawable.backgroun_principal)
                            .error(R.mipmap.ic_launcher)
                            .into(galeria);
                    break;

                case R.id.btnSiguiente:
                    if(apuntadorImagen>=direccionesURL.length){
                        apuntadorImagen=1;
                    }else{
                        apuntadorImagen++;
                    }
                    tvNumImagen.setText(apuntadorImagen+" de "+ direccionesURL.length);
                    Picasso.with(MainActivityImagesJuego.this)
                            .load(direccionesURL[apuntadorImagen-1])
                            .placeholder(R.drawable.backgroun_principal)
                            .error(R.mipmap.ic_launcher)
                            .into(galeria);
                    break;
            }

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
                pDialog = new ProgressDialog(MainActivityImagesJuego.this);
                pDialog.setMessage("Cargando Informacion");
                pDialog.setCancelable(true);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.show();
            }

            @Override
            protected Boolean doInBackground( String... params) {
                XMLParserGaleria parser = new XMLParserGaleria(strURL, getBaseContext());
                direccionesURL= parser.parse();
                return true;
            }


            @Override
            protected void onPostExecute(Boolean success) {
                if(success){
                    try {
                        inicializa();
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
