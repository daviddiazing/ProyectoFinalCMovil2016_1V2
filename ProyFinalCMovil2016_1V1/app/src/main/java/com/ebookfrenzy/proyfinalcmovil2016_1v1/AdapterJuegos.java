package com.ebookfrenzy.proyfinalcmovil2016_1v1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by JUANITO on 22/11/2015.
 */
public class AdapterJuegos extends ArrayAdapter<Object> {
    Context context;
    protected ArrayList<DatosJuego> juegos;

    public AdapterJuegos(Context context, ArrayList<DatosJuego> juegos) {
        super(context, R.layout.item_juego);
        this.context = context;
        this.juegos = juegos;

    }


    @Override
    public int getCount() {
        return juegos.size();
    }

    public static class PlaceHolder {
        TextView tvNombre;
        TextView tvID;
        TextView tvDescripcion;
        ImageView ibtnJuego;

        public static PlaceHolder generate (View convertView){
            PlaceHolder placeHolder = new PlaceHolder();
            placeHolder.tvNombre = (TextView) convertView.findViewById(R.id.tvNombre);
            placeHolder.tvID = (TextView) convertView.findViewById(R.id.tvID);
            placeHolder.tvDescripcion = (TextView) convertView.findViewById(R.id.tvDescripcion);
            placeHolder.ibtnJuego = (ImageView) convertView.findViewById(R.id.ibtnJuego);
            return placeHolder;
        }
    }

    public View getView(int position, View convertView , ViewGroup parent) {
        PlaceHolder placeHolder = null;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_juego,null);
            placeHolder = placeHolder.generate(convertView);
            convertView.setTag(placeHolder);
        }else {
            placeHolder = (PlaceHolder) convertView.getTag();
        }
        placeHolder.tvNombre.setText(juegos.get(position).getNombre());
        placeHolder.tvID.setText(juegos.get(position).getID());
        placeHolder.tvDescripcion.setText(juegos.get(position).getDescripcion());
        //placeHolder.ibtnJuego.setImageBitmap(juegos.get(position).getImgJuego());
        Picasso.with(context).load(juegos.get(position).getImagen()).placeholder(R.drawable.backgroun_principal).error(R.mipmap.ic_launcher).into(placeHolder.ibtnJuego);
        return convertView;
    }

}