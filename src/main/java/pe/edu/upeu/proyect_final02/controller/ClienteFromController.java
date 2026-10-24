package pe.edu.upeu.proyect_final02.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pe.edu.upeu.proyect_final02.modelo.Cliente;
import pe.edu.upeu.proyect_final02.utils.UtilsAlerts;

public class ClienteFromController {
    public static UtilsAlerts utilsAlerts = new UtilsAlerts();

    @FXML
    private Label lbTitulo;
    @FXML
    private TextField txtNombre, txtApellidos, txtDni, txtEmail;
    @FXML
    private Cliente cliente;
    private boolean saved = false;

    public boolean isSaved(){
        return saved;
    }
    public Cliente getCliente(){
        return cliente;
    }


    private boolean validated(){
        if (txtNombre.getText().isEmpty() || txtDni.getText().isEmpty()){
            Alert alert = utilsAlerts.mostrarAlerts("","Nombre y DNI son Obligatoorios","ERROR");
                    alert.show();
            return false;
        }
        return true;
    }
    private void cerrar(){
        Stage stage = (Stage)  txtNombre.getScene().getWindow();
        stage.close();
    }
    //Acciones a la vista
    @FXML
    private void handleCancel(){
        cerrar();
    }
    @FXML
    private void handleSave(){
        if (validated()){
            if (cliente == null) cliente = new Cliente();
            cliente.setNombre(txtNombre.getText());
            cliente.setApellidos(txtApellidos.getText());
            cliente.setDni(txtDni.getText());
            cliente.setEmail(txtEmail.getText());
            saved = true;
            cerrar();
        }
    }
    //Metodo para enviar los datos al formulario y editar
    public void setCliente(Cliente c){
        this.cliente = c;
        if (c!= null){
            lbTitulo.setText("EDITAR CLIENTE");
            txtNombre.setText(c.getNombre());
            txtApellidos.setText(c.getApellidos());
            txtDni.setText(c.getDni());
            txtEmail.setText(c.getEmail());
        }
    }
}
