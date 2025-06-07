/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hoja_excel2;

    import javax.swing.*;
    import javax.swing.table.*;
    import java.awt.*;
    import java.util.ArrayList;

    /**
     * Vista principal de la aplicación que implementa la interfaz gráfica
     * utilizando componentes Swing.
     * Permite gestionar múltiples hojas, agregar, eliminar y renombrar hojas,
     * y mostrar una tabla hash como funcionalidad adicional.
     */
    public class HojaView extends JFrame {
        // Componentes de la interfaz
        public JMenuBar menuBar;
        public JTextField campoFormula;
        public JButton btnAplicar, btnRechazar;
        public JTabbedPane pestañas;
        public ArrayList<JTable> tablas;
        public ArrayList<DefaultTableModel> modelos;
        public JLabel lblFormula;

        // Referencias a modelo y controlador
        private ListaHojas listaHojas;
        private HojaController controlador;
        private int contadorHojas = 1;

        /**
         * Constructor principal.
         * Garantiza que siempre haya al menos una hoja visible al iniciar la aplicación
         * y muestra la ventana.
         * @param listaHojas Modelo de datos a representar
         */
        public HojaView(ListaHojas listaHojas) {
            this.listaHojas = listaHojas;
            // Si la lista está vacía, agrega una hoja inicial por defecto
            if (listaHojas.cantidad() == 0) {
                Hoja hojaInicial = new Hoja("Hoja 1", 50, 50);
                listaHojas.agregarHoja(hojaInicial);
            }
            configurarInterfaz();
            cargarHojasIniciales();
            // Es fundamental para que la ventana sea visible
            setVisible(true);
        }

        /**
         * Configura los parámetros básicos de la ventana
         */
        private void configurarInterfaz() {
            setTitle("Hoja Electrónica");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            setSize(900, 600);
            setLocationRelativeTo(null);

            configurarMenu();
            configurarPanelSuperior();
            configurarPestañas();
        }

        /**
         * Configura la barra de menú con sus opciones:
         * - Archivo: Tabla hash, Insertar hoja, Eliminar hoja, Renombrar hoja
         * - Ayuda
         */
        private void configurarMenu() {
            menuBar = new JMenuBar();

            JMenu menuArchivo = new JMenu("Archivo");
            JMenuItem menuItemHash = new JMenuItem("Tabla hash");
            menuItemHash.addActionListener(e -> mostrarTablaHash());
            menuArchivo.add(menuItemHash);

            JMenuItem insertarHoja = new JMenuItem("Insertar Hoja");
            insertarHoja.addActionListener(e -> insertarNuevaHoja());
            menuArchivo.add(insertarHoja);

            JMenuItem eliminarHoja = new JMenuItem("Eliminar Hoja");
            eliminarHoja.addActionListener(e -> eliminarHojaActual());
            menuArchivo.add(eliminarHoja);

            JMenuItem renombrarHoja = new JMenuItem("Renombrar Hoja");
            renombrarHoja.addActionListener(e -> renombrarHojaActual());
            menuArchivo.add(renombrarHoja);

            JMenu menuAyuda = new JMenu("Ayuda");

            menuBar.add(menuArchivo);
            menuBar.add(menuAyuda);
            setJMenuBar(menuBar);
        }

        /**
         * Configura el panel superior con campo de fórmula y botones
         */
        private void configurarPanelSuperior() {
            JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
            lblFormula = new JLabel("f(x):");
            campoFormula = new JTextField(30);
            btnAplicar = new JButton("Aplicar");
            btnRechazar = new JButton("Rechazar");

            panelSuperior.add(lblFormula);
            panelSuperior.add(campoFormula);
            panelSuperior.add(btnAplicar);
            panelSuperior.add(btnRechazar);
            add(panelSuperior, BorderLayout.NORTH);
        }

        /**
         * Configura el contenedor de pestañas para múltiples hojas
         */
        private void configurarPestañas() {
            pestañas = new JTabbedPane();
            tablas = new ArrayList<>();
            modelos = new ArrayList<>();
            add(pestañas, BorderLayout.CENTER);
        }

        /**
         * Carga las hojas iniciales del modelo a la vista
         */
        private void cargarHojasIniciales() {
            for (int i = 0; i < listaHojas.cantidad(); i++) {
                agregarHojaAVista(i);
            }
        }

        /**
         * Crea y agrega una nueva hoja a la vista
         * @param indiceHoja Posición de la hoja en el modelo
         */
        private void agregarHojaAVista(int indiceHoja) {
            Hoja hoja = listaHojas.obtenerHoja(indiceHoja);

            // Configurar encabezados de columnas (A, B, C...)
            String[] columnas = new String[hoja.getColumnas()];
            for (int j = 0; j < hoja.getColumnas(); j++) {
                columnas[j] = String.valueOf((char) ('A' + j));
            }

            // Crear modelo de tabla editable
            DefaultTableModel modelo = new DefaultTableModel(hoja.getFilas(), hoja.getColumnas()) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return true;
                }
            };
            modelo.setColumnIdentifiers(columnas);

            // Configurar tabla
            JTable tabla = new JTable(modelo);
            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tabla.setRowSelectionAllowed(true);
            tabla.setCellSelectionEnabled(true);

            // Configurar scroll con numeración de filas
            JScrollPane scrollPane = new JScrollPane(tabla);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            agregarNumeracionFilas(scrollPane, tabla, hoja.getFilas());

            // Agregar a las estructuras de datos
            tablas.add(tabla);
            modelos.add(modelo);
            pestañas.addTab(hoja.getNombre(), scrollPane);
        }

        /**
         * Agrega numeración de filas al lado izquierdo de la tabla
         * @param scrollPane JScrollPane que contiene la tabla
         * @param tabla JTable a la que se le agregará la numeración
         * @param numFilas Número total de filas para numerar
         */
        private void agregarNumeracionFilas(JScrollPane scrollPane, JTable tabla, int numFilas) {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (int i = 1; i <= numFilas; i++) {
                listModel.addElement(String.valueOf(i));
            }

            JList<String> rowHeader = new JList<>(listModel);
            rowHeader.setFixedCellWidth(50);
            rowHeader.setFixedCellHeight(tabla.getRowHeight());
            rowHeader.setCellRenderer(new RowHeaderRenderer(tabla));
            rowHeader.setBackground(Color.LIGHT_GRAY);

            scrollPane.setRowHeaderView(rowHeader);
        }

        /**
         * Muestra la interfaz de tabla hash en una ventana aparte
         */
        private void mostrarTablaHash() {
            JFrame frameHash = new JFrame("Tabla Hash");
            frameHash.setSize(600, 400);
            frameHash.setLocationRelativeTo(this);

            JPanel panel = new JPanel(new BorderLayout());
            JTextField txtEntrada = new JTextField(20);
            JButton btnAgregar = new JButton("Agregar");
            DefaultTableModel modeloTabla = new DefaultTableModel(new Object[]{"Clave", "Índice"}, 0);
            JTable tablaHash = new JTable(modeloTabla);

            TablaHash hash = new TablaHash(10);

            btnAgregar.addActionListener(e -> {
                String clave = txtEntrada.getText();
                if (!clave.isEmpty()) {
                    hash.agregar(clave);
                    actualizarTablaHash(tablaHash, hash);
                    txtEntrada.setText("");
                }
            });

            JPanel panelSuperior = new JPanel();
            panelSuperior.add(new JLabel("Dato:"));
            panelSuperior.add(txtEntrada);
            panelSuperior.add(btnAgregar);

            panel.add(panelSuperior, BorderLayout.NORTH);
            panel.add(new JScrollPane(tablaHash), BorderLayout.CENTER);

            frameHash.add(panel);
            frameHash.setVisible(true);
        }

        /**
         * Actualiza la visualización de la tabla hash
         * @param tabla JTable donde se muestran los datos
         * @param hash TablaHash con los datos actuales
         */
        private void actualizarTablaHash(JTable tabla, TablaHash hash) {
            DefaultTableModel model = (DefaultTableModel) tabla.getModel();
            model.setRowCount(0);

            String[] datos = hash.getTabla();
            for (int i = 0; i < datos.length; i++) {
                if (datos[i] != null) {
                    model.addRow(new Object[]{datos[i], i});
                }
            }
        }

        /**
         * Crea una nueva hoja y la agrega a la vista.
         * Pide al usuario el nombre de la hoja.
         */
        private void insertarNuevaHoja() {
            String nombreHoja = JOptionPane.showInputDialog(this, "Nombre de la nueva hoja:", "Nueva hoja", JOptionPane.PLAIN_MESSAGE);
            if (nombreHoja == null || nombreHoja.trim().isEmpty()) {
                nombreHoja = "Hoja " + (contadorHojas + 1);
            }
            Hoja nuevaHoja = new Hoja(nombreHoja, 50, 50);
            listaHojas.agregarHoja(nuevaHoja);
            agregarHojaAVista(listaHojas.cantidad() - 1);
            pestañas.setSelectedIndex(pestañas.getTabCount() - 1);
            contadorHojas++;
        }

        /**
         * Elimina la hoja actualmente seleccionada tras confirmación.
         * Actualiza el modelo y la vista.
         */
        private void eliminarHojaActual() {
            int indice = pestañas.getSelectedIndex();
            if (indice < 0) {
                JOptionPane.showMessageDialog(this, "No hay hoja seleccionada para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar la hoja \"" + listaHojas.obtenerHoja(indice).getNombre() + "\"?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                listaHojas.eliminarHoja(indice);
                pestañas.remove(indice);
                tablas.remove(indice);
                modelos.remove(indice);
            }
        }

        /**
         * Permite renombrar la hoja actualmente seleccionada.
         * Solicita al usuario el nuevo nombre y actualiza modelo y vista.
         */
        private void renombrarHojaActual() {
            int indice = pestañas.getSelectedIndex();
            if (indice < 0) {
                JOptionPane.showMessageDialog(this, "No hay hoja seleccionada para renombrar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String nombreActual = listaHojas.obtenerHoja(indice).getNombre();
            String nuevoNombre = JOptionPane.showInputDialog(this, "Ingrese el nuevo nombre para la hoja:", nombreActual);
            if (nuevoNombre == null) {
                // Usuario canceló el diálogo
                return;
            }
            nuevoNombre = nuevoNombre.trim();
            if (nuevoNombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Actualizar nombre en el modelo
            Hoja hoja = listaHojas.obtenerHoja(indice);
            hoja.setNombre(nuevoNombre);
            // Actualizar título de la pestaña
            pestañas.setTitleAt(indice, nuevoNombre);
        }

        // Métodos de acceso para el controlador

        public void setControlador(HojaController controlador) {
            this.controlador = controlador;
        }

        public JTable getTabla(int indice) {
            return tablas.get(indice);
        }

        public DefaultTableModel getModelo(int indice) {
            return modelos.get(indice);
        }

        public int getNumeroHojas() {
            return tablas.size();
        }

        /**
         * Renderizador personalizado para los números de fila
         */
        private class RowHeaderRenderer extends JLabel implements javax.swing.ListCellRenderer<String> {
            private JTable tabla;

            public RowHeaderRenderer(JTable tabla) {
                this.tabla = tabla;
                setOpaque(true);
                setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                setHorizontalAlignment(JLabel.CENTER);
                setFont(tabla.getTableHeader().getFont());
            }

            @Override
            public Component getListCellRendererComponent(JList<? extends String> list,
                                                          String value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                setText(value);
                setBackground(tabla.getTableHeader().getBackground());
                setForeground(tabla.getTableHeader().getForeground());
                return this;
            }
        }
    }
