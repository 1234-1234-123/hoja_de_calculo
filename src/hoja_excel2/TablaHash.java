/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hoja_excel2;

/**
 * Implementación personalizada de tabla hash con:
 * - Función hash simple basada en suma de caracteres
 * - Resolución de colisiones por dirección abierta
 */
public class TablaHash {
    private String[] tabla;
    private int tamaño;

    /**
     * Crea una nueva tabla hash con tamaño específico
     * @param tamaño Capacidad inicial de la tabla
     */
    public TablaHash(int tamaño) {
        this.tamaño = tamaño;
        tabla = new String[tamaño];
    }

    /**
     * Calcula el índice hash para una clave
     * @param clave String a hashear
     * @return Índice calculado
     */
    private int hash(String clave) {
        int suma = 0;
        for (char c : clave.toCharArray()) {
            suma += c;
        }
        return suma % tamaño;
    }

    /**
     * Inserta un nuevo valor en la tabla
     * @param clave Valor a insertar
     */
    public void agregar(String clave) {
        int indice = hash(clave);
        while (tabla[indice] != null) {
            indice = (indice + 1) % tamaño;
        }
        tabla[indice] = clave;
    }

    /**
     * Obtiene un valor por su índice
     * @param indice Posición en la tabla
     * @return Valor almacenado o null
     */
    public String obtener(int indice) {
        return tabla[indice];
    }

    /**
     * @return Arreglo interno con todos los valores
     */
    public String[] getTabla() {
        return tabla;
    }
}
