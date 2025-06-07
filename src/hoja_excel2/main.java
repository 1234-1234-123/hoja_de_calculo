/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hoja_excel2;



import javax.swing.SwingUtilities;

/**
 * Punto de entrada principal de la aplicación.
 * Inicializa el modelo, vista y controlador.
 */
public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ListaHojas listaHojas = new ListaHojas();
            HojaView vista = new HojaView(listaHojas);
            new HojaController(listaHojas, vista);
        });
    }
}