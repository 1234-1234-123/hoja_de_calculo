# Aplicación de Hoja de Cálculo

---

Este proyecto implementa una **aplicación de hoja de cálculo básica en Java Swing** con funcionalidades esenciales como la gestión de múltiples hojas, edición de celdas, soporte para fórmulas simples (suma, resta, multiplicación y división), y un sistema de dependencias para la actualización automática de valores. Además, incluye una implementación de una tabla hash.

## Estructura del Proyecto (Clases)

El proyecto sigue un patrón de diseño **Modelo-Vista-Controlador (MVC)**, lo que facilita la separación de responsabilidades y la mantenibilidad del código.

### `Celda.java`

Representa una **celda individual** dentro de la hoja de cálculo. Cada celda puede almacenar un **valor directo** o una **fórmula**. Es responsable de:

* Almacenar su propio valor y fórmula.
* Gestionar las **dependencias** (`Set<Celda> dependencias`) y **referencias** (`Set<Celda> referencias`) con otras celdas, lo que permite que una celda notifique a sus dependientes cuando su valor cambia.
* Notificar a las celdas dependientes para que se actualicen (`notificarCambio()`).
* Limpiar sus relaciones de dependencia/referencia (`limpiarRelaciones()`) para evitar referencias circulares o no deseadas al cambiar el contenido de una celda.

### `Hoja.java`

Representa una **hoja de cálculo completa**, organizada como una matriz bidimensional de objetos `Celda`.

* Almacena las `Celda` en un arreglo `Celda[][] celdas`.
* Permite obtener y establecer celdas en posiciones específicas.
* Implementa `Serializable` para posibilitar el guardado y la carga de hojas.

### `HojaController.java`

Actúa como el **intermediario entre la vista (`HojaView`) y el modelo (`ListaHojas` y `Hoja`)**. Su rol principal es:

* Gestionar las **interacciones del usuario** con la interfaz (edición de celdas, aplicación de fórmulas).
* **Evaluar fórmulas** ingresadas en las celdas, incluyendo la lógica para el parseo de referencias a celdas (ej. "B3") y la realización de operaciones (suma, multiplicación).
* **Sincronizar el modelo y la vista**, asegurando que los cambios en una se reflejen en la otra.
* Configurar los `Listeners` para la tabla y el campo de fórmula, permitiendo la reactividad de la aplicación.

### `HojaView.java`

Es la **interfaz gráfica de usuario (GUI)** de la aplicación, construida con Java Swing. Se encarga de:

* Mostrar las hojas de cálculo en un `JTabbedPane`, permitiendo múltiples hojas.
* Visualizar las celdas en objetos `JTable`.
* Proporcionar un campo de texto (`JTextField`) para ingresar fórmulas o valores.
* Ofrecer botones para aplicar y (opcionalmente) rechazar cambios.
* Implementar un menú con opciones para **insertar, eliminar y renombrar hojas**.
* Incluir una funcionalidad adicional para **mostrar una tabla hash**.
* Gestionar la numeración de filas en las tablas.

### `ListaHojas.java`

Una **implementación personalizada de una lista enlazada** para almacenar objetos `Hoja`. Esta clase fue diseñada para cumplir con el requisito de **no utilizar estructuras de datos predefinidas de Java** para la gestión de las hojas.

* Permite agregar, obtener y eliminar hojas por índice.
* Mantiene un registro del tamaño de la lista.

### `NodoLista.java`

Clase auxiliar utilizada por `ListaHojas` para construir la lista enlazada. Cada `NodoLista` contiene un objeto `Hoja` y una referencia al siguiente `NodoLista`.

### `TablaHash.java`

Una **implementación simple de una tabla hash** con las siguientes características:

* **Función hash personalizada** basada en la suma de los valores ASCII de los caracteres de la clave.
* **Resolución de colisiones por direccionamiento abierto** (sondeo lineal).
* Permite agregar y obtener valores.
* Utilizada en la funcionalidad adicional de la `HojaView` para demostrar su uso.

### `main.java`

El **punto de entrada principal** de la aplicación. Es responsable de:

* Inicializar el modelo (`ListaHojas`).
* Inicializar la vista (`HojaView`).
* Inicializar el controlador (`HojaController`), conectando el modelo y la vista.
* Ejecutar la interfaz gráfica en el hilo de despacho de eventos de Swing (`SwingUtilities.invokeLater`).

---

## Cómo Ejecutar la Aplicación

Para ejecutar esta aplicación, necesitarás un entorno de desarrollo Java (JDK) instalado.

1.  **Clonar o descargar el código fuente.**
2.  **Compilar las clases:** Abre una terminal en la raíz del proyecto y compila los archivos Java:
    ```bash
    javac *.java
    ```
3.  **Ejecutar la aplicación:**
    ```bash
    java main
    ```

Esto abrirá la ventana principal de la aplicación de hoja de cálculo.

---

## Funcionalidades Clave

* **Múltiples Hojas:** Gestiona diferentes hojas de cálculo a través de pestañas.
* **Edición de Celdas:** Permite ingresar valores directos o fórmulas en las celdas.
* **Fórmulas Básicas:** Soporta fórmulas de suma (`=A1+B2`) , resta (`=A1-B2`), multiplicación (`=A1*B2`) y división (`=A1/B2`).
* **Actualización Automática:** Las celdas se actualizan automáticamente cuando sus dependencias cambian.
* **Gestión de Hojas:** Opciones de menú para insertar, eliminar y renombrar hojas.
* **Tabla Hash:** Una ventana adicional para interactuar con una implementación básica de una tabla hash.

---

## Contribuciones

Las contribuciones son bienvenidas. Siéntase libre de bifurcar el repositorio y enviar solicitudes de extracción.

---

## Licencia

Este proyecto está bajo la licencia [Carlos Guillermo Ardón Garrido, Melannie Rosse Lorenzana Ajanel, Christian Emanuel Garcia Figueroa].
