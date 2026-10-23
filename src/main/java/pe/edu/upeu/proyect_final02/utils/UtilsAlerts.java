package pe.edu.upeu.proyect_final02.utils;

import javafx.scene.control.Alert;

public class UtilsAlerts {
    public Alert mostrarAlerts(String titulo, String mensaje, String tipo) {
        Alert alert;
        if (tipo.equals("WARNING")) {
            alert = new Alert(Alert.AlertType.WARNING);
        } else if (tipo.equals("CONFIRMATION")) {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
        }
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert;
    }
}
