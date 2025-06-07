/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hoja_excel2;

/**
 * Nodo básico para la lista enlazada de hojas.
 * Almacena una hoja y referencia al siguiente nodo.
 */
public class NodoLista {
    public Hoja hoja;
    public NodoLista siguiente;

    /**
     * Constructor que crea un nodo con una hoja
     * @param hoja Hoja a almacenar en el nodo
     */
    public NodoLista(Hoja hoja) {
        this.hoja = hoja;
        this.siguiente = null;
    }
}
