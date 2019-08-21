package com.ruteausb.alexandra.rutealausb;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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

public class LugaresInterFragment extends Fragment implements interfasDeDatos {
    //Atributos de clase
    private Button devolver;
    private Button siguiente;
    private Button crearRuta;
    private ArrayList<String> linternos = new ArrayList<>();
    private ViewAnimator img;
    private String id_nodo;
    //_---------------

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
        //inicializción
        View vista = inflater.inflate(R.layout.fragment_lugaresinter, container, false);
        img = (ViewAnimator) vista.findViewById(R.id.imglinterno);
        devolver = (Button) vista.findViewById(R.id.liatrasFoto);
        siguiente = (Button) vista.findViewById(R.id.lisigFoto);
        crearRuta = (Button) vista.findViewById(R.id.generarR);
        //---------------------------------------------

        //Argumentos del Fragment anterior
        id_nodo = getArguments() != null ? getArguments().getString("id_nodo") : "21";
        //añadimos los internos correspondientes
        for (int i = 0; i < LUGARES_INTERNOS.size(); i++) {
            if (LUGARES_INTERNOS.get(i).getIdNodo().equals(id_nodo)) {
                linternos.add(LUGARES_INTERNOS.get(i).getRutaPlano());
            }
        }
        //------------------------------

        //traemos la imagen de cada interno correspondiente
        for (String item : linternos) {

            ImageView imageView = new ImageView(getActivity());

            try {
                imageView.setImageBitmap(new DescargarImagen(imageView).execute(DOMINIO + "planos-universidad/" + item.trim()).get());

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            img.addView(imageView);
        }
        //------------------------------------

        //En caso de que ese lugar no tenga fotos en el hosting
        if(linternos.isEmpty()){
            ImageView imageView = new ImageView(getActivity());
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.imagen_nodisponible);
            imageView.setImageBitmap(bitmap);
            Toast.makeText(getActivity(), "No existen lugares internos en la base de datos para esta solicitud",Toast.LENGTH_SHORT).show();

            img.addView(imageView);
        }
        //---------------------------------------


        //Construimos las animaciones
        Animation in = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
        img.setAnimation(in);
        img.setAnimation(out);

        //eventos botones
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.showNext();
            }
        });
        devolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.showPrevious();
            }
        });

        crearRuta.setOnClickListener(new View.OnClickListener() {
            Bundle args = new Bundle();
            String id_nodoActual;

            @Override
            public void onClick(View v) {
                Activity estaActividad = getActivity();

                // Acquire a reference to the system Location Manager
                LocationManager locationManager = (LocationManager) estaActividad.getSystemService(Context.LOCATION_SERVICE);

                // Define a listener that responds to location updates
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        // Called when a new location is found by the network location provider.
                        //nodo actual
                        id_nodoActual = idNodoActual(location.getLatitude(), location.getLongitude());

                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    public void onProviderEnabled(String provider) {
                    }

                    public void onProviderDisabled(String provider) {
                    }
                };


                int permissionCheck = ContextCompat.checkSelfPermission(estaActividad,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(estaActividad,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(estaActividad,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }

                }
                //Transaccion de fragmento
                if (id_nodoActual != null) {
                    Toast.makeText(getActivity(), id_nodoActual, Toast.LENGTH_SHORT).show();
                    double ejeX = NODOS.get(encontrarIndiceNODO(id_nodo)).ejeX;
                    double ejeY = NODOS.get(encontrarIndiceNODO(id_nodo)).ejeY;
                    id_nodo=idNodoDestino(ejeX+","+ejeY);
                    args.putString("idNodoOrigen", id_nodoActual);
                    args.putString("idNodoDestino",id_nodo);

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    ImgRutaFragment imgRutaFragment = new ImgRutaFragment();
                    imgRutaFragment.setArguments(args);
                    fm.beginTransaction().replace(R.id.fragment_container, imgRutaFragment).commit();
                }
            }
        });
        return vista;
    }


    //Descargador de imágenes
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
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                bitmap = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.imagen_nodisponible);
            }
            return bitmap;
        }

        @Override
        public void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
    //----------------------------------

    //Retorna el indice del nodo
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
    //---------------------------------

    //Trae el id mas cercano a las coordenadas
    public String idNodoActual(double lat, double longi) {

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
    //_-----------------------------

    //Trae el d mas cercano a las coordenadas
    public String idNodoDestino(String coordenada){
        String[] coord =coordenada.split(",");
        double x=  Double.parseDouble(coord[0]);
        double y = Double.parseDouble(coord[1]);
        double distMenor= Double.MAX_VALUE;
        int menor=0;
        for(int i=0;i<NODOS.size();i++){
            Nodo temp= NODOS.get(i);
            double radioNodo= Math.sqrt((x-temp.ejeX)*(x-temp.ejeX) + ((y-temp.ejeY)*(y-temp.ejeY)) );
            if(radioNodo < distMenor){
                distMenor=radioNodo;
                menor=i;
            }
        }
        return NODOS.get(menor).id;
    }
    //----------------------------------
}