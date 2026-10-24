module pe.edu.upeu.proyect_final02 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    //requuiero la dependencia del sqlite
    requires org.xerial.sqlitejdbc;
    requires javafx.base;
    requires javafx.graphics;
    requires pe.edu.upeu.proyect_final02;

    opens pe.edu.upeu.proyect_final02 to javafx.fxml;
    //Abrir el modelo para que las tablas de FX Funcionen
    opens pe.edu.upeu.proyect_final02.modelo to javafx.base,javafx.fxml;
    opens pe.edu.upeu.proyect_final02.controller to javafx.fxml;
    opens pe.edu.upeu.proyect_final02.views to javafx.fxml;

    exports pe.edu.upeu.proyect_final02;

    exports pe.edu.upeu.proyect_final02.utils;
    exports pe.edu.upeu.proyect_final02.test;
    exports pe.edu.upeu.proyect_final02.interfaces;
    exports pe.edu.upeu.proyect_final02.implementacion;
    exports pe.edu.upeu.proyect_final02.controller;

}