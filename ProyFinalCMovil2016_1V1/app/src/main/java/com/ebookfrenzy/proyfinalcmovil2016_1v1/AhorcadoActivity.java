package com.ebookfrenzy.proyfinalcmovil2016_1v1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import android.media.MediaPlayer;



public class AhorcadoActivity extends AppCompatActivity implements View.OnClickListener{

	static MediaPlayer mediaPlayer = null;
    static boolean pause=false;

    static TextView textView;
    static TextView textViewOp;
    static EditText editText;
    static ImageView imageView;
    static Button button;
	
	static TextView textViewCategoria;
	static String cadenaOportunidades=null;
		
	
    //Aquí declaro las variables globales importantes. Todos los métodos sólo van a modificar a estas
    static ArrayList<Integer> visibles = new ArrayList<Integer>();
    static String palabraSecreta = "Palabra Secreta";
    // char[] palabraArray = palabraSecreta.toCharArray();
	static char[] palabraArray = null;
    static int oportunidades=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ahorcado);
		
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irInicio = new Intent(getBaseContext(), MainActivity.class);
                startActivity(irInicio);
				reiniciarVariables();
                finish();
            }
        });
		
		String categoria = extras.getString("CATEGORIA");
		palabraSecreta = extras.getString("PALABRA");
		palabraArray = palabraSecreta.toCharArray();
				
		

		textViewCategoria = (TextView) findViewById(R.id.textViewCategoria);//
		textViewCategoria.setText("CATEGORIA: "+categoria);
		
        textView = (TextView) findViewById(R.id.textView);
        textViewOp= (TextView) findViewById(R.id.textViewOp);
        editText= (EditText)findViewById(R.id.editText);
        imageView=(ImageView)findViewById(R.id.imageView);
		button=(Button)findViewById(R.id.button);


        //Inicialización de cómo se verá al principio la pantalla
		revisarVariablesGlobales();
        textView.setText(muestraPalabra());
        button.setOnClickListener(this);
		
        		
		//musica
		if(mediaPlayer==null){
            mediaPlayer = MediaPlayer.create(this, R.raw.cancion);
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(10, 10);
        }
        if(!mediaPlayer.isPlaying())
        {
            if(!pause){
                mediaPlayer.start();
            }
        }
		

    }
	
	void reiniciarVariables()
	{
		oportunidades=5;	
		cadenaOportunidades=null;
		visibles = new ArrayList<Integer>();
		palabraSecreta = null;    
		palabraArray = null;		
		pause=false;				
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                //Obtiene la respuesta del usuario
                String letra = editText.getText().toString();
                if(letra.length()==0){
                    Toast.makeText(this, "Ingrese una letra", Toast.LENGTH_SHORT).show();
                    editText.setText("");
                }else
                if(letra.length()!=1){
                    Toast.makeText(this, "Ingrese sólo una letra", Toast.LENGTH_SHORT).show();
                    editText.setText("");
                }else{
                    //Si ingresó correctamente sólo 1 letra verifica si es parte de la palabra.
                    if(buscarLetra(letra.charAt(0))){
                        //Si es parte de la palabra muestra la nueva palabra, y hay 2 opciones:
                        textView.setText(muestraPalabra());
                        editText.setText("");
                        //Que ya ganó, es decir ya no hay más letras que descubrir
                        if(revisaGanador()){
							revisarVariablesGlobales();
                            Toast.makeText(this, "GANASTE!!!!", Toast.LENGTH_SHORT).show();
                            textViewOp.setText("Ganaste ;)");
							cadenaOportunidades="Ganaste ;)";
                            imageView.setImageResource(R.drawable.win);
                            editText.setEnabled(false);
                            button.setEnabled(false);
                        }else{
                            //O sino sigue jugando
							revisarVariablesGlobales();
                            Toast.makeText(this, "Bien!", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        //Pero si la letra ingresada no es parte de la palabra, entonce:
                        textView.setText(muestraPalabra());
                        //Si aun tiene oportunidades sigue jugando
                        if(revisarOportunidades()){
							revisarVariablesGlobales();
                            Toast.makeText(this, "Ups! Sigue jugando", Toast.LENGTH_SHORT).show();
                            editText.setText("");

                        }else{
                            //Si ya no le quedan oportundades pierde y ya no puede haacer más
							revisarVariablesGlobales();
                            Toast.makeText(this, "GAME OVER :c Loser", Toast.LENGTH_SHORT).show();

                        }

                    }

                }
                break;
        }
		
		//musica
		switch (v.getId()){
            case R.raw.cancion:
                if(mediaPlayer.isPlaying()){
                    pause=true;
                    mediaPlayer.pause();
                }else{
                    pause=false;
                    mediaPlayer.start();
                }
                break;
        }
		
    }


    //Regresa TRUE si encontró al menos una coincidencia de la letra en la palabra secreta.
    //Al mismo tiempo de que guarda la coincidencia para mostrarla en pantalla
    boolean buscarLetra(char c){
        boolean res = false;
        Character character = new Character(c);
        for(int i=0; i<palabraArray.length;i++){
            if(palabraArray[i]==character.toLowerCase(c) || palabraArray[i]==character.toUpperCase(c)){//Para minusculas y mayusculas
                visibles.add(i);
                res=true;
            }
        }
        return res;
    }

    //Regresa la palabra que se va a mostrar mostrar a partir de la palabra secreta original y cuales letras se han adivinado
    String muestraPalabra(){
        String palabraMuestra="";
        for(int i=0; i<palabraArray.length;i++){
            if(visibles.contains(i)){
                palabraMuestra+=palabraArray[i];
            }else{
                palabraMuestra+="*";
            }
        }
        return palabraMuestra;
    }

    //TRUE si ya ganaste el juego. False si aún no has ganado
    boolean revisaGanador(){
        boolean ganaste=true;
        for(int i=0; i<palabraArray.length;i++){
            if(!visibles.contains(i)){
                ganaste=false;
            }
        }
        return ganaste;
    }

    //Revisa las oportunidades que le quedan, y en cada caso cambia la imagen y el texto a mostrar
    //Regresa TRUE si sigue jugando, FALSE para que ya no pueda seguir jugando
    boolean revisarOportunidades(){
        boolean sigue = true;
        switch (oportunidades){
            case 5:
                oportunidades=4;
                break;
            case 4:
                oportunidades=3;
                break;
            case 3:
                oportunidades=2;
                break;
            case 2:
                oportunidades=1;
                break;
            case 1:
                oportunidades=0;
                break;
            case 0:
                oportunidades=-1;
                sigue=false;
                break;
        }
        return sigue;
    }

    //Segun las oportunidades que s etengan revisa las variables globales para cambiarlas
    void revisarVariablesGlobales(){
        switch (oportunidades){
            case 5:
                imageView.setImageResource(R.drawable.h1);
                textViewOp.setText("Tienes 5 oportunidades ");
				cadenaOportunidades="Tienes 5 oportunidades ";
                buscarLetra('_');
                buscarLetra(' ');
                break;
            case 4:
                imageView.setImageResource(R.drawable.h2);
                textViewOp.setText("Te quedan 4 oportunidades ");
				cadenaOportunidades="Te quedan 4 oportunidades ";
                break;
            case 3:
                imageView.setImageResource(R.drawable.h3);
                textViewOp.setText("Te quedan 3 oportunidades ");
				cadenaOportunidades="Te quedan 3 oportunidades ";
                break;
            case 2:
                imageView.setImageResource(R.drawable.h4);
                textViewOp.setText("Te queda 2 oportunidades ");
				cadenaOportunidades="Te queda 2 oportunidades ";
                break;
            case 1:
                imageView.setImageResource(R.drawable.h5);
                textViewOp.setText("Te queda 1 oportunidad");
				cadenaOportunidades="Te queda 1 oportunidad";
                break;
            case 0:
                imageView.setImageResource(R.drawable.h6);
                textViewOp.setText("Es tu última oportunidad");
				cadenaOportunidades="Es tu última oportunidad";
                break;
            case -1:
                imageView.setImageResource(R.drawable.h7);
                textViewOp.setText("¡Perdiste!");
				cadenaOportunidades="¡Perdiste!";
                editText.setEnabled(false);
                button.setEnabled(false);
                break;
        }
    }

	@Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.pause();
    }
	
	@Override
    protected void onPause(){
        super.onPause();
        pause=true;
        mediaPlayer.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        pause=false;
        mediaPlayer.start();
    }
	
	
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
		outState.putInt("oportunidades", oportunidades);
		outState.putString("cadenaOportunidades", cadenaOportunidades);
		
		CharSequence cadenaEn_textView = textView.getText();		
		outState.putCharSequence("cadenaEn_textView", cadenaEn_textView);
		
		outState.putCharArray("palabraArray", palabraArray);
		
		outState.putIntegerArrayList("visibles", visibles);
		
					

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        
		this.oportunidades = savedInstanceState.getInt("oportunidades");
		this.cadenaOportunidades = savedInstanceState.getString("cadenaOportunidades");
		textViewOp.setText(cadenaOportunidades);
		
		CharSequence cadenaEn_textView = savedInstanceState.getCharSequence("cadenaEn_textView");
		textView.setText(cadenaEn_textView);
		
		palabraArray = savedInstanceState.getCharArray("palabraArray");
				
		visibles = savedInstanceState.getIntegerArrayList("visibles");
    }


}






