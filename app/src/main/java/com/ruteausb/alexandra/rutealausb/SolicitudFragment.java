package com.ruteausb.alexandra.rutealausb;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SolicitudFragment extends Fragment implements  interfasDeDatos {
    ArrayList<Solicitud>listaDatos;
    RecyclerView recyclerSolicitudes;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View vista=(View)inflater.inflate(R.layout.fragment_solicitudes,container,false);
        listaDatos=new ArrayList<>();
        Conexion c = new Conexion();
        try {
            String datos = c.execute(c.urlConsultarSolicitud()).get();
            llenarListaSolicitudes(datos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Con esto declarramos la lista del recycler en el fragment de solicitudes
        recyclerSolicitudes=vista.findViewById(R.id.recyclerId);
        recyclerSolicitudes.setLayoutManager(new LinearLayoutManager(getContext()));
        //se llenan los datos

        AdapterDatos adapter=new AdapterDatos(listaDatos);
        recyclerSolicitudes.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Selecciona "+listaDatos.get(recyclerSolicitudes.getChildAdapterPosition(view)).getId_nodo(),Toast.LENGTH_SHORT).show();
                Bundle args = new Bundle();
                args.putString("id_nodo",listaDatos.get(recyclerSolicitudes.getChildAdapterPosition(view)).getId_nodo());


                //Transaccion de fragmento
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ImagenFragment imagenFragment = new ImagenFragment();
                imagenFragment.setArguments(args);
                fm.beginTransaction().replace(R.id.fragment_container,
                        imagenFragment).commit();
            }
        });
        return vista;
    }

    private void llenarListaSolicitudes(String datos) {
        String[] temp = datos.split(";");

        for (int i=0;i<temp.length;i++) {
            String [] row  = temp[i].split(",");

            Solicitud solicitud = new Solicitud(row[0],row[1],row[2],row[3],row[4]);
            listaDatos.add(solicitud);
        }
    }
}