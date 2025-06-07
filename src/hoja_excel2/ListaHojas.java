/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hoja_excel2;

/**
 * Implementación de lista enlazada manual para almacenar hojas de cálculo.
 * Cumple con el requisito de no usar estructuras de datos predefinidas.
 */
public class ListaHojas {
    private NodoLista cabeza;
    private int tamaño;

    /**
     * Constructor que inicializa una lista vacía
     */
    public ListaHojas() {
        cabeza = null;
        tamaño = 0;
    }

    /**
     * Agrega una nueva hoja al final de la lista
     * @param hoja Hoja a agregar
     */
    public void agregarHoja(Hoja hoja) {
        NodoLista nuevoNodo = new NodoLista(hoja);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            NodoLista actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
        tamaño++;
    }

    /**
     * Obtiene una hoja por su índice
     * @param indice Posición en la lista (0-based)
     * @return Hoja solicitada
     * @throws IndexOutOfBoundsException Si el índice es inválido
     */
    public Hoja obtenerHoja(int indice) {
        if (indice < 0 || indice >= tamaño) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        
        NodoLista actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        return actual.hoja;
    }

    /**
     * Elimina una hoja de la lista
     * @param indice Posición de la hoja a eliminar
     */
    public void eliminarHoja(int indice) {
        if (indice < 0 || indice >= tamaño) return;
        
        if (indice == 0) {
            cabeza = cabeza.siguiente;
        } else {
            NodoLista anterior = null;
            NodoLista actual = cabeza;
            for (int i = 0; i < indice; i++) {
                anterior = actual;
                actual = actual.siguiente;
            }
            anterior.siguiente = actual.siguiente;
        }
        tamaño--;
    }

    /**
     * @return Cantidad de hojas en la lista
     */
    public int cantidad() {
        return tamaño;
    }
}