# POO-Unidad-2-Actividad-1
### Calculadora científica con interfáz gráfica
### Reporte
[Overleaf](https://www.overleaf.com/read/jqchhwctbwmx)
### Nota
JavaFx no es adecuado para escribir editores de texto o IDE's por lo que la mayoria de la funcionalidad de Octave no se implementa.  

### Objetivo de la Práctica

El alumno deberá demostrar solura en los siguientes temas de la Programación Orientada a Objetos.
  * Manejo de excepciones y errores.
  * Pruebas Unitarias
  * Interfaces gráficas
  * Paquetes

### Descripción de la práctica.

Tomando como base la implementación de la calculadora científica de la actividad 2, se necesita el agregar las siguientes mejoras:

  * Generar una interfáz gráfica al estilo como la que se muesta a continuación
    > ![alt text](https://github.com/UPV-Programacion-Orientada-a-Objetos/POO-Unidad-2-Actividad-1/blob/main/img/fig1.png "Imagen")
    1. Menú principal: Herramientas genéricas como Guardar, Abrir, cambiar estilo.
    2. Explorador de archivos: Permite la navegación de archivos y carpetas para poder cargar datos mediante doble click.
    3. Explorador de variables / objetos: Permite la visualización del estado de los objetos actualmente cargados en memoria.
    4. Historial de comandos: Permite visualizar el historial de los comandos realizados.
    5. Línea de comandos: Permite introducir comandos a la interfaz para realizar acciones.

  * De igual manera se deberá incorporar siguientes comandos
    * `bar(x, y)` deberá generar una gráfica de barras con los datos de `x` y `y`
    * `circle(x, y)` deberá generar una gráfica de pastel con los datos de `x` y `y`
    * `scatter(x, y)` deberá generar una gráfica de disperción con los datos de `x` y `y`
    * `hist(x)` deberá generar un histograma con los datos de `x`
  * Se debe tomar en cuenta que `x` y/o `y` puede ser una variable de tipo `ArrayList`, `Vector` o un objeto de tipo `EquationTree`

### Requerimientos Funcionales
Se deberá hacer uso de los siguientes temas:

  * Manejo de excepciones
  * Excepciones propias
  * Propagación de errores.
  * Pruebas Unitarias
  * Pruebas de integración
  * Notación infija
  * Interfaces gráficas

De igual manera se deberá tener lo siguiente:

  * Se deberá crear un nuevo proyecto mediante maven. **No se deberá crear una nueva carpeta contenedora** el archivo `pom` deberá estar en la raiz del repositorio.
  * Se deberá utilizar clases que implementen Pruebas Unitarias y Pruebas de Integración.
  * Se podrán introducir los siguientes elementos:
    * Operaciones: `+`, `-`, `*`, `/`, `^`, `sin`, `cos`, `tan`, `sqrt`, `()`, `[]`
    * Operandos: 0-9, x
    * No deberá existir límite en el número de operaciones y operandos.
    * El programa deberá emitir un mensaje de error si existiera una sintaxis erronea en la ecuación.
    * **El programa no deberá cerrarce por ningúna excepción o error** 

### Requerimientos no funcionales

  * Se introducirán las ecuaciones en notación infija en la línea de comandos o mediante un archivo.
  * Se introducirán comandos en la interfaz de acción.

### Entregables:

  1. Código de la implementación documentado mediante JavaDoc.
  2. Se deberá generar el archivo Jar => **Se subirá a plataforma google classroom**
  3. Se deberá generar un reporte de actividades en formato PDF => **Se subirá a plataforma google classroom**

