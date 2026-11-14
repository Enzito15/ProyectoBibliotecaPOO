package pe.edu.upeu.proyect_final02.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pe.edu.upeu.proyect_final02.implementacion.UsuarioDaoImpl;
import pe.edu.upeu.proyect_final02.interfaces.IUsuarioDao;
import pe.edu.upeu.proyect_final02.modelo.UserSession;
import pe.edu.upeu.proyect_final02.modelo.Usuario;
import pe.edu.upeu.proyect_final02.utils.UtilsAlerts;

import java.io.IOException;

public class LoginController {
    private static UtilsAlerts utilsAlerts = new UtilsAlerts();
    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPassword;
    private final IUsuarioDao usuarioDao = new UsuarioDaoImpl();

    @FXML
    private void handleLogin(){
            String user = txtUser.getText();
            String pass = txtPassword.getText();

            if (user.isEmpty() || pass.isEmpty()){
                Alert alert = utilsAlerts.mostrarAlerts("Campos vacios",
                        "Por favor ingrese el usuario y la contraseña", "WARNING");
                alert.showAndWait();
                return;
            }
        Usuario u = usuarioDao.login(user, pass);
            if (u !=null){
                Alert alert = utilsAlerts.mostrarAlerts("Acceso Concedido",
                        "Bienvenido,"+u.getNombre()+"!","INFORMATION");
                alert.showAndWait();
                //  Guardar el usuario
                UserSession.getInstance().setUserLogin(u);
                navigateToHome();
            }else{
                Alert alert = utilsAlerts.mostrarAlerts("Acceso Denegado",
                        "Credenciales Incorrectas", "INFORMATION");
                alert.showAndWait();
            }
    }
    private void navigateToHome(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/proyect_final02/views/main-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage)  txtUser.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);//Propiedad para que el sistema Maximize
            stage.setTitle("Sistema de Biblioteca - DashBoard");
            stage.show();
        }catch (IOException e){

        }
    }
}
