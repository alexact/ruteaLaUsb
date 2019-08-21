package com.ruteausb.alexandra.rutealausb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

/**
 * @author sebastian
 */
public class Grafo {

    String[] nodos;  // Letras de identificación de nodo
    int[][] grafo;  // Matriz de distancias entre nodos
    String rutaMasCorta;                           // distancia más corta
    int longitudMasCorta = Integer.MAX_VALUE;   // ruta más corta
    public static ArrayList<Nodo> listaDeNodos;
    List<Nodo> listos = null;                        // nodos revisados Dijkstra
    static Grafo grafito;
    public static ArrayList<LugarInterno> listaDeLugaresInternos;


    // construye el grafo con la serie de identificadores de nodo en una cadena
    public Grafo() throws ExecutionException, InterruptedException {
        Conexion c = new Conexion();//creamos una conexion
        String cadena = c.execute(c.urlConsultarNodos()).get();//ejecutamos la consulta
        String[] row = cadena.split(";");//separamos los registros
        nodos = new String[row.length];
        listaDeNodos = new ArrayList<>();
        listaDeLugaresInternos = new ArrayList<>();
        //asignamos a cada nodo su id
        for (int i = 0; i < row.length; i++) {
            String[] campos = row[i].split(",");
            nodos[i] = campos[0];
            Nodo temp = new Nodo(campos[0], campos[1], campos[2]);
            listaDeNodos.add(temp);
        }
        //------------------------

        grafo = new int[nodos.length][nodos.length];//declaramos el grafo
        construirCaminos();
        listaDeLugaresInternos = getListaDeLugaresInternos();
        grafito = this;
    }

    public int[][] getGrafo() {
        return grafo;
    }

