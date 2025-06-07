/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hoja_excel2;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador principal que conecta la vista (HojaView) con el modelo (Hoja).
 * Gestiona la edición y evaluación de fórmulas, actualización del campo fórmula,
 * y sincronización entre modelo y vista. Soporta suma, resta, multiplicación y división.
 */
public class HojaController {
    private ListaHojas listaHojas;
    private HojaView vista;

    /**
     * Constructor del controlador.
     * @param listaHojas Lista enlazada de hojas de cálculo (modelo)
     * @param vista      Vista principal de la aplicación
     */
    public HojaController(ListaHojas listaHojas, HojaView vista) {
        this.listaHojas = listaHojas;
        this.vista = vista;
        vista.setControlador(this);
        configurarListeners();
        configurarBotonAplicar();
    }

    /**
     * Configura el listener para el botón "Aplicar" que aplica la fórmula o valor
     * ingresado en el campo fórmula a la celda seleccionada.
     */
    private void configurarBotonAplicar() {
        vista.btnAplicar.addActionListener(e -> aplicarFormulaACeldaSeleccionada());
    }

    /**
     * Aplica la fórmula o valor ingresado en el campo de fórmula a la celda seleccionada.
     * Reemplaza cualquier valor o fórmula previa que tuviera la celda.
     */
    private void aplicarFormulaACeldaSeleccionada() {
        int indiceHoja = vista.pestañas.getSelectedIndex();
        if (indiceHoja < 0) {
            JOptionPane.showMessageDialog(vista, "No hay hoja seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTable tabla = vista.getTabla(indiceHoja);
        int fila = tabla.getSelectedRow();
        int columna = tabla.getSelectedColumn();

        if (fila < 0 || columna < 0) {
            JOptionPane.showMessageDialog(vista, "No hay celda seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String textoFormula = vista.campoFormula.getText().trim();
        if (textoFormula.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El campo de fórmula está vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Hoja hojaActual = listaHojas.obtenerHoja(indiceHoja);
        Celda celda = hojaActual.getCelda(fila, columna);

        // Limpiar fórmula previa y relaciones para evitar conflictos
        celda.setFormula("");
        celda.limpiarRelaciones();

        String resultado;

        if (textoFormula.startsWith("=")) {
            // Guardar la nueva fórmula
            celda.setFormula(textoFormula);
            // Evaluar la fórmula para obtener el resultado
            resultado = evaluarFormula(textoFormula, hojaActual);
        } else {
            resultado = textoFormula;
        }

        // Actualizar el valor de la celda con el resultado o valor directo
        celda.setValor(resultado);

        // Actualizar la tabla en la vista para reflejar el nuevo valor
        DefaultTableModel modelo = vista.getModelo(indiceHoja);
        modelo.setValueAt(resultado, fila, columna);
    }

    /**
     * Configura los listeners para cada hoja:
     * - Listener para edición directa en tabla que evalúa fórmulas.
     * - Listener para actualizar el campo fórmula al cambiar la celda seleccionada.
     */
    private void configurarListeners() {
        for (int i = 0; i < vista.getNumeroHojas(); i++) {
            final int hojaIndex = i;
            DefaultTableModel modelo = vista.getModelo(i);
            JTable tabla = vista.getTabla(i);

            // Listener para edición directa en tabla
            modelo.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int fila = e.getFirstRow();
                        int columna = e.getColumn();
                        if (columna < 0 || fila < 0) return;

                        String valorIngresado = String.valueOf(modelo.getValueAt(fila, columna));
                        Hoja hojaActual = listaHojas.obtenerHoja(hojaIndex);
                        Celda celda = hojaActual.getCelda(fila, columna);

                        String resultado;
                        if (valorIngresado.startsWith("=")) {
                            resultado = evaluarFormula(valorIngresado, hojaActual);
                            celda.setFormula(valorIngresado);
                        } else {
                            resultado = valorIngresado;
                            celda.setFormula("");
                        }

                        celda.setValor(resultado);

                        if (!resultado.equals(valorIngresado)) {
                            modelo.setValueAt(resultado, fila, columna);
                        }
                    }
                }
            });

            // Listener para actualizar campo fórmula al cambiar selección
            tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        int fila = tabla.getSelectedRow();
                        int columna = tabla.getSelectedColumn();
                        if (fila >= 0 && columna >= 0) {
                            Celda celda = listaHojas.obtenerHoja(hojaIndex).getCelda(fila, columna);
                            String formula = celda.getFormula();
                            if (formula != null && !formula.isEmpty()) {
                                vista.campoFormula.setText(formula);
                            } else {
                                String valor = celda.getValor();
                                vista.campoFormula.setText(valor != null ? valor : "");
                            }
                        } else {
                            vista.campoFormula.setText("");
                        }
                    }
                }
            });
        }
    }

    /**
     * Evalúa una fórmula simple de suma, resta, multiplicación o división entre dos celdas.
     * Fórmulas válidas: "=B3+B4", "=C2-C1", "=D1*E2", "=F3/G4"
     * @param formula La fórmula ingresada (ejemplo: "=B3+B4")
     * @param hoja    La hoja de cálculo para obtener valores de celdas
     * @return Resultado de la operación o "ERROR" si no se puede evaluar
     */
    public String evaluarFormula(String formula, Hoja hoja) {
        if (formula == null || !formula.startsWith("=")) return formula;
        String expr = formula.substring(1); // Quita el '='

        // Detectar el operador
        String operador = null;
        if (expr.contains("+")) operador = "+";
        else if (expr.contains("-")) operador = "-";
        else if (expr.contains("*")) operador = "*";
        else if (expr.contains("/")) operador = "/";

        if (operador == null) return "ERROR";

        String[] partes = expr.split("\\" + operador);
        if (partes.length != 2) return "ERROR";

        double valor1 = obtenerValorCelda(partes[0].trim(), hoja);
        double valor2 = obtenerValorCelda(partes[1].trim(), hoja);

        switch (operador) {
            case "+":
                return String.valueOf(valor1 + valor2);
            case "-":
                return String.valueOf(valor1 - valor2);
            case "*":
                return String.valueOf(valor1 * valor2);
            case "/":
                if (valor2 == 0) return "DIV/0";
                return String.valueOf(valor1 / valor2);
            default:
                return "ERROR";
        }
    }

    /**
     * Obtiene el valor numérico de una celda dada su referencia tipo "B3".
     * Retorna 0 si la referencia es inválida o la celda está vacía.
     * @param ref  Referencia de celda (ejemplo: "B3")
     * @param hoja La hoja de cálculo donde buscar la celda
     * @return Valor numérico de la celda o 0 en caso de error
     */
    private double obtenerValorCelda(String ref, Hoja hoja) {
        if (ref.length() < 2) return 0;
        char colChar = ref.charAt(0);
        int col = Character.toUpperCase(colChar) - 'A';
        int fila;
        try {
            fila = Integer.parseInt(ref.substring(1)) - 1; // Convierte "3" en 2 (base 0)
        } catch (NumberFormatException e) {
            return 0;
        }
        try {
            String valor = hoja.getCelda(fila, col).getValor();
            return Double.parseDouble(valor);
        } catch (Exception e) {
            return 0;
        }
    }
}
