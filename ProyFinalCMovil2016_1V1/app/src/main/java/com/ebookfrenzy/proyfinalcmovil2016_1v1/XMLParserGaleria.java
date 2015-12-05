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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by JUANITO on 05/12/2015.
 */
public class XMLParserGaleria {
    private URL strURL;
    private Context context;
    private String inputStream;
    private String cadenaURL;

    public XMLParserGaleria(String strURL, Context context) {
        this.context = context;
        try {
            this.strURL = new URL(strURL);
        }catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


    public String[] parse() {
        //Instanciamos la fábrica para DOM
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        String [] direccionImagenes;
        try{
            //Creamos un nuevo parser DOM
            DocumentBuilder builder = factory.newDocumentBuilder();

            //Realizamos lalectura completa del XML
            Document dom = builder.parse(this.getInputStream());

            //Nos posicionamos en el nodo principal del árbol (<rss>)
            Element root = dom.getDocumentElement();

            //Localizamos todos los elementos <array>
            NodeList items = root.getElementsByTagName("array");

            direccionImagenes = new String[items.getLength()];
            //Recorremos la lista de imagenes

            for (int i=0; i<items.getLength(); i++) {

                //Obtenemos la imagen actual
                Node item = items.item(i);

                //Obtenemos la lista de datos de la noticia actual
                NodeList datosImagen = item.getChildNodes();

                //Procesamos cada URL de la imagen
                for (int j=0; j<datosImagen.getLength(); j++)
                {
                    Node dato = datosImagen.item(j);
                    String etiqueta = dato.getNodeName();

                    if (etiqueta.equals("string"))
                    {
                        String texto = obtenerTexto(dato);
                        direccionImagenes[i] = texto;
                    }
                }

            }

            return  direccionImagenes;

        }catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }


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
