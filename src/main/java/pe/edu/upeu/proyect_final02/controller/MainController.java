package pe.edu.upeu.proyect_final02.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {
    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        showHome();
    }

    @FXML
    private void showHome() {
        loadview("home-view.fxml");

    }

    @FXML
    private void showClient() {
        loadview("cliente-view.fxml");
    }

    @FXML
    private void showLibros() {
        loadview("libros-view.fxml");
    }

    @FXML
    private void showReserva() {
        loadview("reserva-view.fxml");
    }


    //metodo que llama y busca a las vistas
    private void loadview(String fxml) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/proyect_final02/views/" + fxml));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            System.err.println("Error de carga de vista" + e.getMessage());

        }

    }

}

