package com.ruteausb.alexandra.rutealausb;

public class Solicitud {
    String nombreSolicitud;
    String Descripcion;
    double x,y;
    int id;
    String id_nodo;

    public Solicitud() {
    }

    public Solicitud(String idx, String nombreSolicitud,String coordenadas, String descripcion, String id_nodo) {
        idx = idx.replace("null1","1");
        this.id = Integer.parseInt(idx);
        this.nombreSolicitud = nombreSolicitud;
        this.Descripcion = descripcion.replace("-->",",");
        this.id_nodo=id_nodo;
        if (coordenadas !=""|| coordenadas!= " ") {
            String[] temp = coordenadas.split("-");
            this.x = Double.parseDouble(temp[0]);
            this.y = Double.parseDouble(temp[1]);
        }

    }

    public String getId_nodo() {
        return id_nodo;
    }

    public void setId_nodo(String id_nodo) {
        this.id_nodo = id_nodo;
    }

    public String getNombreSolicitud() {
        return nombreSolicitud;
    }

    public void setNombreSolicitud(String nombreSolicitud) {
        this.nombreSolicitud = nombreSolicitud;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
