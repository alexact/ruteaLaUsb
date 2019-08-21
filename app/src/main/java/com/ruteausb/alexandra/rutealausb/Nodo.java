package com.ruteausb.alexandra.rutealausb;

/**
 *
 * @author sebastian
 */
public class Nodo implements Comparable<Nodo> {

    String id;
    String nombreFotos;
    double ejeX,ejeY;
    int distancia = Integer.MAX_VALUE;
    Nodo procedencia = null;


    Nodo(String x, String nombre ,String coordenadas,int d, Nodo p) {
        if(coordenadas!=null){
            String []temp = coordenadas.split("-");
            ejeX= Double.parseDouble(temp[0]);
            ejeY= Double.parseDouble(temp[1]);
            ejeY = ejeY*(-1);
        }
        id = x;
        nombreFotos = nombre;
        distancia = d;
        procedencia = p;
    }

    Nodo(String x ,String nombreFotos, String coordenadas) {
        this(x, nombreFotos, coordenadas, 0, null);
    }

    Nodo(String x){
        this(x,null,null,0,null);
    }

    Nodo(String x,int d, Nodo p){
        this(x,null,null,d,p);
    }

    public int compareTo(Nodo tmp) {
        return this.distancia - tmp.distancia;
    }

    public boolean equals(Object o) {
        Nodo tmp = (Nodo) o;
        if (tmp.id == this.id) {
            return true;
        }
        return false;
    }
}
