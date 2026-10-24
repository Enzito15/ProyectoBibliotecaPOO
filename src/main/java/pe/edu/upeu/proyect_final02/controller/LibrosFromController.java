package pe.edu.upeu.proyect_final02.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pe.edu.upeu.proyect_final02.modelo.Cliente;
import pe.edu.upeu.proyect_final02.modelo.Libros;
import pe.edu.upeu.proyect_final02.utils.UtilsAlerts;

public class LibrosFromController {
    public static UtilsAlerts utilsAlerts = new UtilsAlerts();

    @FXML
    private Label lbTitulo;
    @FXML
    private TextField txtTitulo, txtVolumen, txtAutor, txtPrecio,txtEstado;
    @FXML
    private Libros libros;
    private boolean saved = false;

    public boolean isSaved(){
        return saved;
    }
    public Libros getLibros(){
        return libros;
    }


    private boolean validated(){
        if (txtTitulo.getText().isEmpty() && txtAutor.getText().isEmpty()){
            Alert alert = utilsAlerts.mostrarAlerts("","Titulo y Autor son Obligatoorios","ERROR");
            alert.show();
            return false;
        }
        return true;
    }
    private void cerrar(){
        Stage stage = (Stage)  txtTitulo.getScene().getWindow();
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
            double precio;
            try {
                precio = Double.parseDouble(txtPrecio.getText().trim());
            } catch (NumberFormatException e) {
                Alert alert = utilsAlerts.mostrarAlerts("", "El precio debe ser un número válido", "ERROR");
                alert.show();
                return;
            }

            if (libros == null) libros = new Libros();
            libros.setTitulo(txtTitulo.getText());
            libros.setVolumen(txtVolumen.getText());
            libros.setAutor(txtAutor.getText());
            libros.setPrecio(precio);
            libros.setEstado(txtEstado.getText());
            saved = true;
            cerrar();
        }
    }
    //Metodo para enviar los datos al formulario y editar
    public void setLibros(Libros l){
        this.libros = l;
        if (l!= null){
            lbTitulo.setText("EDITAR LIBRO");
            txtTitulo.setText(l.getTitulo());
            txtVolumen.setText(l.getVolumen());
            txtAutor.setText(l.getAutor());
            txtPrecio.setText(String.valueOf(l.getPrecio()));
            txtEstado.setText(l.getEstado());
        }
    }
}
