package com.ruteausb.alexandra.rutealausb;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

public class RecorrerFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, interfasDeDatos {
    //Atributos de clase
    View vista;
    Button btnRuta;
    String lat, lng;
    private GoogleMap mMap;
    //------------------------
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista=inflater.inflate(R.layout.fragment_recorrido,container,false);
        btnRuta=(Button) vista.findViewById(R.id.btnSeleccionar);
        btnRuta.setEnabled(false);//evento click
                btnRuta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),"Redireccionando",Toast.LENGTH_SHORT).show();
                        //Transaccion de fragmento
                        Bundle args=new Bundle();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        ImgLugarFragment imgLugarFragment=new ImgLugarFragment();
                        args.putString("id_nodo",idNodoDestino());
                        imgLugarFragment.setArguments(args);
                        fm.beginTransaction().replace(R.id.fragment_container,
                                imgLugarFragment).commit();

                    }
                });

        return vista;
    }
    //retorna el id del nodo destino
    public String idNodoDestino(){
        String[] coord =(lat+","+lng).split(",");
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
    //-----------------
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment  mapsFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapRecorrido);
        mapsFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        // Add a marker and move the camera
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
        LatLng camCedroQbano = new LatLng(3.344492, -76542447);
        LatLng inicioNaranjos = new LatLng(3.344811, -76.542549);
        LatLng centralAtras = new LatLng(3.346088, -76.545037);
        LatLng caminoSalidaBiblio = new LatLng(3.344988, -76.543837);
        LatLng capillaInterna = new LatLng(3.345944, -76.545014);
        LatLng central = new LatLng(3.345765, -76544592);
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

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usbCali,18));
        googleMap.setOnMarkerClickListener( this);
    }

    //evento marker click
    @Override
    public boolean onMarkerClick(Marker marker) {

        this.lat= Double.toString(marker.getPosition().latitude);
        this.lng= Double.toString(marker.getPosition().longitude);
        btnRuta.setEnabled(true);
        return false;
    }
    //-------------------------------------
}
