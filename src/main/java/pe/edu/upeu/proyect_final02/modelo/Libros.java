package pe.edu.upeu.proyect_final02.modelo;

import javafx.beans.property.*;

public class Libros {
    private final IntegerProperty idlibros = new SimpleIntegerProperty();
    private final StringProperty numero = new SimpleStringProperty(this, "numero");
    private final StringProperty titulo = new SimpleStringProperty(this, "titulo");
    private final StringProperty volumen = new SimpleStringProperty(this, "volumen");
    private final StringProperty autor = new SimpleStringProperty(this, "autor");
    private final DoubleProperty precio = new SimpleDoubleProperty(this, "precio");
    private final StringProperty estado = new SimpleStringProperty(this, "estado");

    public Libros() {
    }

    public Libros(int idlibros, String numero, String titulo, String volumen, String autor, double precio, String estado ) {
        setIdlibros(idlibros);
        setNumero(numero);
        setTitulo(titulo);
        setVolumen(volumen);
        setAutor(autor);
        setPrecio(precio);
        setEstado(estado);
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


    public final double getPrecio() {
        return precio.get();
    }

    public final void setPrecio(double value) {
        precio.set(value);
    }

    public final DoubleProperty precioProperty() {
        return precio;
    }


    public final String getAutor() {
        return autor.get();
    }

    public final void setAutor(String value) {
        autor.set(value);
    }

    public final StringProperty autorProperty() {
        return autor;
    }

    public final String getVolumen() {
        return volumen.get();
    }

    public final void setVolumen(String value) {
        volumen.set(value);
    }

    public final StringProperty volumenProperty() {
        return volumen;
    }


    public final String getTitulo() {
        return titulo.get();
    }

    public final void setTitulo(String value) {
        titulo.set(value);
    }

    public final StringProperty tituloProperty() {
        return titulo;
    }


    public final String getNumero() {
        return numero.get();
    }

    public final void setNumero(String value) {
        numero.set(value);
    }

    public final StringProperty numeroProperty() {
        return numero;
    }


    public final int getIdlibros() {
        return idlibros.get();
    }

    public final void setIdlibros(int value) {
        idlibros.set(value);
    }

    public final IntegerProperty idlibrosProperty() {
        return idlibros;
    }

}
