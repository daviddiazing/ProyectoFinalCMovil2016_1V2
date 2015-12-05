package com.ebookfrenzy.proyfinalcmovil2016_1v1;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by JUANITO on 05/12/2015.
 */
public class XMLParserJuegos {
    private URL strURL;
    private Context context;
    private String inputStream;

    public XMLParserJuegos(String strURL, Context context) {
        this.context = context;
        try {
            this.strURL = new URL(strURL);
        }catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<DatosJuego> parse() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ArrayList<DatosJuego> ArrayJuegos = new ArrayList<DatosJuego>();
        try
        {
            //Creamos un nuevo parser DOM
            DocumentBuilder builder = factory.newDocumentBuilder();

            //Realizamos lalectura completa del XML
            Document dom = builder.parse(this.getInputStream());

            //Nos posicionamos en el nodo principal del árbol (<rss>)
            Element root = dom.getDocumentElement();

            //Localizamos todos los elementos <item>
            NodeList items = root.getElementsByTagName("array");

            //Recorremos la lista de juegos
            for (int i=0; i<items.getLength(); i++)
            {
                //________________________________________________________________________________________________________
                DatosJuego juego = new DatosJuego();

                //Obtenemos el juego
                Node item = items.item(i);

                //Obtenemos la lista de datos de la noticia actual
                NodeList datosJuego = item.getChildNodes();

                //Procesamos cada dato de la noticia
                for (int j=0; j<datosJuego.getLength(); j++)
                {
                    Node dato = datosJuego.item(j);
                    String etiqueta = dato.getNodeName();
                    switch (j){
                        case 1:
                            if (etiqueta.equals("string"))
                            {
                                String texto = obtenerTexto(dato);
                                juego.setID(texto);
                            }
                            break;
                        case 3:
                            if (etiqueta.equals("string"))
                            {
                                String texto = obtenerTexto(dato);
                                juego.setNombre(texto);
                            }
                            break;
                        case 5:
                            if (etiqueta.equals("string"))
                            {
                                String texto = obtenerTexto(dato);
                                juego.setDescripcion(texto);
                            }
                            break;
                        case 7:
                            if (etiqueta.equals("string"))
                            {
                                String texto = obtenerTexto(dato);
                                juego.setImagen(texto);
                            }
                            break;
                    }
                }
                ArrayJuegos.add(juego);
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        return ArrayJuegos;

    }

    private String obtenerTexto(Node dato)
    {
        StringBuilder texto = new StringBuilder();
        NodeList fragmentos = dato.getChildNodes();

        for (int k=0;k<fragmentos.getLength();k++)
        {
            texto.append(fragmentos.item(k).getNodeValue());
        }

        return texto.toString();
    }
    private InputStream getInputStream()
    {
        try
        {
            return strURL.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
