module pe.edu.upeu.proyect_final02 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens pe.edu.upeu.proyect_final02 to javafx.fxml;
    exports pe.edu.upeu.proyect_final02;
}