package pe.edu.upeu.proyect_final02.controller;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import pe.edu.upeu.proyect_final02.implementacion.ClienteDaoImpl;
import pe.edu.upeu.proyect_final02.implementacion.ReservaDaoImpl;
import pe.edu.upeu.proyect_final02.interfaces.IClienteDao;
import pe.edu.upeu.proyect_final02.interfaces.IReservaDao;
import pe.edu.upeu.proyect_final02.modelo.Cliente;
import pe.edu.upeu.proyect_final02.modelo.Libros;
import pe.edu.upeu.proyect_final02.modelo.Reserva;
import pe.edu.upeu.proyect_final02.utils.UtilsAlerts;

import java.time.LocalDate;
import java.util.List;

public class ReservaFromController {
    private static final UtilsAlerts utilsAlerts = new UtilsAlerts();
    private final IClienteDao clienteDao = new ClienteDaoImpl();
    private final IReservaDao reservaDao = new ReservaDaoImpl();

    @FXML
    private TextField txtBuscarLibro;
    @FXML
    private ListView<Libros> lvLibros;
    @FXML
    private TextField txtDni, txtNombre, txtApellidos, txtEmail, txtTotal;
    @FXML
    private DatePicker dpAlquiler, dpDevolucion;
    @FXML
    private Label lbClienteInfo;

    private FilteredList<Libros> librosFiltrados;
    private Reserva reserva;
    private boolean saved = false;

    public boolean isSaved() {
        return saved;
    }

    public Reserva getReserva() {
        return reserva;
    }

    @FXML
    public void initialize() {
        dpAlquiler.setValue(LocalDate.now());
        dpDevolucion.setValue(LocalDate.now().plusDays(7));

        lvLibros.setCellFactory(lv -> new ListCell<Libros>() {
            @Override
            protected void updateItem(Libros l, boolean empty) {
                super.updateItem(l, empty);
                setText(empty || l == null ? null : l.getNumero() + " - " + l.getTitulo() + " (" + l.getAutor() + ")");
            }
        });
        // buscador de libros: filtra por titulo, autor o numero mientras se escribe
        txtBuscarLibro.textProperty().addListener((obs, anterior, texto) -> {
            if (librosFiltrados == null) {
                return;
            }
            String filtro = texto == null ? "" : texto.trim().toLowerCase();
            librosFiltrados.setPredicate(l -> filtro.isEmpty()
                    || contiene(l.getTitulo(), filtro)
                    || contiene(l.getAutor(), filtro)
                    || contiene(l.getNumero(), filtro));
        });
        // al salir del campo DNI (o con Enter) se busca si el cliente ya existe
        txtDni.focusedProperty().addListener((obs, estabaEnfocado, estaEnfocado) -> {
            if (!estaEnfocado) {
                autocompletarCliente();
            }
        });
        txtDni.setOnAction(e -> autocompletarCliente());
    }

    // Lo llama ReservaController antes de mostrar el formulario
    public void setLibrosDisponibles(List<Libros> libros) {
        librosFiltrados = new FilteredList<>(FXCollections.observableArrayList(libros), l -> true);
        lvLibros.setItems(librosFiltrados);
    }
    private boolean contiene(String campo, String filtro) {
        return campo != null && campo.toLowerCase().contains(filtro);
    }
    private void autocompletarCliente() {
        String dni = txtDni.getText() == null ? "" : txtDni.getText().trim();
        if (dni.isEmpty()) {
            lbClienteInfo.setText("");
            return;
        }
        Cliente c = clienteDao.searchByDni(dni);
        if (c != null) {
            txtNombre.setText(c.getNombre());
            txtApellidos.setText(c.getApellidos());
            txtEmail.setText(c.getEmail());
            lbClienteInfo.setText("Cliente existente: se usara su registro.");
        } else {
            lbClienteInfo.setText("Cliente nuevo: se registrara al guardar la reserva.");
        }
    }

    private void cerrar() {
        Stage stage = (Stage) txtDni.getScene().getWindow();
        stage.close();
    }

    private void error(String titulo, String mensaje) {
        utilsAlerts.mostrarAlerts(titulo, mensaje, "ERROR").showAndWait();
    }

    @FXML
    private void handleCancel() {
        cerrar();
    }

    @FXML
    private void handleSave() {
        Libros libro = lvLibros.getSelectionModel().getSelectedItem();
        String dni = txtDni.getText() == null ? "" : txtDni.getText().trim();
        String nombre = txtNombre.getText() == null ? "" : txtNombre.getText().trim();
        String apellidos = txtApellidos.getText() == null ? "" : txtApellidos.getText().trim();
        String email = txtEmail.getText() == null ? "" : txtEmail.getText().trim();
        LocalDate alquiler = dpAlquiler.getValue();
        LocalDate devolucion = dpDevolucion.getValue();

        if (libro == null || dni.isEmpty() || nombre.isEmpty() || alquiler == null || devolucion == null) {
            error("Datos incompletos", "Libro, DNI, Nombre y las dos fechas son obligatorios");
            return;
        }
        if (!devolucion.isAfter(alquiler)) {
            error("Fechas invalidas", "La fecha de devolucion debe ser posterior a la fecha de alquiler");
            return;
        }

        Cliente cliente = new Cliente();
        cliente.setDni(dni);
        cliente.setNombre(nombre);
        cliente.setApellidos(apellidos);
        cliente.setEmail(email);

        reserva = new Reserva();
        reserva.setLibro(libro);
        reserva.setCliente(cliente);
        reserva.setFecha_reserva(LocalDate.now());
        reserva.setFecha_ingreso(alquiler);
        reserva.setFecha_devolver(devolucion);
        // la columna total guarda la multa: empieza en 0 y pasa a S/20 si no se devuelve a tiempo
        reserva.setTotal(0);
        reserva.setEstado("Activa");
        // un libro alquilado no se puede volver a alquilar hasta que lo devuelvan
        if (!reservaDao.validateLibro(reserva)) {
            reserva = null;
            error("Libro no disponible", "El libro ya esta alquilado y aun no ha sido devuelto");
            return;
        }
        saved = true;
        cerrar();
    }
}
