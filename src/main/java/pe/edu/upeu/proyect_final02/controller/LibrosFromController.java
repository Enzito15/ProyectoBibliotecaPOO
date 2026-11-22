package pe.edu.upeu.proyect_final02.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pe.edu.upeu.proyect_final02.modelo.Cliente;
import pe.edu.upeu.proyect_final02.modelo.Libros;
import pe.edu.upeu.proyect_final02.utils.UtilsAlerts;
import pe.edu.upeu.proyect_final02.implementacion.LibrosDaoImpl;
import pe.edu.upeu.proyect_final02.interfaces.ILibrosDao;

public class LibrosFromController {
    public static UtilsAlerts utilsAlerts = new UtilsAlerts();
    // esto hace que al editar un libro no choque con su propio número. Con esto, si se repites un número
    // el formulario se queda abierto con el aviso y no pierdes lo escrito (Error de ultima hora XDD)
    private final ILibrosDao librosDao = new LibrosDaoImpl();

    @FXML
    private Label lbTitulo;
    @FXML
    private TextField txtTitulo, txtVolumen, txtAutor,txtEstado;
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
        if (txtTitulo.getText().trim().isEmpty() || txtVolumen.getText().trim().isEmpty()
                || txtAutor.getText().trim().isEmpty() || txtEstado.getText().trim().isEmpty()){
            Alert alert = utilsAlerts.mostrarAlerts("","Titulo, Volumen, Autor y Estado son Obligatorios","ERROR");
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
            if (libros == null) libros = new Libros();
            libros.setTitulo(txtTitulo.getText());
            libros.setVolumen(txtVolumen.getText());
            libros.setAutor(txtAutor.getText());
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
            txtEstado.setText(l.getEstado());
        }
    }
}
