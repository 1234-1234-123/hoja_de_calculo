/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hoja_excel2;



import java.io.Serializable;

/**
 * Representa una hoja de cálculo con una matriz bidimensional de celdas.
 * Permite serialización para almacenamiento en disco.
 */
public class Hoja implements Serializable {
    private static final long serialVersionUID = 1L;
    private Celda[][] celdas;
    private String nombre;
    private int filas, columnas;

    /**
     * Crea una nueva hoja con dimensiones específicas
     * @param nombre Identificador de la hoja
     * @param filas Cantidad de filas
     * @param columnas Cantidad de columnas
     */
    public Hoja(String nombre, int filas, int columnas) {
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.celdas = new Celda[filas][columnas];
        inicializarCeldas();
    }

    /**
     * Inicializa todas las celdas con valores por defecto
     */
    private void inicializarCeldas() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                celdas[i][j] = new Celda();
            }
        }
    }

    /**
     * Obtiene una celda específica
     * @param fila Índice de fila (0-based)
     * @param columna Índice de columna (0-based)
     * @return Objeto Celda en la posición solicitada
     */
    public Celda getCelda(int fila, int columna) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            throw new IndexOutOfBoundsException("Coordenadas de celda inválidas");
        }
        return celdas[fila][columna];
    }

    /**
     * Establece un valor directo en una celda
     * @param fila Índice de fila
     * @param columna Índice de columna
     * @param valor Valor a asignar
     */
    public void setCelda(int fila, int columna, String valor) {
        getCelda(fila, columna).setValor(valor);
    }

    /**
     * Obtiene el nombre de la hoja
     * @return Nombre actual de la hoja
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece un nuevo nombre para la hoja.
     * @param nombre Nuevo nombre que se asignará a la hoja
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la cantidad de filas
     * @return Número de filas
     */
    public int getFilas() {
        return filas;
    }

    /**
     * Obtiene la cantidad de columnas
     * @return Número de columnas
     */
    public int getColumnas() {
        return columnas;
    }
}
