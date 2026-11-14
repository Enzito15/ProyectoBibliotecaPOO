package pe.edu.upeu.proyect_final02.modelo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Usuario extends Persona {
    private final IntegerProperty idusuario = new SimpleIntegerProperty();
    private final StringProperty user = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty tipoUser = new SimpleStringProperty(this, "tipoUser");

    public Usuario() {
    }

    public final String getTipoUser() {
        return tipoUser.get();
    }

    public final void setTipoUser(String value) {
        tipoUser.set(value);
    }

    public final StringProperty tipoUserProperty() {
        return tipoUser;
    }


    public final String getPassword() {
        return password.get();
    }

    public final void setPassword(String value) {
        password.set(value);
    }

    public final StringProperty passwordProperty() {
        return password;
    }


    public final String getUser() {
        return user.get();
    }

    public final void setUser(String value) {
        user.set(value);
    }

    public final StringProperty userProperty() {
        return user;
    }


    public final int getIdusuario() {
        return idusuario.get();
    }

    public final void setIdusuario(int value) {
        idusuario.set(value);
    }

    public final IntegerProperty idusuarioProperty() {
        return idusuario;
    }

}
