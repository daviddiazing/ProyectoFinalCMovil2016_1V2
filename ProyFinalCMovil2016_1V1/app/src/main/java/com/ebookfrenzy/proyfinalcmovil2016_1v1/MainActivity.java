package com.ebookfrenzy.proyfinalcmovil2016_1v1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.ebookfrenzy.proyfinalcmovil2016_1v1.BoundService.MyLocalBinder;


/**
 * Created by NORUEGO on 14/11/2015.
 */
public class MainActivity extends AppCompatActivity{

    private DrawerLayout drawerLayout;
    private NavigationView navView;

    BoundService myService;//gps
    boolean isBound = false;//gps

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent(this, BoundService.class);//levantar servicio gps
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);//levantar servicio gps

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navView = (NavigationView)findViewById(R.id.navview);
        drawerLayout.openDrawer(GravityCompat.START);

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        double currentTime[] = myService.getCurrentTime();//levantar servicio gps

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        boolean miTienda = false;

                        switch (menuItem.getItemId()) {
                            case R.id.menu_escanear_caja:
                                Intent irSimpleClientTrackingActivity = new Intent(getBaseContext(), SimpleClientTrackingActivity.class);
                                startActivity(irSimpleClientTrackingActivity);
                                //finish();
                                break;
                            case R.id.menu_listar_juegos:
                                Intent irListarJuegos = new Intent(getBaseContext(), ListarJuegosActivity.class);
                                startActivity(irListarJuegos);
                                //finish();
                                break;
                            case R.id.menu_ubicar_tienda:
                                Intent irTodasLasTiendasActivity = new Intent(getBaseContext(), TodasLasTiendasActivity.class);
                                startActivity(irTodasLasTiendasActivity);
                                //finish();
                                miTienda = true;
                                break;
                            case R.id.menu_mini_juego:
                                Intent irBuscarPalabraActivity = new Intent(getBaseContext(), BuscarPalabraActivity.class);
                                startActivity(irBuscarPalabraActivity);
                                //finish();
                                break;
                        }
                        drawerLayout.closeDrawers();

                        return true;
                    }
                });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }


    //conexión para el gps
    private ServiceConnection myConnection = new ServiceConnection()
    {

        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            MyLocalBinder binder = (MyLocalBinder) service;
            myService = binder.getService();
            isBound = true;
        }

        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };


}