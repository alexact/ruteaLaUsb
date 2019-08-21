package com.ruteausb.alexandra.rutealausb;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public interface interfasDeDatos {

    ArrayList<Nodo> NODOS = Grafo.listaDeNodos;
    Grafo GRAFO = Grafo.grafito;
    String DOMINIO= "https://softneosas.com/sebas/rutea-la-usb/";
    int [][] MATRIZ_ADY = GRAFO.getGrafo();
    ArrayList<LugarInterno> LUGARES_INTERNOS = Grafo.listaDeLugaresInternos;

}
