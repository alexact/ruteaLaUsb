package com.ruteausb.alexandra.rutealausb;

public class LugarInterno {
    private String id;
    private String idNodo;
    private String rutaPlano;

    public LugarInterno(String id,  String rutaPlano, String idNodo) {
        this.id = id;
        this.idNodo = idNodo;
        this.rutaPlano = rutaPlano;
    }

    public String getRutaPlano() {
        return rutaPlano.trim()+".jpg";
    }

    public String getIdNodo() {
        return idNodo;
    }

    public String getId() {
        return id;
    }
}
