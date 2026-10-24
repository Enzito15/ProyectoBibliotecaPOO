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
import pe.edu.upeu.proyect_final02.implementacion.ClienteDaoImpl;
import pe.edu.upeu.proyect_final02.interfaces.IClienteDao;
import pe.edu.upeu.proyect_final02.modelo.Cliente;
import pe.edu.upeu.proyect_final02.utils.UtilsAlerts;

import java.util.Optional;

public class ClienteController {
    private static UtilsAlerts utilsAlerts = new UtilsAlerts();

    @FXML
    private TableView<Cliente> tableCliente;
    //Variable co relativa
    @FXML
    private TableColumn<Cliente, Number> ColNumero;
    @FXML
    private TableColumn<Cliente, Integer> ColId;
    @FXML
    private TableColumn<Cliente, String> ColNombre;
    @FXML
    private TableColumn<Cliente, String> ColApellidos;
    @FXML
    private TableColumn<Cliente, String> ColDni;
    @FXML
    private TableColumn<Cliente, String> ColEmail;

    @FXML
    private TextField txtBuscar;

    private final IClienteDao clienteDao = new ClienteDaoImpl();
    private ObservableList<Cliente> listaClientes;

    @FXML
    public void initialize() {
        ColNumero.setCellFactory(colum -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });

        ColId.setCellValueFactory(cellData -> cellData.getValue().idclienteProperty().asObject());
        ColNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        ColApellidos.setCellValueFactory(cellData -> cellData.getValue().apellidosProperty());
        ColDni.setCellValueFactory(cellData -> cellData.getValue().dniProperty());
        ColEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        //Llamar al metodo
        cargarDatos();
    }

    private void cargarDatos() {
        listaClientes = FXCollections.observableArrayList(clienteDao.listAll());
        tableCliente.setItems(listaClientes);
    }
    @FXML
    private void handleSearch() {
        String texto = txtBuscar.getText() == null ? "" : txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            cargarDatos();
            return;
        }
        Cliente encontrado = clienteDao.searchByDni(texto);
        listaClientes = FXCollections.observableArrayList();
        if (encontrado != null) {
            listaClientes.add(encontrado);
        }
        tableCliente.setPlaceholder(new Label("No se encontro ningun cliente con ese DNI"));
        tableCliente.setItems(listaClientes);
    }
    @FXML
    private void handleClear() {
        txtBuscar.clear();
        cargarDatos();
    }
    @FXML
    private void handleDelete() {
        Cliente selectId = tableCliente.getSelectionModel().getSelectedItem();
        if (selectId != null) {
            Alert confirm = utilsAlerts.mostrarAlerts("Confirmar eliminacion", "Estas seguro de eliminar este cliente " + selectId.getNombre() + "?", "CONFIRMATION");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (clienteDao.delete(selectId)) {
                    listaClientes.remove(selectId);
                } else {
                    Alert warning = utilsAlerts.mostrarAlerts("Seleccion requerrida", "seleccione un cliente para eliminar",
                            "WARNING");
                    warning.showAndWait();
                }
            }
        }
    }

    @FXML
    private void handleNew() {
        System.out.println("Abriendo Formulario");
        showForm(null);
    }
    @FXML
    private void handleEdit() {
        Cliente selectClient = tableCliente.getSelectionModel().getSelectedItem();
        if (selectClient !=null) {
            Cliente clienteFull = clienteDao.searchById(selectClient.getIdcliente());
            showForm(clienteFull);
        } else {
            Alert alert = utilsAlerts.mostrarAlerts("Sleccion Requerida","", "WARNING");
            alert.show();
        }
    }

    private void showForm(Cliente c) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/pe/edu/upeu/proyect_final02/views/cliente-form.fxml"));
            Parent root = loader.load();
            ClienteFromController controler = loader.getController();
            controler.setCliente(c);
            Stage stage = new Stage();
            stage.setTitle(c==null ?"nuevo cliente": "editar cliente");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            if (controler.isSaved()){
                Cliente clienteProcesado = controler.getCliente();
                if (c==null){
                    boolean result = clienteDao.insert(clienteProcesado);
                    if (result){
                        Alert alert = utilsAlerts.mostrarAlerts("success", "registro sastisfactorio", "CONFIRMATION");
                        alert.show();
                    }else{
                        Alert alert = utilsAlerts.mostrarAlerts("ERROR", "ERROR", "ERROR");
                        alert.show();
                    }
                }else{
                    boolean result = clienteDao.update (clienteProcesado);
                    if (result){
                        Alert alert = utilsAlerts.mostrarAlerts("success", "registro sastisfactorio", "WARNING");
                        alert.show();
                    }else{
                        Alert alert = utilsAlerts.mostrarAlerts("ERROR", "Error", "ERROR");
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
