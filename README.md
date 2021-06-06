# JavaFX Octave Replica Template
Here we are going to implement the [simple calculator](https://github.com/UPV-Programacion-Orientada-a-Objetos/poo-unidad-1-actividad-1-Uriegas) for testing, while the [new one](https://github.com/UPV-Programacion-Orientada-a-Objetos/unidad-2-actividad-1-Uriegas) with new functions is being developed


## Executing code
Compile with:  
```
mvn clean package
```
Run with:
```
java --module-path /usr/lib/jvm/java-11-openjfx/lib --add-modules=javafx.controls,javafx.fxml -cp target/practicaFX-1.0-SNAPSHOT.jar:target/libs/* com.uriegas.App
```