    public ArrayList<LugarInterno> getListaDeLugaresInternos() throws ExecutionException, InterruptedException {
        ArrayList<LugarInterno> lista = new ArrayList<>();
        Conexion c = new Conexion();//creamos una conexion
        String cadena = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            cadena = c.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, c.urlConsultarLugaresInternos()).get();//ejecutamos la consulta
        } else {

            cadena = c.execute(c.urlConsultarLugaresInternos()).get();
        }
        String[] row = cadena.split(";");//separamos los registros
        //agregamos cada registro
        for (int i = 0; i < row.length; i++) {
            String[] campos = row[i].split(",");
            LugarInterno lugarInterno = new LugarInterno(campos[0], campos[1], campos[2]);
            lista.add(lugarInterno);
        }
        //------------------------
        return lista;
    }


    //retorna la lista de nodos
    public ArrayList<Nodo> getListaDeNodos() {
        return this.listaDeNodos;
    }
    //----------------------------------------

    //Método que agrega todas las rutas registradas en la base de datos
    public void construirCaminos() throws ExecutionException, InterruptedException {
        Conexion c = new Conexion();//creamos una conexion
        String cadena = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            cadena = c.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, c.urlConsultarAristas()).get();//ejecutamos la consulta
        } else {

            cadena = c.execute(c.urlConsultarSolicitud()).get();
        }
        String[] row = cadena.split(";");//separamos los registros
        //agregamos cada ruta registrada
        for (int i = 0; i < row.length; i++) {
            String[] campos = row[i].split(",");
            int d = Integer.parseInt(campos[2]);
            agregarRuta(campos[0], campos[1], d);
        }
        //------------------------

    }
    //---------------------------------------------

    // asigna el tamaño de la arista entre dos nodos
    public void agregarRuta(String origen, String destino, int distancia) {
        int n1 = posicionNodo(origen);
        int n2 = posicionNodo(destino);
        grafo[n1][n2] = distancia;
        grafo[n2][n1] = distancia;
    }

    // retorna la posición en el arreglo de un nodo específico
    public int posicionNodo(String nodo) {

        for (int i = 0; i < nodos.length; i++) {
            if (nodos[i].equals(nodo)) {
                return i;
            }
        }
        return -1;
    }

    // encuentra la ruta más corta desde un nodo origen a un nodo destino
    public ArrayList<String> encontrarRutaMinimaDijkstra(String inicio, String fin) {
        // calcula la ruta más corta del inicio a los demás
        encontrarRutaMinimaDijkstra(inicio);
        // recupera el nodo final de la lista de terminados
        Nodo tmp = new Nodo(fin);
        if (!listos.contains(tmp)) {
            Log.d("Error:", "Nodo no alcanzable :(");
            return null;
        }
        tmp = listos.get(listos.indexOf(tmp));
        int distancia = tmp.distancia;
        // crea una pila para almacenar la ruta desde el nodo final al origen
        Stack<Nodo> pila = new Stack<>();
        while (tmp != null) {
            pila.add(tmp);
            tmp = tmp.procedencia;
        }
        ArrayList<String> ruta = new ArrayList<>();
        // recorre la pila para armar la ruta en el orden correcto
        while (!pila.isEmpty()) {
            ruta.add((pila.pop().id));
        }
        //ruta.add("\ndistancia: "+ distancia);
        return ruta;
    }

    //Retorna los indices adyacentes
    public ArrayList<Integer> getAdyacentes(int indiceNodo) {
        int[][] mAdy = getGrafo();
        ArrayList<Integer> adya = new ArrayList<>();
        for (int i = 0; i < listaDeNodos.size(); i++) {
            if (mAdy[indiceNodo][i] != 0) {
                adya.add(i);
            }
        }
        return adya;
    }
    //------------------------------------
    //------------------------------------



    public String getCoordenadas(int indiceNodo) {
        Nodo temp = listaDeNodos.get(indiceNodo);
        return temp.ejeX+","+temp.ejeY;
    }

    // encuentra la ruta más corta desde el nodo inicial a todos los demás
    public void encontrarRutaMinimaDijkstra(String inicio) {
        Queue<Nodo> cola = new PriorityQueue<>(); // cola de prioridad
        Nodo ni = new Nodo(inicio);          // nodo inicial

        listos = new LinkedList<>();// lista de nodos ya revisados
        cola.add(ni);                   // Agregar nodo inicial a la cola de prioridad
        while (!cola.isEmpty()) {        // mientras que la cola no esta vacia
            Nodo tmp = cola.poll();     // saca el primer elemento
            listos.add(tmp);            // lo manda a la lista de terminados
            int p = posicionNodo(tmp.id);
            for (int j = 0; j < grafo[p].length; j++) {  // revisa los nodos hijos del nodo tmp
                if (grafo[p][j] == 0) {
                    continue;        // si no hay conexión no lo evalua
                }
                if (estaTerminado(j)) {
                    continue;      // si ya fue agregado a la lista de terminados
                }
                Nodo nod = new Nodo(nodos[j], tmp.distancia + grafo[p][j], tmp);
                // si no está en la cola de prioridad, lo agrega
                if (!cola.contains(nod)) {
                    cola.add(nod);
                    continue;
                }
                // si ya está en la cola de prioridad actualiza la distancia menor
                for (Nodo x : cola) {
                    // si la distancia en la cola es mayor que la distancia calculada
                    if (x.id == nod.id && x.distancia > nod.distancia) {
                        cola.remove(x); // remueve el nodo de la cola
                        cola.add(nod);  // agrega el nodo con la nueva distancia
                        break;          // no sigue revisando
                    }
                }
            }
        }
    }

    // verifica si un nodo ya está en lista de terminados
    public boolean estaTerminado(int j) {
        Nodo tmp = new Nodo(nodos[j]);
        return listos.contains(tmp);
    }

    // encontrar la ruta mínima por fuerza bruta
    public void encontrarRutaMinimaFuerzaBruta(String inicio, String fin) {
        int p1 = posicionNodo(inicio);
        int p2 = posicionNodo(fin);
        // cola para almacenar cada ruta que está siendo evaluada
        Stack<Integer> resultado = new Stack<>();
        resultado.push(p1);
        recorrerRutas(p1, p2, resultado);
    }

    // recorre recursivamente las rutas entre un nodo inicial y un nodo final
    // almacenando en una cola cada nodo visitado
    private void recorrerRutas(int nodoI, int nodoF, Stack<Integer> resultado) {
        // si el nodo inicial es igual al final se evalúa la ruta en revisión
        if (nodoI == nodoF) {
            int respuesta = evaluar(resultado);
            if (respuesta < longitudMasCorta) {
                longitudMasCorta = respuesta;
                rutaMasCorta = "";
                for (int x : resultado) {
                    rutaMasCorta += (nodos[x] + " ");
                }
            }
            return;
        }
        // Si el nodoInicial no es igual al final se crea una lista con todos los nodos
        // adyacentes al nodo inicial que no estén en la ruta en evaluación
        List<Integer> lista = new Vector<>();
        for (int i = 0; i < grafo.length; i++) {
            if (grafo[nodoI][i] != 0 && !resultado.contains(i)) {
                lista.add(i);
            }
        }
        // se recorren todas las rutas formadas con los nodos adyacentes al inicial
        for (int nodo : lista) {
            resultado.push(nodo);
            recorrerRutas(nodo, nodoF, resultado);
            resultado.pop();
        }
    }

    // evaluar la longitud de una ruta
    public int evaluar(Stack<Integer> resultado) {
        int resp = 0;
        int[] r = new int[resultado.size()];
        int i = 0;
        for (int x : resultado) {
            r[i++] = x;
        }
        for (i = 1; i < r.length; i++) {
            resp += grafo[r[i]][r[i - 1]];
        }
        return resp;
    }

}