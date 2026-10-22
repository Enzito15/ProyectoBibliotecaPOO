package pe.edu.upeu.proyect_final02;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/pe/edu/upeu/proyect_final02/views/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Biblioteca");
        stage.setScene(scene);
        stage.show();
    }
}
