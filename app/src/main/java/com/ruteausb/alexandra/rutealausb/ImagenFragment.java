package com.ruteausb.alexandra.rutealausb;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ImagenFragment extends Fragment implements interfasDeDatos {
    private ImageView img;
    private Conexion conexion=new Conexion();
    private Button lugarInterno;
    private TextView nombre;


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

        View vista=inflater.inflate(R.layout.fragment_imagen,container,false);
        img=(ImageView)vista.findViewById(R.id.Vanimators);
        nombre = (TextView)vista.findViewById(R.id.NombreLugar);
        try {
            String id =  getArguments() != null? getArguments().getString("id_nodo"): "2";
            int i = 0 ;
            boolean encontrado = false;
            while (!encontrado && i < NODOS.size()){
                if (NODOS.get(i).id.equals(id)){
                    encontrado = true;
                    i--;
                }
                i++ ;
            }

            ArrayList<String> ruta=new ArrayList<>();

            nombre.setText(NODOS.get(i).nombreFotos);
            descargarImageView(NODOS.get(i).nombreFotos+"-3.jpg");


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Comunicación hacia lugar interno, cambio de fragment y envío de parametros
        lugarInterno=(Button) vista.findViewById(R.id.lInternoFoto);
        lugarInterno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                String id = getArguments() != null? getArguments().getString("id_nodo"): "2";
                args.putString("id_nodo",id);
                //Transaccion de fragmento


                FragmentManager fm = getActivity().getSupportFragmentManager();
                LugaresInterFragment lugaresInter=new LugaresInterFragment();
                lugaresInter.setArguments(args);
                fm.beginTransaction().replace(R.id.fragment_container,lugaresInter).commit();
            }
        });

        return vista;
    }

    //Descarga la imagen y mostrarla
    public void descargarImageView(String nombre) throws IOException, JSONException {
        nombre=nombre.trim();
        Picasso.with(getActivity()).load(conexion.getDOMINIO()+"/img2/"+nombre).into(img);

    }
    //--------------------------------------------------



}