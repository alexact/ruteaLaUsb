package com.ruteausb.alexandra.rutealausb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ImgRutaFragment extends Fragment implements interfasDeDatos {

    //Atributos de clase
    private Button devolver;
    private Button siguiente;
    private TextView nombreLugar;
    private ArrayList<String> ruta = new ArrayList<>();
    private ViewAnimator img;
    private int idxRuta;
    private String nombreActual;
    //-----------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //inicializacion de vistas y variables
        View vista = inflater.inflate(R.layout.fragment_imgruta, container, false);
        img = (ViewAnimator) vista.findViewById(R.id.imgruta);
        nombreLugar = (TextView) vista.findViewById(R.id.RNombreLugar);
        idxRuta = 0;
        nombreActual = "";
        //Argumentos del fragment anterior
        String idOrigen = getArguments() != null ? getArguments().getString("idNodoOrigen") : "2";
        String idDestino = getArguments() != null ? getArguments().getString("idNodoDestino") : "4";
        final int width = this.getResources().getDisplayMetrics().widthPixels;
        final int height = this.getResources().getDisplayMetrics().heightPixels;

        //calcular ruta
        ruta = GRAFO.encontrarRutaMinimaDijkstra(idOrigen.trim(), idDestino.trim());
        //-----------------------------------------------------------------------

        devolver = (Button) vista.findViewById(R.id.iniciar);
        siguiente = (Button) vista.findViewById(R.id.sigFotoR);

        //traemos la primera imagen
        actualizarImagen(ruta.get(0));
        //Creamos los animation
        Animation in = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
        //adicionamos los animation
        img.setAnimation(in);
        img.setAnimation(out);
        //Fin Inicio

        //eventos touch
        vista.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getX() > width * 0.5) {

                        img.showNext();

                    } else {

                        img.showPrevious();

                    }
                }

                return true;
            }
        });
        //----------------------------------------

        //Eventos click botones
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idxRuta++;
                if (idxRuta >= ruta.size()) {
                    idxRuta = 0;
                }
                actualizarImagen(ruta.get(idxRuta));
            }
        });
        devolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idxRuta--;
                if (idxRuta < 0) {
                    idxRuta = ruta.size() - 1;
                }
                actualizarImagen(ruta.get(idxRuta));
            }
        });
        //--------------------------


        return vista;
    }

    //Método que retorna el indice de un Nodo
    public int encontrarIndiceNODO(String idDestino) {
        int i = 0;
        boolean encontrado = false;
        while (i < NODOS.size() && !encontrado) {
            if (NODOS.get(i).id.equals(idDestino)) {
                encontrado = true;
                i--;
            }
            i++;
        }
        return i;
    }
    //-----------------------------------

    //ACtualiza el ViewAnimator con las nuevas imagenes
    public void actualizarImagen(String id) {
        int j = 0;
        img.removeAllViews();//Removemos los Views actuales
        j = encontrarIndiceNODO(id);
        nombreActual = NODOS.get(j).nombreFotos.trim();//Nombre actual de la foto
        for (int i = 0; i < 4; i++) {

            ImageView imageView = new ImageView(getActivity());

            try {
                //Descargamos la imagen
                imageView.setImageBitmap(new DescargarImagen(imageView).execute(DOMINIO + "img2/" + nombreActual + "-" + i + ".jpg").get());

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Añadimos al ViewAnimator
            img.addView(imageView);
        }

    }
    //-------------------------


    //Clase encargada de traer el Bitmap de la imagen correspondiente
    private class DescargarImagen extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DescargarImagen(ImageView img) {
            this.imageView = img;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlOfimg = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream is = new URL(urlOfimg).openStream();
                bitmap = BitmapFactory.decodeStream(is);

                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.imagen_nodisponible);
            }
            return bitmap;
        }

        @Override
        public void onPostExecute(Bitmap result) {
            nombreLugar.setText(nombreActual);
            imageView.setImageBitmap(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }
    //-------------------------------------------------------
}