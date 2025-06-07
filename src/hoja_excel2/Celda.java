/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hoja_excel2;


import java.util.HashSet;
import java.util.Set;

/**
 * Representa una celda individual en la hoja de cálculo con capacidad para:
 * - Almacenar valores directos o fórmulas
 * - Gestionar dependencias entre celdas
 * - Notificar cambios a celdas dependientes
 */
public class Celda {
    private String valor;
    private String formula;
    private Set<Celda> dependencias;
    private Set<Celda> referencias;

    /**
     * Constructor que inicializa una celda vacía
     */
    public Celda() {
        this.valor = "";
        this.formula = "";
        this.dependencias = new HashSet<>();
        this.referencias = new HashSet<>();
    }

    /**
     * Obtiene el valor actual de la celda
     * @return Valor como String (puede ser resultado calculado)
     */
    public String getValor() { 
        return valor; 
    }
    
    /**
     * Establece un valor directo y notifica a celdas dependientes
     * @param valor Nuevo valor a asignar
     */
    public void setValor(String valor) { 
        this.valor = valor;
        notificarCambio();
    }

    /**
     * Obtiene la fórmula original (si existe)
     * @return Fórmula como String
     */
    public String getFormula() { 
        return formula; 
    }
    
    /**
     * Establece una nueva fórmula para la celda
     * @param formula Expresión matemática o referencia
     */
    public void setFormula(String formula) { 
        this.formula = formula; 
    }

    /**
     * Agrega una celda dependiente de esta
     * @param celda Celda que referencia esta celda
     */
    public void addDependencia(Celda celda) {
        dependencias.add(celda);
    }

    /**
     * Registra una celda referenciada por esta celda
     * @param celda Celda que esta celda referencia
     */
    public void addReferencia(Celda celda) {
        referencias.add(celda);
    }

    /**
     * Notifica a todas las celdas dependientes que deben actualizarse
     */
    public void notificarCambio() {
        for (Celda dependiente : dependencias) {
            dependiente.actualizarValor();
        }
    }

    /**
     * Marca la celda para actualización (la evaluación real se hace en el controlador)
     */
    public void actualizarValor() {
        if (!formula.isEmpty()) {
            // La recalculación se delega al controlador
        }
    }

    /**
     * Limpia todas las relaciones de dependencia/referencia
     */
    public void limpiarRelaciones() {
        for (Celda referencia : referencias) {
            referencia.dependencias.remove(this);
        }
        referencias.clear();
    }
}