package com.ruteausb.alexandra.rutealausb;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RutaFragment extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, interfasDeDatos {

    View vista;
    TextView tvOrigen;
    TextView tvDestino;
    Button btnRuta;
    String lat;
    String lng;
    Button btnRutaFotos;

    private GoogleMap mMap;
    Bundle args = new Bundle();
    private ArrayList<String> ruta = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_ruta, container, false);
        btnRuta = (Button) vista.findViewById(R.id.btnSeleccionar2);
        tvDestino = (TextView) vista.findViewById(R.id.tvDestino);
        btnRutaFotos=(Button) vista.findViewById(R.id.btnSeleccionar3);
        tvOrigen = (TextView) vista.findViewById(R.id.pOrigen);
        Activity estaActividad = getActivity();

        tvOrigen.setText("Buscando Ubicación...");
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) estaActividad.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                tvOrigen.setText("" + location.getLatitude() + "," + location.getLongitude());
                args.putString("idNodoOrigen", idNodoActual());
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
        btnRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvOrigen.getText() != "Buscando Ubicación..." && tvDestino != null) {
                    mostrarLineas(idNodoActual(),idNodoDestino());
                   } else {
                    Toast.makeText(getActivity(), "Esperando las coordenadas...", Toast.LENGTH_SHORT).show();
                }
            }
        });

