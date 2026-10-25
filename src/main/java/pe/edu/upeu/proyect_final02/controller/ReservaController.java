package pe.edu.upeu.proyect_final02.controller;

import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pe.edu.upeu.proyect_final02.HelloApplication;
import pe.edu.upeu.proyect_final02.implementacion.ClienteDaoImpl;
import pe.edu.upeu.proyect_final02.implementacion.LibrosDaoImpl;
import pe.edu.upeu.proyect_final02.implementacion.ReservaDaoImpl;
import pe.edu.upeu.proyect_final02.interfaces.IClienteDao;
import pe.edu.upeu.proyect_final02.interfaces.ILibrosDao;
import pe.edu.upeu.proyect_final02.interfaces.IReservaDao;
import pe.edu.upeu.proyect_final02.modelo.Cliente;
import pe.edu.upeu.proyect_final02.modelo.Libros;
import pe.edu.upeu.proyect_final02.modelo.Reserva;
import pe.edu.upeu.proyect_final02.utils.UtilsAlerts;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservaController {
    private static final UtilsAlerts utilAlerts = new UtilsAlerts();
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private TableView<Reserva> tableReserva;
    @FXML
    private TableColumn<Reserva, String> ColNumero;
    @FXML
    private TableColumn<Reserva, String> ColCliente;
    @FXML
    private TableColumn<Reserva, String> ColDni;
    @FXML
    private TableColumn<Reserva, String> ColLibro;
    @FXML
    private TableColumn<Reserva, String> ColAlquiler;
    @FXML
    private TableColumn<Reserva, String> ColDevolucion;
    @FXML
    private TableColumn<Reserva, Double> ColTotal;
    @FXML
    private TableColumn<Reserva, String> ColEstado;

    @FXML
    private TextField txtBuscar;

    private final IReservaDao reservaDao = new ReservaDaoImpl();
    private final IClienteDao clienteDao = new ClienteDaoImpl();
    private final ILibrosDao librosDao = new LibrosDaoImpl();
    private ObservableList<Reserva> listaReservas;

    @FXML
    public void initialize() {
        // columna N: numero de fila
        ColNumero.setCellValueFactory(cd -> new SimpleStringProperty(""));
        ColNumero.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });

        ColCliente.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getCliente().getNombre()));
        ColDni.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getCliente().getDni()));
        ColLibro.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getLibro().getTitulo()));
        ColAlquiler.setCellValueFactory(cd -> new SimpleStringProperty(formatear(cd.getValue().getFecha_ingreso())));
        ColDevolucion.setCellValueFactory(cd -> new SimpleStringProperty(formatear(cd.getValue().getFecha_devolver())));
        ColTotal.setCellValueFactory(cd -> cd.getValue().totalProperty().asObject());
        ColEstado.setCellValueFactory(cd -> cd.getValue().estadoProperty());

        // el estado se pinta segun su valor
        ColEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(item);
                if (ReservaDaoImpl.ACTIVA.equals(item)) {
                    setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
                } else if (ReservaDaoImpl.NO_DEVUELTO.equals(item)) {
                    setStyle("-fx-text-fill: #c62828; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-text-fill: #616161;");
                }
            }
        });

        cargarDatos();
    }

    private String formatear(LocalDate fecha) {
        return fecha == null ? "" : fecha.format(FORMATO);
    }

    private void cargarDatos() {
        // antes de listar, las reservas vencidas pasan a "No devuelto"
        reservaDao.updateVencidas();
        listaReservas = FXCollections.observableArrayList(reservaDao.rlistAll());
        tableReserva.setPlaceholder(new Label("No hay reservas registradas"));
        tableReserva.setItems(listaReservas);
    }

    // Busca por DNI, nombre o apellidos del cliente, o por titulo del libro
    @FXML
    private void handleSearch() {
        String texto = txtBuscar.getText() == null ? "" : txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            cargarDatos();
            return;
        }
        reservaDao.updateVencidas();
        listaReservas = FXCollections.observableArrayList(reservaDao.rsearch(texto));
        tableReserva.setPlaceholder(new Label("No se encontraron reservas con ese dato"));
        tableReserva.setItems(listaReservas);
    }

    @FXML
    private void handleClear() {
        txtBuscar.clear();
        cargarDatos();
    }

    @FXML
    private void handleNew() {
        // solo se pueden reservar libros disponibles
        List<Libros> disponibles = librosDao.listAll().stream()
                .filter(l -> l.getEstado() != null && l.getEstado().trim().equalsIgnoreCase("Disponible"))
                .collect(Collectors.toList());
        if (disponibles.isEmpty()) {
            utilAlerts.mostrarAlerts("Sin libros", "No hay libros disponibles para reservar", "WARNING").showAndWait();
            return;
        }
        showForm(disponibles);
    }

    @FXML
    private void handleDevolver() {
        Reserva seleccionada = tableReserva.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            utilAlerts.mostrarAlerts("Seleccion requerida", "Seleccione una reserva", "WARNING").showAndWait();
            return;
        }
        if (ReservaDaoImpl.DEVUELTO.equals(seleccionada.getEstado())) {
            utilAlerts.mostrarAlerts("Reserva devuelta", "Esta reserva ya fue marcada como devuelta", "WARNING").showAndWait();
            return;
        }
        Alert confirm = utilAlerts.mostrarAlerts("Confirmar devolucion",
                "Marcar como devuelto el libro \"" + seleccionada.getLibro().getTitulo() + "\"?", "CONFIRMATION");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String estadoAnterior = seleccionada.getEstado();
            seleccionada.setEstado(ReservaDaoImpl.DEVUELTO);
            if (reservaDao.updateStatus(seleccionada)) {
                handleSearch();
            } else {
                seleccionada.setEstado(estadoAnterior);
                utilAlerts.mostrarAlerts("ERROR", "No se pudo registrar la devolucion", "ERROR").showAndWait();
            }
        }
    }

    @FXML
    private void handleDelete() {
        Reserva seleccionada = tableReserva.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            utilAlerts.mostrarAlerts("Seleccion requerida", "Seleccione una reserva para eliminar", "WARNING").showAndWait();
            return;
        }
        Alert confirm = utilAlerts.mostrarAlerts("Confirmar eliminacion",
                "Estas seguro de eliminar la reserva de " + seleccionada.getCliente().getNombre() + "?", "CONFIRMATION");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (reservaDao.delete(seleccionada)) {
                handleSearch();
            } else {
                utilAlerts.mostrarAlerts("ERROR", "No se pudo eliminar la reserva", "ERROR").showAndWait();
            }
        }
    }

    private void showForm(List<Libros> disponibles) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/pe/edu/upeu/proyect_final02/views/reserva-from.fxml"));
            Parent root = loader.load();
            ReservaFromController form = loader.getController();
            form.setLibrosDisponibles(disponibles);
            Stage stage = new Stage();
            stage.setTitle("nueva reserva");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (form.isSaved()) {
                Reserva nueva = form.getReserva();
                // si el cliente no existe en la tabla cliente, se crea aqui
                Cliente cliente = obtenerOCrearCliente(nueva.getCliente());
                if (cliente == null) {
                    utilAlerts.mostrarAlerts("ERROR", "No se pudo registrar el cliente", "ERROR").showAndWait();
                    return;
                }
                nueva.setCliente(cliente);
                if (!reservaDao.insert(nueva)) {
                    utilAlerts.mostrarAlerts("ERROR",
                            "No se pudo registrar la reserva", "ERROR").showAndWait();
                }
                handleSearch();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Busca el cliente por DNI; si no existe, lo inserta en la tabla cliente y devuelve el registrado
    private Cliente obtenerOCrearCliente(Cliente datos) {
        Cliente existente = clienteDao.searchByDni(datos.getDni());
        if (existente != null) {
            return existente;
        }
        if (!clienteDao.insert(datos)) {
            return null;
        }
        return clienteDao.searchByDni(datos.getDni());
    }
}
