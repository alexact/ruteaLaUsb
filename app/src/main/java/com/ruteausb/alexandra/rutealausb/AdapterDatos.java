package com.ruteausb.alexandra.rutealausb;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> implements View.OnClickListener{

    ArrayList<Solicitud> listDatos;
    private View.OnClickListener listener;
    public AdapterDatos(ArrayList<Solicitud> listDatos){
        this.listDatos = listDatos;
    }
    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null,false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.nombreSolicitud.setText(listDatos.get(position).getNombreSolicitud());
        holder.descripcion.setText(listDatos.get(position).getDescripcion());
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView nombreSolicitud, descripcion;
        ImageView foto;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombreSolicitud=(TextView) itemView.findViewById(R.id.idNombreSol);
            descripcion=(TextView) itemView.findViewById(R.id.idDescripcion);
        }


    }
}
