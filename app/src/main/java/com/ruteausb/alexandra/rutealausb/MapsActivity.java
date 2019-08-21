package com.ruteausb.alexandra.rutealausb;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,  GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker markerDePrueba;
    private String lat, lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker and move the camera
        LatLng usbCali = new LatLng(3.345143, -76.544339);
        LatLng palmas = new LatLng(3.3452376124217236, -76.54547682404791);
        LatLng farallones = new LatLng(3.345660232490656, -76.54585182666779);
        LatLng cerezos = new LatLng(3.3450051651140336, -76.54480408852521);
        LatLng biblioteca = new LatLng(3.3448559698081763, -76.54393312360719);
        LatLng cedro = new LatLng(3.3447463457137334, -76.54322327763606);
        LatLng lago = new LatLng(3.344335284755747, -76.54318921379439);
        LatLng naranjos = new LatLng(3.3452778124560494, -76.54147281963799);
        LatLng higuerones = new LatLng(3.344557132740309, -76.54138055207352);
        LatLng parqTecno = new LatLng(3.343655191364098, -76.54196865856647);
        LatLng bristo = new LatLng(3.344319483018295, -76.5423830795279);
        LatLng qbano = new LatLng(3.3444644543842585, -76.54241014270428);
        LatLng entradaParq1 = new LatLng(3.3438666339434926, -76.54229014683801);
        LatLng entradaParq2 = new LatLng(3.3439894957265888, -76.54309853947171);
        LatLng entradavisitantes = new LatLng(3.342656375889226, -76.54409150487407);
        LatLng central = new LatLng(3.3457673379858672, -76.54439806938171);
        LatLng capilla = new LatLng(3.345717896581743, -76.54500565153444);
        mMap.addMarker(new MarkerOptions().position(usbCali).title("Marker in USB Cali"));
        mMap.addMarker(new MarkerOptions().position(palmas).title("Edif.Palmas").icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));
        mMap.addMarker(new MarkerOptions().position(farallones).title("Edif.Farallones").icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));
        mMap.addMarker(new MarkerOptions().position(cerezos).title("Edif.Cerezos").icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));
        mMap.addMarker(new MarkerOptions().position(cedro).title("Edif.Cedro").icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));
        mMap.addMarker(new MarkerOptions().position(lago).title("Edif.Lago").icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));
        mMap.addMarker(new MarkerOptions().position(biblioteca).title("Bibilioteca").icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));
        mMap.addMarker(new MarkerOptions().position(naranjos).title("Edif.Naranjos").icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));
        mMap.addMarker(new MarkerOptions().position(higuerones).title("Edif.Higuerones"));
        mMap.addMarker(new MarkerOptions().position(parqTecno).title("Parque Tecnológico").icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));
        mMap.addMarker(new MarkerOptions().position(bristo).title("Cafeteria Bristo").icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));
        mMap.addMarker(new MarkerOptions().position(qbano).title("Sandwich Qbano").icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));
        mMap.addMarker(new MarkerOptions().position(entradaParq1).title("Entrada parqueadero estudiantes").icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));
        mMap.addMarker(new MarkerOptions().position(entradaParq2).title("Entrada parqueadero a Universidad").icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));
        mMap.addMarker(new MarkerOptions().position(entradavisitantes).title("Entrada Visitantes y paradero MIO").icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));
        mMap.addMarker(new MarkerOptions().position(central).title("Cafeteria Central y Edfi.Horizontes").icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));
        mMap.addMarker(new MarkerOptions().position(capilla).title("Capilla ").icon(BitmapDescriptorFactory.fromResource(R.drawable.building)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(palmas,18));
        googleMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
    }
    @Override
    public boolean onMarkerClick(Marker marker) {

        this.lat= Double.toString(marker.getPosition().latitude);
        this.lng= Double.toString(marker.getPosition().longitude);
        Toast.makeText(this,lng+"  "+lat,Toast.LENGTH_SHORT).show();
        return false;
    }

}
