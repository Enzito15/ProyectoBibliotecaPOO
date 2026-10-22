package pe.edu.upeu.proyect_final02.modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cliente extends Persona {
    private final IntegerProperty idcliente = new SimpleIntegerProperty();
    private final StringProperty email = new SimpleStringProperty();

    public Cliente() {
    }

    public Cliente(String nombre, String apellidos, String dni,int idcliente,String email) {
        super(nombre, apellidos, dni);
        setIdcliente(idcliente);
        setEmail(email);
    }

    public final String getEmail() {
        return email.get();
    }

    public final void setEmail(String value) {
        email.set(value);
    }

    public final StringProperty emailProperty() {
        return email;
    }


    public final int getIdcliente() {
        return idcliente.get();
    }

    public final void setIdcliente(int value) {
        idcliente.set(value);
    }

    public final IntegerProperty idclienteProperty() {
        return idcliente;
    }


}