btnRutaFotos.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (tvOrigen.getText() != "Buscando Ubicación..." && tvDestino != null) {
            args.putString("idNodoDestino", idNodoDestino());
            //Transaccion de fragmento

            Toast.makeText(getActivity(), "Calculando ruta...", Toast.LENGTH_SHORT).show();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            ImgRutaFragment imgRutaFragment = new ImgRutaFragment();
            imgRutaFragment.setArguments(args);
            fm.beginTransaction().replace(R.id.fragment_container, imgRutaFragment).commit();

        } else {
            Toast.makeText(getActivity(), "Esperando las coordenadas...", Toast.LENGTH_SHORT).show();
        }
    }
});
        return vista;
    }

    public String idNodoActual() {
        String[] coord = tvOrigen.getText().toString().split(",");
        double x = Double.parseDouble(coord[0]);
        double y = Double.parseDouble(coord[1]);
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

    public String idNodoDestino() {
        String[] coord = tvDestino.getText().toString().split(",");
        double x = Double.parseDouble(coord[0]);
        double y = Double.parseDouble(coord[1]);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapsFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapRecorrido);
        mapsFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        // Add a marker and move the camera
        //mMap.addPolyline();
        //  mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng usbCali = new LatLng(3.345143, -76.544339);
        LatLng palmas = new LatLng(3.3452376124217236, -76.54547682404791);
        LatLng cerezos = new LatLng(3.3450051651140336, -76.54480408852521);
        LatLng biblioteca = new LatLng(3.3448559698081763, -76.54393312360719);
        LatLng cedro = new LatLng(3.3447463457137334, -76.54322327763606);
        LatLng lago = new LatLng(3.344335284755747, -76.54318921379439);
        LatLng parqTecno = new LatLng(3.343655191364098, -76.54196865856647);
        LatLng bristo = new LatLng(3.344319483018295, -76.5423830795279);
        LatLng qbano = new LatLng(3.3444644543842585, -76.54241014270428);
        LatLng entradaParq1 = new LatLng(3.3438666339434926, -76.54229014683801);
        LatLng entradaParq2 = new LatLng(3.3439894957265888, -76.54309853947171);
        LatLng entradavisitantes = new LatLng(3.342656375889226, -76.54409150487407);
        LatLng capilla = new LatLng(3.345717896581743, -76.54500565153444);
        LatLng cerezosFrente = new LatLng(3.344915, -76.545037);
        LatLng caminoFrenteCerezos = new LatLng(3.3455169, -76.544433);
        LatLng tenisMesa = new LatLng(3.3461237804356045, -76.54595048752816);
        LatLng ajedrez = new LatLng(3.34505511, -76.54425002);
        LatLng akiosco = new LatLng(3.34542208, -76.54380408);
        LatLng arbolFarall = new LatLng(3.34581876, -76.54534584);
        LatLng biblioLago = new LatLng(3.344755, -76.543689);
        LatLng bristoS = new LatLng(3.344216, -76.542501);
        LatLng caminoBasurero = new LatLng(3.346188, -76.545085);
        LatLng caminobiblioLago = new LatLng(3.34476646, -76.54360916);
        LatLng camCedroQbano = new LatLng(3.344492, -76.542447);
        LatLng inicioNaranjos = new LatLng(3.344811, -76.542549);
        LatLng centralAtras = new LatLng(3.346088, -76.545037);
        LatLng caminoSalidaBiblio = new LatLng(3.344988, -76.543837);
        LatLng capillaInterna = new LatLng(3.345944, -76.545014);
        LatLng central = new LatLng(3.345765, -76.544592);
        LatLng centralDer = new LatLng(3.345774, -76.544484);
        LatLng centralFinal = new LatLng(3.345901, -76.544603);
        LatLng cerezosPrincipal = new LatLng(3.344915, -76.544415);
        LatLng cedroOeste = new LatLng(3.344773, -76.543349);
        LatLng lagooeste = new LatLng(3.344265, -76.54335);
        LatLng entrBiblio = new LatLng(3.344798, -76.543765);
        LatLng sanBuenaventura = new LatLng(3.344892, -76.543751);
        LatLng caminobiblioCedro = new LatLng(3.345040, -76.5433847);
        LatLng entrCapilla = new LatLng(3.345645, -76.545003);
        LatLng entrCentral = new LatLng(3.345627, -76.544507);
        LatLng entrParque = new LatLng(3.343713, -76.541959);
        LatLng farallones = new LatLng(3.345680, -76.545423);
        LatLng finalBiblio = new LatLng(3.345079, -76.543850);
        LatLng kiosco = new LatLng(3.345635, -76.544035);
        LatLng finalcentralDer =new LatLng(3.345913,-76.544429);
        LatLng naranjos =new LatLng(3.345212,-76.541570);
        LatLng higuerones =new LatLng(3.344412,-76.541568);
        LatLng EntradaPrincipal =new LatLng(3.342745,-76.543994);
        LatLng cafGreenP =new LatLng(3.346274,-76.546003);
        LatLng mesasLago =new LatLng(3.344302,-76.542766);


        mMap.addMarker(new MarkerOptions().position(usbCali).title("Marker in USB Cali"));
        mMap.addMarker(new MarkerOptions().position(palmas).title("Edif.Palmas").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(cerezos).title("Edif.Cerezos").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(cerezosFrente).title("Edif.CerezosFrente").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(cedro).title("Edif.Cedro").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(caminoFrenteCerezos).title("Escultura cerezos").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(lago).title("Edif.Lago").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(biblioteca).title("Bibilioteca").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(parqTecno).title("Parque Tecnológico").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(bristo).title("Cafeteria Bristo").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(qbano).title("Sandwich Qbano").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(entradaParq1).title("Entrada parqueadero estudiantes").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(entradaParq2).title("Entrada parqueadero a Universidad").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(entradavisitantes).title("Entrada Visitantes y paradero MIO").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(capilla).title("Capilla ").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(tenisMesa).title("Tenis de Mesa").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(ajedrez).title("Ajedrez").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(akiosco).title("Camino Kiosco").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(arbolFarall).title("Camping").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(biblioLago).title("Biblioteca lateral").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(bristoS).title("Bristo").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(caminoBasurero).title("Camino Central").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(caminobiblioLago).title("Biblioteca por Lago").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(camCedroQbano).title("Cedro por Sandwich Qbano").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(inicioNaranjos).title("Camino Naranjos").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(centralAtras).title("Central Posterior").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(caminoSalidaBiblio).title("Salida Biblioteca").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(capillaInterna).title("Adentro Capilla").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(central).title("Cafeteria Central").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(centralDer).title("Horizontes Derecha").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(centralFinal).title("Final Cafeteria").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(cerezosPrincipal).title("Frente Cerezos").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(cedroOeste).title("Cedro Oeste").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(lagooeste).title("Lago Oeste").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(entrBiblio).title("Entrada Biblioteca").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(sanBuenaventura).title("Auditorios Biblioteca").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(caminobiblioCedro).title("Biblioteca por Cedro").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(entrCapilla).title("Entrada de Capilla").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(entrCentral).title("Frente Central").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(entrParque).title("Parque Tecnologico").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(farallones).title("Farallones").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(finalBiblio).title("Salida Biblioteca").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(kiosco).title("Kiosco").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(finalcentralDer).title("Mirador Central 1").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(naranjos).title("Edif.Naranjos").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(higuerones).title("Edif. Higuerones").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(EntradaPrincipal).title("Entrada Principal").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(cafGreenP).title("Restaurante Green point").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));
        mMap.addMarker(new MarkerOptions().position(mesasLago).title("Mesas Lago").icon(BitmapDescriptorFactory.fromResource(R.drawable.punto)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usbCali, 18));
        googleMap.setOnMarkerClickListener(this);
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
    private void mostrarLineas(String idNodoActual,String idNodoDestino){
        ruta = GRAFO.encontrarRutaMinimaDijkstra(idNodoActual().trim(), idNodoDestino().trim());

        //ArrayList<String> coordRuta=new ArrayList<>();
        PolylineOptions lineas = new PolylineOptions();

        for (String id:ruta) {
            String[] coord = GRAFO.getCoordenadas(encontrarIndiceNODO(id)).split(",");
            System.out.println("Nodod destino"+Double.parseDouble(coord[0])+","+ Double.parseDouble(coord[1]));
            lineas.add(new LatLng(Double.parseDouble(coord[0]), Double.parseDouble(coord[1])));
        }


       /* //Dibujo con Lineas
        System.out.println("Nodod destino"+encontrarIndiceNODO(idNodoDestino()));
        System.out.println(Double.parseDouble(coordenadas2[0])+","+ Double.parseDouble(coordenadas2[1]));

        PolylineOptions lineas = new PolylineOptions()
                .add(new LatLng(3.345143, -76.544339))
                 .add(new LatLng(Double.parseDouble(coordenadas1[0]), Double.parseDouble(coordenadas1[1])));
           .add(new LatLng(45.0, 5.0));
                .add(new LatLng(34.5, 5.0))
                .add(new LatLng(34.5, -12.0))
                .add(new LatLng(45.0, -12.0));*/

        lineas.width(8);
        lineas.color(Color.RED);

        mMap.addPolyline(lineas);
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        this.lat = Double.toString(marker.getPosition().latitude);
        this.lng = Double.toString(marker.getPosition().longitude);
        tvDestino.setText(lat + "," + lng);
        return false;
    }

}
