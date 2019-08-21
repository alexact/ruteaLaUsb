package com.ruteausb.alexandra.rutealausb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewAnimator;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ImgLugarFragment extends Fragment implements interfasDeDatos {

    //Atributos de clase
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;

    private Button devolver;
    private Button siguiente;
    private TextView nombreLugar;
    private ArrayList<String> ruta = new ArrayList<>();
    private ImageView img;

    private ImageButton botonFlecha;
    private GestureDetector mGestureDetector;
    private String idNodo;
    private int idxNodo;
    private int dir;
    private View vista;
    //-----------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_imglugar, container, false);
        botonFlecha = (ImageButton) vista.findViewById(R.id.imgbtnrigth);
        nombreLugar = (TextView) vista.findViewById(R.id.RNombreLugarF);
        img = (ImageView) vista.findViewById(R.id.imglugar);
        final int width = this.getResources().getDisplayMetrics().widthPixels;
        final int height = this.getResources().getDisplayMetrics().heightPixels;
        idNodo = getArguments() != null ? getArguments().getString("id_nodo") : "4";
        idxNodo = encontrarIndiceNODO(idNodo);
        dir = 0;
        try {
            acomodarFragment(idxNodo);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("ERROR 625: ", "Error al ejecutar multiples Task");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("ERROR 15: ", "Error el task fue interrumpido");
        }

        vista.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getX() > width * 0.5) {//girar a la derecha
                        if (dir == 3) {
                            dir = 0;
                        } else {
                            dir++;
                        }
                        try {
                            img.setImageBitmap(new DescargarImagen(img).execute(DOMINIO + "img2/" + NODOS.get(idxNodo).nombreFotos.trim() + "-" + dir + ".jpg").get());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else {
                        if (dir == 0) {//girar a la izquierda
                            dir = 3;
                        } else {
                            dir--;
                        }
                        try {
                            img.setImageBitmap(new DescargarImagen(img).execute(DOMINIO + "img2/" + NODOS.get(idxNodo).nombreFotos.trim() + "-" + dir + ".jpg").get());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }


                return true;
            }
        });
        botonFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nodo actual = NODOS.get(idxNodo);
                Nodo temp = actual;
                int i = 0;
                //Variables de uso para  avanzar en las coordenadas
                double nuevoX = actual.ejeX;
                double nuevoY = actual.ejeY;
                while (actual.id == idNodo && i < 50) {
                    switch (dir) {
                        case 0://norte
                            nuevoX += 0.0000078;
                            nuevoY -= 0.0000003;
                            idNodo = idNodoMascerano(nuevoX, nuevoY);
                            idxNodo = encontrarIndiceNODO(idNodo);
                            temp = NODOS.get(idxNodo);
                            break;

                        case 1://este
                            nuevoY += 0.0000078;
                            nuevoX -= 0.0000003;
                            idNodo = idNodoMascerano(nuevoX, nuevoY);
                            idxNodo = encontrarIndiceNODO(idNodo);
                            temp = NODOS.get(idxNodo);
                            break;
                        case 2://sur
                            nuevoX -= 0.0000078;
                            nuevoY += 0.0000003;
                            idNodo = idNodoMascerano(nuevoX, nuevoY);
                            idxNodo = encontrarIndiceNODO(idNodo);
                            temp = NODOS.get(idxNodo);
                            break;
                        case 3://oeste
                            nuevoY -= 0.0000078;
                            nuevoX += 0.0000003;
                            idNodo = idNodoMascerano(nuevoX, nuevoY);
                            idxNodo = encontrarIndiceNODO(idNodo);
                            temp = NODOS.get(idxNodo);
                            break;
                    }
                    i++;
                }

                try {
                    acomodarFragment(idxNodo);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        return vista;
    }


    //Dada una coordenada, devuelve el id del nodo mas carcano
    public String idNodoMascerano(double lat, double longi) {
        double x = lat;
        double y = longi;
        double distMenor = Double.MAX_VALUE;
        int menor = 0;
        for (int i = 0; i < NODOS.size(); i++) {
            Nodo temp = NODOS.get(i);
            double radioNodo = Math.sqrt((x - temp.ejeX) * (x - temp.ejeX) + ((y - temp.ejeY) * (y - temp.ejeY)));
            if (radioNodo < distMenor) {
                distMenor = radioNodo;
                menor = i;
            }
        }
        return NODOS.get(menor).id;
    }
    //----------------------------------------

    //Devuelve el indice de un nodo
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
    //---------------------


    private void acomodarFragment(int idxNodo) throws ExecutionException, InterruptedException {
        img.setImageBitmap(new DescargarImagen(img).execute(DOMINIO + "img2/" + NODOS.get(idxNodo).nombreFotos.trim() + "-" + dir + ".jpg").get());
        nombreLugar.setText(NODOS.get(idxNodo).nombreFotos);
    }

    private class DescargarImagen extends AsyncTask<String, Integer, Bitmap> {
        //Atributos de clase
        ImageView imageView;
        ProgressBar progressBar;
        //-------------------------------

        //Constructor
        public DescargarImagen(ImageView img) {
            this.imageView = img;
        }
        //--------------------------------------------------------

        //Barra de progreso de la tarea
        public void setProgressBar(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        //Tarea en segundo plano
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
                bitmap = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.imagen_nodisponible);
            }
            return bitmap;
        }
        //--------------------------------

        //tarea finalizada
        @Override
        public void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
        //----------------------------

        //Actualizando barra de progreso
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (this.progressBar != null) {
                progressBar.setProgress(values[0]);
            }
        }
        //------------------------------------------
    }

}