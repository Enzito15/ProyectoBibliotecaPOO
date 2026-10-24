package pe.edu.upeu.proyect_final02.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pe.edu.upeu.proyect_final02.HelloApplication;
import pe.edu.upeu.proyect_final02.implementacion.LibrosDaoImpl;
import pe.edu.upeu.proyect_final02.interfaces.ILibrosDao;
import pe.edu.upeu.proyect_final02.modelo.Libros;
import pe.edu.upeu.proyect_final02.utils.UtilsAlerts;

import java.util.Optional;

public class LibrosController {
    private static UtilsAlerts utilAlerts = new UtilsAlerts();

    @FXML
    private TableView<Libros> tableLibros;
    //variable co relativa
    @FXML
    private TableColumn<Libros, String> ColNumero;
    @FXML
    private TableColumn<Libros, Integer> ColId;
    @FXML
    private TableColumn<Libros, String> ColTitulo;
    @FXML
    private TableColumn<Libros, String> ColVolumen;
    @FXML
    private TableColumn<Libros, String> ColAutor;
    @FXML
    private TableColumn<Libros, Double> ColPrecio;
    @FXML
    private TableColumn<Libros, String> ColEstado;

    private final ILibrosDao librosDao = new LibrosDaoImpl();
    private ObservableList<Libros> listaClientes;

    @FXML
    public void initialize() {
        ColNumero.setCellFactory(colum -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }

        });

        ColId.setCellValueFactory(cellData -> cellData.getValue().idlibrosProperty().asObject());
        ColNumero.setCellValueFactory(cellData -> cellData.getValue().numeroProperty());
        ColTitulo.setCellValueFactory(cellData -> cellData.getValue().tituloProperty());
        ColVolumen.setCellValueFactory(cellData -> cellData.getValue().volumenProperty());
        ColAutor.setCellValueFactory(cellData -> cellData.getValue().autorProperty());
        ColPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty().asObject());
        ColEstado.setCellValueFactory(cellData -> cellData.getValue().estadoProperty());
        //LLamar al metodo
        cargarDatos();

    }

    private void cargarDatos() {
        listaClientes = FXCollections.observableArrayList(librosDao.listAll());
        tableLibros.setItems(listaClientes);
    }

    @FXML
    private void handleDelete() {
        Libros selectId = tableLibros.getSelectionModel().getSelectedItem();
        if (selectId != null) {
            Alert confirm = utilAlerts.mostrarAlerts("Confirmar eliminacion",
                    "Estas seguro de eliminar este Libro? " + selectId.getTitulo() + "?", "CONFIRMATION");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (librosDao.delete(selectId)) {
                    listaClientes.remove(selectId);
                } else {
                    Alert warning = utilAlerts.mostrarAlerts("seleccion requerida",
                            "seleccione un Libro para eliminar ", "WARNING");
                    warning.showAndWait();
                }
            }

        }
    }

    @FXML
    private void handleNew() {
        System.out.println("Abriendo el formulario");
        showForm(null);
    }
    @FXML
    private void handleEdit(){
        Libros selectClient = tableLibros.getSelectionModel().getSelectedItem();
        if (selectClient !=null){
            Libros librosFull = librosDao.searchById(selectClient.getIdlibros());
            showForm(librosFull);
        }else{
            Alert alert = utilAlerts.mostrarAlerts("Seleccion requerida","", "WARNING");
            alert.show();
        }
    }

    private void showForm(Libros l) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/pe/edu/upeu/proyect_final02/views/libros-from.fxml"));
            Parent root = loader.load();
            LibrosFromController libros = loader.getController();
            libros.setLibros(l);
            Stage stage = new Stage();
            stage.setTitle(l==null ?"nuevo libros": "editar libros");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            if (libros.isSaved()){
                Libros LibroProcesado = libros.getLibros();
                if (l==null){
                    boolean result = librosDao.insert(LibroProcesado);
                    if (result){
                        Alert alert = utilAlerts.mostrarAlerts("success", "registro sastisfactorio", "CONFIRMATION");
                        alert.show();
                    }else{
                        Alert alert = utilAlerts.mostrarAlerts("ERROR", "ERROR", "ERROR");
                        alert.show();
                    }
                }else{
                    boolean result = librosDao.update (LibroProcesado);
                    if (result){
                        Alert alert = utilAlerts.mostrarAlerts("success", "registro sastisfactorio", "WARNING");
                        alert.show();
                    }else{
                        Alert alert = utilAlerts.mostrarAlerts("ERROR", "Error", "ERROR");
                        alert.show();
                    }
                }
                cargarDatos();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
