package pe.edu.upeu.proyect_final02.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private TableColumn<Cliente, Number> ColNumero ;
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

    private final IClienteDao clienteDao = new ClienteDaoImpl();
    private ObservableList<Cliente> listaClientes;

    @FXML
    public void initialize(){
        ColNumero.setCellFactory(colum -> new TableCell<>(){
            @Override
            protected void updateItem(Number item, boolean empty){
                super.updateItem(item, empty);

                if (empty){
                    setText(null);
                }else{
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
    private void cargarDatos(){
        listaClientes = FXCollections.observableArrayList(clienteDao.listAll());
        tableCliente.setItems(listaClientes);
    }
    private void handleDelete(){
        Cliente selectId = tableCliente.getSelectionModel().getSelectedItem();
        if (selectId !=null){
            Alert confirm = utilsAlerts.mostrarAlerts("Confirmar eliminacion", "Estas seguro de eliminar este cliente " + selectId.getNombre() + "?","CONFIRMATION" );
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                if (clienteDao.delete(selectId)){
                    listaClientes.remove(selectId);
                }else{
                    Alert warning = utilsAlerts.mostrarAlerts("Seleccion requerrida", "seleccione un cliente para eliminar",
                            "WARNING");
                    warning.showAndWait();
                }
            }
        }
    }
    private void handleNew(){

    }
    private void showForm(Cliente c){

    }
}
