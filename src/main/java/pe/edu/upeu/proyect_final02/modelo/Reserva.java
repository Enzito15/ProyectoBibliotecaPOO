package pe.edu.upeu.proyect_final02.modelo;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Reserva {
    private final IntegerProperty idreserva = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> fecha_reserva = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> fecha_ingreso = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> fecha_devolver = new SimpleObjectProperty<>();
    private final StringProperty estado = new SimpleStringProperty();
    private final DoubleProperty total = new SimpleDoubleProperty();
    private final ObjectProperty<Cliente> cliente = new SimpleObjectProperty<>();
    private final ObjectProperty<Libros> libro = new SimpleObjectProperty<>();

    public final Libros getLibro() {
        return libro.get();
    }

    public final void setLibro(Libros value) {
        libro.set(value);
    }

    public final ObjectProperty<Libros> libroProperty() {
        return libro;
    }


    public final Cliente getCliente() {
        return cliente.get();
    }

    public final void setCliente(Cliente value) {
        cliente.set(value);
    }

    public final ObjectProperty<Cliente> clienteProperty() {
        return cliente;
    }


    public final double getTotal() {
        return total.get();
    }

    public final void setTotal(double value) {
        total.set(value);
    }

    public final DoubleProperty totalProperty() {
        return total;
    }


    public final String getEstado() {
        return estado.get();
    }

    public final void setEstado(String value) {
        estado.set(value);
    }

    public final StringProperty estadoProperty() {
        return estado;
    }


    public final LocalDate getFecha_devolver() {
        return fecha_devolver.get();
    }

    public final void setFecha_devolver(LocalDate value) {
        fecha_devolver.set(value);
    }

    public final ObjectProperty<LocalDate> fecha_devolverProperty() {
        return fecha_devolver;
    }


    public final LocalDate getFecha_ingreso() {
        return fecha_ingreso.get();
    }

    public final void setFecha_ingreso(LocalDate value) {
        fecha_ingreso.set(value);
    }

    public final ObjectProperty<LocalDate> fecha_ingresoProperty() {
        return fecha_ingreso;
    }


    public final LocalDate getFecha_reserva() {
        return fecha_reserva.get();
    }

    public final void setFecha_reserva(LocalDate value) {
        fecha_reserva.set(value);
    }

    public final ObjectProperty<LocalDate> fecha_reservaProperty() {
        return fecha_reserva;
    }


    public final int getIdreserva() {
        return idreserva.get();
    }

    public final void setIdreserva(int value) {
        idreserva.set(value);
    }

    public final IntegerProperty idreservaProperty() {
        return idreserva;
    }

}
