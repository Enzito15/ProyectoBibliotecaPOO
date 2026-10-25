package pe.edu.upeu.proyect_final02.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private ComboBox<Libros> cmbLibro;
    @FXML
    private TextField txtDni, txtNombre, txtApellidos, txtEmail, txtTotal;
    @FXML
    private DatePicker dpAlquiler, dpDevolucion;
    @FXML
    private Label lbClienteInfo;

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

        cmbLibro.setConverter(new StringConverter<Libros>() {
            @Override
            public String toString(Libros l) {
                return l == null ? "" : l.getNumero() + " - " + l.getTitulo();
            }

            @Override
            public Libros fromString(String s) {
                return null;
            }
        });
        // al elegir un libro, el total se llena con su precio (se puede cambiar)
        cmbLibro.valueProperty().addListener((obs, anterior, libro) -> {
            if (libro != null) {
                txtTotal.setText(String.valueOf(libro.getPrecio()));
            }
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
        cmbLibro.setItems(FXCollections.observableArrayList(libros));
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
        Libros libro = cmbLibro.getValue();
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
        double total;
        try {
            String textoTotal = txtTotal.getText() == null ? "" : txtTotal.getText().trim();
            total = textoTotal.isEmpty() ? libro.getPrecio() : Double.parseDouble(textoTotal.replace(',', '.'));
            if (total < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            error("Total invalido", "El total debe ser un numero mayor o igual a 0");
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
        reserva.setTotal(total);
        // si la fecha limite ya paso, nace como "No devuelto"
        reserva.setEstado(devolucion.isBefore(LocalDate.now()) ? "No devuelto" : "Activa");
        // el libro no puede estar sin devolver ni reservado en esas fechas
        if (!reservaDao.validateLibro(reserva)) {
            reserva = null;
            error("Libro no disponible", "El libro ya esta reservado o sin devolver en esas fechas");
            return;
        }
        saved = true;
        cerrar();
    }
}
